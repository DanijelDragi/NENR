package lab3;

import lab1.IDomain;
import lab1.DomainElement;
import lab2.IBinaryFunction;
import lab2.IFuzzySet;
import lab2.MutableFuzzySet;

public class Rule {
	
	private IFuzzySet relation;
	private IDomain outputDomain;
	
	public Rule(IFuzzySet[] DomainSets, IFuzzySet outputSet, IBinaryFunction implication) {
		outputDomain = outputSet.getDomain();
		
		IFuzzySet temp = DomainSets[0];
		for(int i = 1; i < DomainSets.length; i++) {
			temp = SetOperations.cartesianProduct(temp, DomainSets[i], implication);
		}
		relation = SetOperations.cartesianProduct(temp, outputSet, implication);
	}
	
	public IFuzzySet getRelation() {
		return relation;
	}
	
	public IFuzzySet conclusion(int[] inputs) {

		MutableFuzzySet conclusion = new MutableFuzzySet(outputDomain);
		
		int[] indexes = new int[inputs.length + 1];
		for(DomainElement element : outputDomain) {
			
			System.arraycopy(inputs, 0, indexes, 0, inputs.length);
			indexes[inputs.length] = element.getComponentValue(0);
			
			conclusion.set(element, relation.getValueAt(DomainElement.of(indexes)));	
		}
		
		return conclusion;
	}

}
