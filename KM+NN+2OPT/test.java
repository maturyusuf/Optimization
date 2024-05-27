
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;  // Import this class to handle errors
import java.util.Collections;
import java.util.Scanner;

public class test {

    public static int firstTestInputLength = 318;
    public static int secondTestInputLength = 984;
    public static int thirdTestInputLength = 7397;
    public static int forthTestInputLength = 50000;

    public static String firstTestInput = "test-input-1.txt";
    public static String secondTestInput = "test-input-2.txt";
    public static String thirdTestInput = "test-input-3.txt";
    public static String forthTestInput = "test-input-4.txt";

    static double totalDistance0;
    static double totalDistance1;
    static boolean clusterNumber = true;

    public static ArrayList<Point> cluster_0_cities = new ArrayList<>();
    public static ArrayList<Point> cluster_1_cities = new ArrayList<>();

    public static Point centeroid_0 = new Point(550000, 120000, -1); // adjust centeroids
    public static Point centeroid_1 = new Point(120000, 550000, -2);

    public static void distance(Point cities[], Point clusters[], int cities_array_length, int clusters_array_length) {

        for (int city = 0; city < cities_array_length; city++) {

            double dx = clusters[0].x - cities[city].x;
            double dy = clusters[0].y - cities[city].y;

            double c0_dist = Math.sqrt(dx * dx + dy * dy);

            dx = clusters[1].x - cities[city].x;
            dy = clusters[1].y - cities[city].y;

            double c1_dist = Math.sqrt(dx * dx + dy * dy);
            assign_cities(cities, city, c0_dist, c1_dist);

        }

    }

    public static void assign_cities(Point cities[], int city, double c0_dist, double c1_dist) {
        if (c0_dist < c1_dist) {
            cluster_0_cities.add(cities[city]);
        } else {
            cluster_1_cities.add(cities[city]);
        }

    }

    public static void update_centroid(ArrayList<Point> cluster, Point centeroid) {
        double sum_x = 0;
        double sum_y = 0;
        for (int p = 0; p < cluster.size(); p++) {

            sum_x += cluster.get(p).x;
            sum_y += cluster.get(p).y;

        }
        double mean_x = sum_x / cluster.size();
        double mean_y = sum_y / cluster.size();

        centeroid.x = mean_x;
        centeroid.y = mean_y;
    }

    public static void k_means(Point cities[], Point clusters[], int cities_array_length, int clusters_array_length) {

        while (true) {
            distance(cities, clusters, cities_array_length, clusters_array_length);
            //Before Update
            //System.out.println("Centroid 0 coordinates: " + centeroid_0.x + " " + centeroid_0.y);
            //System.out.println("Centroid 1 coordinates: " + centeroid_1.x + " " + centeroid_1.y);
            Point old_x_c0 = new Point(centeroid_0.x, centeroid_0.y, centeroid_0.number);
            Point old_x_c1 = new Point(centeroid_1.x, centeroid_1.y, centeroid_1.number);

            update_centroid(cluster_0_cities, centeroid_0);
            update_centroid(cluster_1_cities, centeroid_1);

            //After Update
            //System.out.println("Centroid 0 new coordinates: " + centeroid_0.x + " " + centeroid_0.y);
            //System.out.println("Centroid 1 new coordinates: " + centeroid_1.x + " " + centeroid_1.y);
            Point new_x_c0 = new Point(centeroid_0.x, centeroid_0.y, centeroid_0.number);
            Point new_x_c1 = new Point(centeroid_1.x, centeroid_1.y, centeroid_1.number);
            //Display displacements
            double displacement_c0 = Math.sqrt(Math.pow((new_x_c0.x - old_x_c0.x), 2) + Math.pow((new_x_c0.y - old_x_c0.y), 2));
            double displacement_c1 = Math.sqrt(Math.pow((new_x_c1.x - old_x_c1.x), 2) + Math.pow((new_x_c1.y - old_x_c1.y), 2));
            //System.out.println("Centroid 0 displacement: " + Math.sqrt(Math.pow((new_x_c0.x - old_x_c0.x),2) + Math.pow((new_x_c0.y - old_x_c0.y),2)));
            //System.out.println("Centroid 1 displacement: " + Math.sqrt(Math.pow((new_x_c1.x - old_x_c1.x),2) + Math.pow((new_x_c1.y - old_x_c1.y),2)));
            if (displacement_c0 < 0.5 && displacement_c1 < 0.5) {
                break;
            } else {
                cluster_0_cities.clear();
                cluster_1_cities.clear();
            }
        }

        System.out.println("K-means ended");
    }

