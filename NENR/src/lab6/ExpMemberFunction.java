package lab6;

import java.util.Random;

public class ExpMemberFunction {
	
	private double b, a;
	
	public ExpMemberFunction() {
		Random random = new Random();
		b = random.nextDouble();
		a = random.nextDouble();
	}
	
	public double getB() {
		return b;
	}
	
	public double getA() {
		return a;
	}
	
	public void setB(double b) {
		this.b = b;
	}
	
	public void setA(double a) {
		this.a = a;
	}
	
	public double getF(double x) {
		return 1.0 / (1.0 + Math.exp(b * (x - a)));
	}
}
