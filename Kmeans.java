import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;


public class Kmeans {
	private static final int numOfCluster = 3;
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scanFile = new Scanner(new File("/home/nawrin/A.txt"));
		List<Point> points = new ArrayList<Point>();
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		Random rand = new Random();
		
		while(scanFile.hasNext()) {
			String coord = scanFile.nextLine();
			String[] x_y = coord.split(" ");
			Point point = new Point(Double.parseDouble(x_y[0]), Double.parseDouble(x_y[1]));
			points.add(point);
		}
		System.out.println("points" + points.size());
		int i = 0;
		while (i < numOfCluster) {
			Point p = points.get(rand.nextInt(points.size()));
			if (!hasElement(p, clusterList)) {
				//ArrayList<Point> center = new ArrayList<Point>();
				//center.add(p);
				clusterList.add(new Cluster(null, p));
				i++;
			}
		}
		
		//System.out.println(clusterList.size());
		int closestCenter = 0;
		int currentCluster = 0;
		double distortion = 0;
		double oldDistortion = 0;
		while(true) {
			for (Point p : points) {
				closestCenter = getClosestCenter(p, clusterList);
				currentCluster = getCurrentCluster(p, clusterList);
				if (currentCluster == -1) {
					if (clusterList.get(closestCenter).getCluster() != null) {
						clusterList.get(closestCenter).getCluster().add(p);
					}
					else {
						ArrayList<Point> newPoint = new ArrayList<Point>();
						newPoint.add(p);
						clusterList.get(closestCenter).setCluster(newPoint);
					}
				}
				else {
					if (currentCluster != closestCenter) {
						clusterList.get(currentCluster).getCluster().remove(p);
						clusterList.get(closestCenter).getCluster().add(p);
					}
				}
			}
			
			//System.out.println(clusterList.size());
			/*for (Cluster c : clusterList) {
				System.out.println(c.getCluster().size());
			}*/
			//saveOldCenter(clusterList, oldCenters);
			oldDistortion = getDistortion(clusterList);
			recalculateCenter(clusterList);
			distortion = getDistortion(clusterList);
			//oldCenters.clear();
			if (oldDistortion == distortion)
				break;
			
		}
		
		System.out.println("distortion: " + distortion);
		for (Cluster c : clusterList) {
			System.out.println(c.getCluster().size());
			System.out.println("center: " + c.getCenter().getX() + " " + c.getCenter().getY());
		}
		
		GraphPanel mainPanel = new GraphPanel(clusterList.get(0).getCluster(), clusterList.get(1).getCluster(), clusterList.get(2).getCluster());
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("KMeans");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	private static double getDistortion(ArrayList<Cluster> clusters) {
		double total = 0;
		for (Cluster c : clusters) {
			ArrayList<Point> pts = c.getCluster();
			Point center = c.getCenter();
			for (Point pt : pts) {
				total += Math.pow(euclideanDistance(pt, center), 2);
			}
		}
		return total;
	}
	
	private static void recalculateCenter(ArrayList<Cluster> clusterList) {
		double x = 0, y = 0;
		
		for(int i = 0; i < clusterList.size(); i++) {
			x = 0;
			y = 0;
			for (Point p : clusterList.get(i).getCluster()) {
				x += p.getX();
				y += p.getY();
			}
			Point newCenter = new Point(x / (double)clusterList.get(i).getCluster().size(), y / (double)clusterList.get(i).getCluster().size());
			clusterList.get(i).setCenter(newCenter);
		}
	}
	
	private static int getClosestCenter(Point p, ArrayList<Cluster> clusterList) {
		int center = 0;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < clusterList.size(); i++) {
			Point clusterCenter = clusterList.get(i).getCenter();
			double distance = euclideanDistance(p, clusterCenter);
			if (min > distance) {
				min = distance;
				center = i;
			}
		}
		return center;
	}
	
	private static int getCurrentCluster(Point p, ArrayList<Cluster> clusterList) {
		int current = -1;
		for (int i = 0; i < clusterList.size(); i++) {
			if(clusterList.get(i).getCluster() == null) {
				return current;
			}
			else {
				if (clusterList.get(i).getCluster().contains(p)) {
					return i;
				}
			}
		}
		return current;
	}
	
	private static double euclideanDistance(Point a, Point b) {
		return Math.sqrt(Math.pow((b.getY() - a.getY()), 2) + Math.pow((b.getX() - a.getX()), 2));
	}
	
	private static boolean hasElement(Point p, ArrayList<Cluster> cluster) {
		for (Cluster c : cluster) {
			if (c.getCenter().getX() == p.getX() && c.getCenter().getY() == p.getY()) {
				return true;
			}
		}
		return false;
	}
}