    public static ArrayList<Integer> nearest_neighbors(double[][] distanceMatrix, ArrayList<Point> cityList) {
        int numberOfCities = distanceMatrix.length;
        boolean[] visited = new boolean[thirdTestInputLength];    //// First Change
        ArrayList<Integer> tour = new ArrayList<>();
        double totalDistance = 0;

        // Start from the first city
        int currentCity = 0;
        tour.add(cityList.get(currentCity).number);
        visited[cityList.get(currentCity).number] = true;

        for (int i = 1; i < numberOfCities; i++) {
            int nextCity = -1;
            double shortestDistance = Double.MAX_VALUE;

            // Find the nearest unvisited city
            for (int j = 0; j < numberOfCities; j++) {
                if (!visited[cityList.get(j).number] && distanceMatrix[currentCity][j] < shortestDistance) {
                    shortestDistance = distanceMatrix[currentCity][j];
                    nextCity = j;
                }
            }
            totalDistance += shortestDistance;
            if (nextCity != -1) {
                tour.add(cityList.get(nextCity).number);
                visited[cityList.get(nextCity).number] = true;
                currentCity = nextCity;
            }
        }

        // Return to the starting city
        totalDistance += distanceMatrix[currentCity][0];
        tour.add(cityList.get(0).number);

        if (clusterNumber) {
            totalDistance0 = totalDistance;
        } else {
            totalDistance1 = totalDistance;
        }

        return tour;
    }

    public static double[][] adjacency_matrix(ArrayList<Point> cluster) {
        double[][] adjacency_matrix = new double[cluster.size()][cluster.size()];
        int adj_matrix_length = adjacency_matrix.length;

        for (int i = 0; i < adj_matrix_length; i++) {
            for (int j = 0; j < adj_matrix_length; j++) {
                double dx = cluster.get(i).x - cluster.get(j).x;
                double dy = cluster.get(i).y - cluster.get(j).y;
                adjacency_matrix[i][j] = Math.sqrt(dx * dx + dy * dy);
            }
        }
        return adjacency_matrix;
    }

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.nanoTime();
        int city_length = thirdTestInputLength;   ////////// Second Change
        int cluster_length = 2;

        Point cities[] = new Point[city_length];
        Point centeroids[] = new Point[cluster_length];

        centeroids[0] = centeroid_0;
        centeroids[1] = centeroid_1;

        java.io.File file = new java.io.File(thirdTestInput); /////////// change name of the file

