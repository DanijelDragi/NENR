package lab6;

import java.util.Random;

public class RuleConclusionFunction {
	
	private double p, q, r;

	public RuleConclusionFunction() {
		Random random = new Random();
		p = random.nextDouble();
		q = random.nextDouble();
		r = random.nextDouble();
	}

	public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}

	public double getQ() {
		return q;
	}

	public void setQ(double q) {
		this.q = q;
	}

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}
	
	public double getF(double x, double y) {
		return (p * x) + (q * y) + r;
	}
}
