package lab2;
import lab1.DomainElement;
import lab1.IDomain;

public class MutableFuzzySet implements IFuzzySet {
	
	private double[] membership;
	private IDomain domain;
	
	public MutableFuzzySet(IDomain domain) {
		this.domain = domain;
		membership = new double[domain.getCardinality()];
		for(int i = 0; i < domain.getCardinality(); i++) {
			membership[i] = 0;
		}
	}

	public MutableFuzzySet set(DomainElement element, double value) {
		membership[domain.indexOfElement(element)] = value;
		return this;
	}
	
	@Override
	public IDomain getDomain() {
		return domain;
	}

	@Override
	public double getValueAt(DomainElement element) {
		return membership[domain.indexOfElement(element)];
	}
}
