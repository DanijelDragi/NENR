package lab5;

public class Point {
	
	private double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getXi() {
		return (int) Math.round(x);
	}
	
	public int getYi() {
		return (int) Math.round(y);
	}
	
	public String toString() {
		return "[" + x + "; " + y + "]";
	}
}
