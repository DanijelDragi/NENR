package lab4;

import java.util.Arrays;

public class Instance {

	private int size;
	private double[] genes;
	private double goodness;
	
	public Instance(int size, double...genes) {
		this.size = size;
		this.genes = new double[size];
		for(int i = 0; i < size; i++) {
			this.genes[i] = genes[i];
		}
	}
	
	public void setGoodness(double goodness) {
		this.goodness = goodness;
	}
	
	public double getGoodness() {
		return goodness;
	}

	public double getGene(int index) {
		return genes[index];
	}
	
	public void setGene(int index, double value) {
		genes[index] = value;
	}

	public int getDimension() {
		return size;
	}
	
	public String toString() {
		return Arrays.toString(genes);
	}
}
