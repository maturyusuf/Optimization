//150121016-150122058-524123004
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class TspMain {
	
	public static int distance(Point a,Point b) {
		int ret=(int)Math.sqrt(Math.pow(a.x-b.x,2)+(Math.pow(a.y-b.y,2)));
		return ret;
	}
	
	public static int addPoint(ArrayList<Point>tspCities,Point added) {
		int size=tspCities.size();
		int minDist=2147483647;
		int currentDistance=0;
		int Index=0;
		int sizeMinus=size-1;
		for(int a=0;a<sizeMinus;a++) {
			currentDistance = distance(tspCities.get(a),added)+distance(tspCities.get(a+1),added)-distance(tspCities.get(a),tspCities.get(a+1));
			if(currentDistance<minDist) {
				minDist=currentDistance;
				Index=a;
			}
		}
		currentDistance = distance(tspCities.get(0),added)+distance(tspCities.get(sizeMinus),added)-distance(tspCities.get(0),tspCities.get(sizeMinus));
		if(currentDistance<minDist) {
			minDist=currentDistance;
			Index=sizeMinus;
		}
		return Index;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		/*System.out.println("Enter input file name:");
		Scanner inputConsole=new Scanner(System.in);
		String inputFileName=inputConsole.next();*/
		String inputFileName="input.txt";
		
		
		
		java.io.File file=new java.io.File(inputFileName);
		
		Scanner input = new Scanner(file);
		ArrayList<Point> allCities = new ArrayList<Point>();
		int size=0;
		while(input.hasNext()) {
			int number=input.nextInt();
			int xCoordinate = input.nextInt();
			int yCoordinate=input.nextInt();
			Point newPoint=new Point(xCoordinate,yCoordinate, number);
			allCities.add(newPoint);
			size++;
		}
		input.close();
		
		int lastIndex=size-1;
		ArrayList<Point> tspCities = new ArrayList<Point>();
		for(int a=0;a<3;a++) {
			tspCities.add(allCities.get(0));
			allCities.remove(0);
		}size-=3;
		int tspSize=3;
		while(size>0) {
			int removed=addPoint(tspCities,allCities.get(0))+1;
			if(removed==tspSize) {
				tspCities.add(allCities.get(0));
			}else {
				tspCities.add(removed,allCities.get(0));	
			}
			allCities.remove(0);
			size--;
			tspSize++;
		}
		int sizeMinus2=lastIndex-1;
		int c1=0;
		int c2=0;
		int totalMin=2147483647;
		int currentMin=0;
		for(int m=0;m<=sizeMinus2;m++) {
			currentMin=-distance(tspCities.get(m),tspCities.get(m+1));
			for(int n=m+2;n<lastIndex;n++) {
				currentMin-=distance(tspCities.get(n),tspCities.get(n+1));
				currentMin+=distance(tspCities.get(m),tspCities.get(n+1));
				currentMin+=distance(tspCities.get(m+1),tspCities.get(n));
				if(currentMin<totalMin) {
					c1=m;
					c2=n;
					totalMin=currentMin;
				}
			}
		}
		ArrayList<Point> ts1Cities = new ArrayList<Point>();
		while(lastIndex>c2) {
			ts1Cities.add(tspCities.get(lastIndex));
			tspCities.remove(lastIndex);
			lastIndex--;
		}
		for(int m=c1;m>=0;m--) {
			ts1Cities.add(tspCities.get(m));
			tspCities.remove(m);
		}
		
		/*int half=tspCities.size()/2;
		for(int k=0;k<half;k++) {
			ts1Cities.add(tspCities.get(0));
			tspCities.remove(0);
		}*/
		int ts1CitySize=ts1Cities.size();
		int ts2CitySize=tspCities.size();
		int ts1CityLastIndex=ts1Cities.size()-1;
		int ts2CityLastIndex=tspCities.size()-1;
		int totaldistance1=0;
		int totaldistance2=0;
		for(int a = 0;a<ts1CityLastIndex;a++){
			totaldistance1+=distance(ts1Cities.get(a),ts1Cities.get(a+1));
		}
		totaldistance1+=distance(ts1Cities.get(0),ts1Cities.get(ts1CitySize-1));
		
		for(int a = 0;a<ts2CityLastIndex;a++){
			totaldistance2+=distance(tspCities.get(a),tspCities.get(a+1));
		}
		totaldistance2+=distance(tspCities.get(0),tspCities.get(ts2CitySize-1));
		
		try (PrintWriter writer = new PrintWriter("output.txt")) {
			writer.println(totaldistance1+totaldistance2);
			writer.println(totaldistance1 + " " + ts1CitySize);
	        writer.println(" ");
	        for (int j = 0; j < ts1CitySize; j++) {
	        	writer.println(ts1Cities.get(j).number);
	        }
	        writer.println(" ");
	        
	        writer.println(totaldistance2 + " " + ts2CitySize);
	        for (int j = 0; j < ts2CitySize; j++) {
	        	writer.println(tspCities.get(j).number);
	        }
	        
		}
		
	}
	
}