        try (Scanner input = new Scanner(file)) {

            while (input.hasNext()) {
                int number = input.nextInt();
                int xCoordinate = input.nextInt();
                int yCoordinate = input.nextInt();
                Point newPoint = new Point(xCoordinate, yCoordinate, number);
                cities[number] = newPoint;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        k_means(cities, centeroids, city_length, cluster_length);
///////////// Cities nad clusters
        double[][] cluster_0_adj = adjacency_matrix(cluster_0_cities);
        int cluster_0_size = cluster_0_cities.size();
        int cluster_1_size = cluster_1_cities.size();
        try (PrintWriter writer = new PrintWriter("C0-Cities after KMeans.txt")) {    ///////// Thirs change
            for (int i = 0; i < cluster_0_size; i++) {
                writer.println(cluster_0_cities.get(i).number + ",");
            }

        }

        try (PrintWriter writer = new PrintWriter("C1-Cities after KMeans.txt")) {    ///////// Thirs change
            for (int i = 0; i < cluster_1_size; i++) {
                writer.println(cluster_1_cities.get(i).number + ",");
            }

        }

        ArrayList<Integer> cluster_0_path = nearest_neighbors(cluster_0_adj, cluster_0_cities);
////////////////////////////////// Write path after NN for CLUSTER 0
        try (PrintWriter writer = new PrintWriter("C0-outputFileFirstInputAfterNN.txt")) {    ///////// Thirs change
            writer.println(Math.round(totalDistance0 + totalDistance1));
            writer.println(Math.round(totalDistance0) + " " + cluster_0_path.size());
            for (int i = 0; i < cluster_0_adj.length; i++) {
                writer.println(cluster_0_path.get(i));
            }

        }
        /////////////////////

        System.out.println("Total distance after nearest neighbors for first cluster: " + calculateTourDistance(cluster_0_path, cluster_0_adj, cluster_0_cities));
        cluster_0_path = optimize2Opt(cluster_0_path, cluster_0_adj, cluster_0_cities);

        clusterNumber = false;
        double[][] cluster_1_adj = adjacency_matrix(cluster_1_cities);

        ArrayList<Integer> cluster_1_path = nearest_neighbors(cluster_1_adj, cluster_1_cities);

        ////////////////////////////////// Write path after NN for CLUSTER 1
        try (PrintWriter writer = new PrintWriter("C1-outputFileFirstInputAfterNN.txt")) {    ///////// Thirs change
            writer.println(Math.round(totalDistance0 + totalDistance1));
            writer.println(Math.round(totalDistance0) + " " + cluster_1_path.size());
            for (int i = 0; i < cluster_1_adj.length; i++) {
                writer.println(cluster_1_path.get(i));

            }

        }
        /////////////////////

        System.out.println("Total distance after nearest neighbors for second cluster: " + calculateTourDistance(cluster_1_path, cluster_1_adj, cluster_1_cities));

        cluster_1_path = optimize2Opt(cluster_1_path, cluster_1_adj, cluster_1_cities);

        System.out.println(calculateTourDistance(cluster_1_path, cluster_1_adj, cluster_1_cities));

        totalDistance0 = calculateTourDistance(cluster_0_path, cluster_0_adj, cluster_0_cities);
        System.out.println("Total distance after 2 opt for first cluster: " + totalDistance0);

        totalDistance1 = calculateTourDistance(cluster_1_path, cluster_1_adj, cluster_1_cities);
        System.out.println("Total distance after 2 opt for second cluster: " + totalDistance1);
        System.out.println();

        try (PrintWriter writer = new PrintWriter("outputFileFirstInputAfter2Opt.txt")) {    ///////// Thirs change
            writer.println(Math.round(totalDistance0 + totalDistance1));
            writer.println(Math.round(totalDistance0) + " " + cluster_0_path.size());
            for (int i = 0; i < cluster_0_adj.length; i++) {
                writer.println(cluster_0_path.get(i));
            }

            writer.println("\n" + Math.round(totalDistance1) + " " + cluster_1_path.size());
            for (int i = 0; i < cluster_1_adj.length; i++) {
                writer.println(cluster_1_path.get(i));
            }
        }

        long endTime = System.nanoTime();

        System.out.println((endTime - startTime) / 1000000000);

        /*System.out.println(cluster_0_path.get(0));
        for (int i = 0; i < 5; i++) {
            int k,l;
            for(k = 0;k < cluster_0_cities.size();k++){
                if(cluster_0_path.get(i) == cluster_0_cities.get(k).number){
                    break;
                }
            }
            System.out.println(cluster_0_path.get(i) + " X: " + cluster_0_cities.get(k).x + " Y: " + cluster_0_cities.get(k).y);
            
        }*/
 /*System.out.println("Cluster 0 Cities:");
    for (int i = 0; i < cluster_0_cities.size(); i++) {
            System.out.println("City " + cluster_0_cities.get(i).number + ": " + cluster_0_cities.get(i).x + " " + cluster_0_cities.get(i).y);

        }

        System.out.println("Cluster 1 Cities:");
        for (int i = 0; i < cluster_1_cities.size(); i++) {
            System.out.println("City " + cluster_1_cities.get(i).number + ": " + cluster_1_cities.get(i).x + " " + cluster_1_cities.get(i).y);

        }*/
    }

    public static ArrayList<Integer> optimize2Opt(ArrayList<Integer> tour, double[][] distanceMatrix, ArrayList<Point> sortedList) {
        System.out.println("optimzation has begun.");
        boolean improvement = true;
        double bestDistance = calculateTourDistance(tour, distanceMatrix, sortedList);
        int change = 0;
        while (improvement) {
            improvement = false;
            for (int i = 1; i < tour.size() - 2; i++) {
                for (int k = i + 1; k < tour.size() - 1; k++) {
                    ArrayList<Integer> newTour = twoOptSwap(tour, i, k);
                    double newDistance = calculateTourDistance(newTour, distanceMatrix, sortedList);
                    if (newDistance < bestDistance) {
                        System.out.println("Change.");
                        change = change + 1;
                        tour = newTour;
                        bestDistance = newDistance;
                        improvement = true;

                        if (change > 20) {
                            return tour;

                        }

                    }
                }
            }
        }

        return tour;
    }

    public static double calculateTourDistance(ArrayList<Integer> tour, double[][] distanceMatrix, ArrayList<Point> sortedList) {
        //System.out.println("Distance is being calculated.");
        double totalDistance = 0.0;
        int size = tour.size();
        for (int i = 0; i < size - 1; i++) {
            int k = 0, l = 0;
            for (k = 0; k < sortedList.size(); k++) {
                if (tour.get(i) == sortedList.get(k).number) {
                    break;
                }
            }
            for (l = 0; l < sortedList.size(); l++) {
                if (tour.get(i + 1) == sortedList.get(l).number) {
                    break;
                }
            }
            totalDistance += distanceMatrix[k][l];
        }

        // Add the distance from the last city back to the first city
        int k = 0, l = 0;
        for (k = 0; k < sortedList.size(); k++) {
            if (tour.get(size - 1) == sortedList.get(k).number) {
                break;
            }
        }
        for (l = 0; l < sortedList.size(); l++) {
            if (tour.get(0) == sortedList.get(l).number) {
                break;
            }
        }
        totalDistance += distanceMatrix[k][l];

        return totalDistance;
    }

    private static ArrayList<Integer> twoOptSwap(ArrayList<Integer> tour, int i, int k) {
        ArrayList<Integer> newTour = new ArrayList<>(tour.subList(0, i));
        ArrayList<Integer> reversedSegment = new ArrayList<>(tour.subList(i, k + 1));
        Collections.reverse(reversedSegment);
        newTour.addAll(reversedSegment);
        newTour.addAll(tour.subList(k + 1, tour.size()));
        return newTour;
    }
}
