package lab4;

public class GoodnessFunction {

	public double goodness(Datapoint datapoint, Instance instance) {
		if(!isCompatible(instance)) throw new IllegalArgumentException("Incompatible instance dimension!");
		else {
			double temp1 = Math.sin(instance.getGene(0) + instance.getGene(1) * datapoint.getX());
			double temp2 = instance.getGene(2) * Math.cos(datapoint.getX() * (instance.getGene(3) + datapoint.getY()));
			double temp3 = 1.0 / (1.0 + Math.exp(Math.pow((datapoint.getX() - instance.getGene(4)), 2)));
			return Math.pow((temp1 + temp2 * temp3) - datapoint.getF(), 2);
		}
	}

	public boolean isCompatible(Instance instance) {
		return instance.getDimension() == 5;
	}

}
