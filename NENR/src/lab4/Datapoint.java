package lab4;

public class Datapoint {

	private double x, y, f;
	
	public Datapoint(double x, double y, double f) {
		this.x = x;
		this.y = y;
		this.f = f;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getF() {
		return f;
	}
	
	public String toString() {
		return "x: " + x + ", y: " + y + ", f. " + f;
	}
}
