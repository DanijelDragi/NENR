package lab1;
import lab2.IFuzzySet;

public class Main {

	public static void main(String[] args) {
		/*
		IDomain d1 = Domain.intRange(0, 5);  // {0,1,2,3,4}
		printDomain(d1, "Elementi domene d1:");
		IDomain d2 = Domain.intRange(0, 3);  // {0,1,2}
		printDomain(d2, "Elementi domene d2:");
		IDomain d3 = Domain.combine(d1, d2);
		printDomain(d3, "Elementi domene d3:");
		System.out.println(d3.elementForIndex(0));
		System.out.println(d3.elementForIndex(5));
		System.out.println(d3.elementForIndex(14));
		System.out.println(d3.indexOfElement(DomainElement.of(new int[]{4,1})));
		*/
		/*
		IDomain d = Domain.intRange(0, 11); // {0,1,...,10}
		IFuzzySet set1 = new MutableFuzzySet(d)
				.set(DomainElement.of(new int[] {0}), 1.0)
				.set(DomainElement.of(new int[] {1}), 0.8)
				.set(DomainElement.of(new int[] {2}), 0.6)
				.set(DomainElement.of(new int[] {3}), 0.4)
				.set(DomainElement.of(new int[] {4}), 0.2);
		printSet(set1, "Set1:");
		
		IDomain d2 = Domain.intRange(-5, 6); // {-5,-4,...,4,5}
		IFuzzySet set2 = new CalculatedFuzzySet(d2, 
				StandardFuzzySets.lambdaFunction(d2.indexOfElement(DomainElement.of(new int[] {-4})),
												 d2.indexOfElement(DomainElement.of(new int[] { 0})), 
												 d2.indexOfElement(DomainElement.of(new int[] { 4}))));
		printSet(set2, "Set2:");
		*/
		/*
		IDomain d = Domain.intRange(0, 11);
		IFuzzySet set1 = new MutableFuzzySet(d).set(DomainElement.of(new int[] {0}), 1.0)
											   .set(DomainElement.of(new int[] {1}), 0.8)
											   .set(DomainElement.of(new int[] {2}), 0.6)
											   .set(DomainElement.of(new int[] {3}), 0.4)
											   .set(DomainElement.of(new int[] {4}), 0.2);
		printSet(set1, "Set1:");
		
		IFuzzySet notSet1 = Operations.unaryOperation(set1, Operations.zadehNot());
		printSet(notSet1, "notSet1:");
		
		IFuzzySet union = Operations.binaryOperation(set1, notSet1, Operations.zadehOr());
		printSet(union, "Set1 union notSet1:");
		
		IFuzzySet hinters = Operations.binaryOperation(set1, notSet1, Operations.hamacherTNorm(1.0));
		printSet(hinters, "Set1 intersection with notSet1 using parameterised Hamacher T norm with parameter 1.0:");
		*/
	}
	
	public static void printDomain(IDomain domain, String headingText) {
		if(headingText!=null) {
			System.out.println(headingText);
		}
		for(DomainElement e : domain) {
			System.out.println("Element domene: " + e);
		}
		System.out.println("Kardinalitet domene je: " + domain.getCardinality());
		System.out.println();
	}
	
	public static void printSet(IFuzzySet set, String headingText) {
		if(headingText!=null) {
			System.out.println(headingText);
		}
		for(DomainElement e : set.getDomain()) {
			System.out.println("d" + e + ": " + set.getValueAt(e));
		}
	}
}
