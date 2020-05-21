package lab4;

import java.util.Random;

public class InstanceOperations {

	private InstanceOperations() {};
	
	public static Instance crossover(Instance instance1, Instance instance2) {
		
		if(instance1.getDimension() != instance2.getDimension()) throw new IllegalArgumentException("got dimensions: " + instance1.getDimension() + " and " + instance2.getDimension());
		
		Random random = new Random();
		double[] genes = new double[instance1.getDimension()];
		for(int i = 0; i < instance1.getDimension(); i++) {
			if(random.nextDouble() > 0.5) genes[i] = instance1.getGene(i);
			else genes[i] = instance2.getGene(i);
		}
		Instance child = new Instance(instance1.getDimension(), genes);
		return child;
	}
	
	public static void mutate(Instance instance, double mutationChance, double min, double range) {
		Random random = new Random();
		if(random.nextDouble() <= mutationChance) {
			int index = random.nextInt(instance.getDimension());
			instance.setGene(index, min + random.nextDouble() * range);
		}
	}
}
