package lab3;

import lab1.Domain;
import lab1.DomainElement;
import lab1.IDomain;
import lab2.IBinaryFunction;
import lab2.IFuzzySet;
import lab2.MutableFuzzySet;
import lab2.Operations;

public class SetOperations {
	
	private SetOperations() {};
	
	public static IFuzzySet cartesianProduct(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function) {
		return new IFuzzySet() {
			
			@Override
			public double getValueAt(DomainElement element) {
				int dim1 = set1.getDomain().getNumberOfComponents();
				int dim2 = set2.getDomain().getNumberOfComponents();
				int[] temp = element.getAllComponentValues();
				int[] subelement1 = new int[dim1];
				int[] subelement2 = new int[dim2];
				for(int i = 0; i < element.getNumberOfComponents(); i++) {
					if(i < dim1) subelement1[i] = temp[i];
					else subelement2[i - dim1] = temp[i];
				}
				return function.valueAt(set1.getValueAt(DomainElement.of(subelement1)), set2.getValueAt(DomainElement.of(subelement2)));
			}
			
			@Override
			public IDomain getDomain() {
				return Domain.combine(set1.getDomain(), set2.getDomain());
			}
		};
	}
	
	public static IFuzzySet union(IFuzzySet ... sets) {
		
		MutableFuzzySet result = new MutableFuzzySet(sets[0].getDomain());
		for(IFuzzySet set : sets) {
			for(DomainElement element : set.getDomain()) {
				result.set(element, Operations.zadehOr().valueAt(result.getValueAt(element), set.getValueAt(element)));
			}
		}
		return result;
	}
}
