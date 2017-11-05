import java.util.ArrayList;


public class Cluster {
	private ArrayList<Point> cluster;
	private Point center;
	
	public Cluster(ArrayList<Point> c1, Point p) {
		this.cluster = c1;
		this.center = p;
	}
	
	public ArrayList<Point> getCluster() {
		return cluster;
	}
	
	public void setCluster(ArrayList<Point> c) {
		cluster = c;
	}
	public Point getCenter() {
		return center;
	}
	public void setCenter(Point p) {
		center = p;
	}
}
