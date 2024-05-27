
import java.util.ArrayList;

public class App {

    public static ArrayList<Point> cluster_0_cities = new ArrayList<>();
    public static ArrayList<Point> cluster_1_cities = new ArrayList<>();

    public static Point centeroid_0 = new Point(4, 0);
    public static Point centeroid_1 = new Point(10, 0);

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
            System.out.println("Centroid 0 coordinates: " + centeroid_0.x + " " + centeroid_0.y);
            System.out.println("Centroid 1 coordinates: " + centeroid_1.x + " " + centeroid_1.y);
            Point old_x_c0 = new Point(centeroid_0.x, centeroid_0.y);
            Point old_x_c1 = new Point(centeroid_1.x, centeroid_1.y);
    
            update_centroid(cluster_0_cities, centeroid_0);
            update_centroid(cluster_1_cities, centeroid_1);
            
    
    
            //After Update
            System.out.println("Centroid 0 new coordinates: " + centeroid_0.x + " " + centeroid_0.y);
            System.out.println("Centroid 1 new coordinates: " + centeroid_1.x + " " + centeroid_1.y);
            Point new_x_c0 = new Point(centeroid_0.x, centeroid_0.y);
            Point new_x_c1 = new Point(centeroid_1.x, centeroid_1.y);
            //Display displacements
            double displacement_c0 = Math.sqrt(Math.pow((new_x_c0.x - old_x_c0.x),2) + Math.pow((new_x_c0.y - old_x_c0.y),2)); 
            double displacement_c1 = Math.sqrt(Math.pow((new_x_c1.x - old_x_c1.x),2) + Math.pow((new_x_c1.y - old_x_c1.y),2));
            System.out.println("Centroid 0 displacement: " + Math.sqrt(Math.pow((new_x_c0.x - old_x_c0.x),2) + Math.pow((new_x_c0.y - old_x_c0.y),2)));
            System.out.println("Centroid 1 displacement: " + Math.sqrt(Math.pow((new_x_c1.x - old_x_c1.x),2) + Math.pow((new_x_c1.y - old_x_c1.y),2)));
            if (displacement_c0 == 0 && displacement_c1 == 0) {
                break;
            }
        }

        

    }

    public static void main(String[] args) {
        int city_length = 8;
        int cluster_length = 2;

        Point cities[] = new Point[city_length];
        Point centeroids[] = new Point[cluster_length];

        cities[4] = new Point(10, 10);
        cities[5] = new Point(11, 11);
        cities[6] = new Point(10, 11);
        cities[7] = new Point(11, 10);

        cities[0] = new Point(4, 4);
        cities[1] = new Point(4, 5);
        cities[2] = new Point(5, 4);
        cities[3] = new Point(5, 5);

        centeroids[0] = centeroid_0;
        centeroids[1] = centeroid_1;

        k_means(cities, centeroids, city_length, cluster_length);

        /*
    System.out.println("Cluster 0 Cities:");
    for (int i = 0; i < cluster_0_cities.size(); i++) {
            System.out.println("City " + i + ": " + cluster_0_cities.get(i).x + " " + cluster_0_cities.get(i).y);

        }

        System.out.println("Cluster 1 Cities:");
        for (int i = 0; i < cluster_1_cities.size(); i++) {
            System.out.println("City " + i + ": " + cluster_1_cities.get(i).x + " " + cluster_1_cities.get(i).y);

        }
    
         */
    }
}
