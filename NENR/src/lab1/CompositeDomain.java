package lab1;
import java.util.Arrays;
import java.util.Iterator;

public class CompositeDomain extends Domain {
	
	private SimpleDomain[] domains;
	
	public CompositeDomain(SimpleDomain[] domains) {
		this.domains = domains;
	}

	@Override
	public int getCardinality() {
		int cardinality = 1;
		for (SimpleDomain domain : domains) {
			cardinality *= domain.getCardinality();
		}
		return cardinality;
	}

	@Override
	public IDomain getComponent(int component) {
		if(component < 0 || component > domains.length - 1) throw new IndexOutOfBoundsException();
		return domains[component];
	}

	@Override
	public int getNumberOfComponents() {
		return domains.length;
	}
	
	public SimpleDomain[] getDomains() {
		return domains;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		return new CompositeDomainIterator(domains);
	}

	@Override
	public int indexOfElement(DomainElement domainElement) {
		int index = 0, numComponents = domainElement.getNumberOfComponents();
		int multiplier = 1;
		for(int i = 0; i < numComponents; i++) {
			index += (domainElement.getComponentValue(i) - domains[i].getFirst()) * multiplier;
			multiplier *= domains[i].getCardinality();
		}
		return index;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		int temp = index;
		int[] values = new int[domains.length];
		for(int i = 0; i < domains.length; i++) {
			values[i] = temp % domains[i].getCardinality() + domains[i].getFirst();
			temp = temp / domains[i].getCardinality();
		}
		return new DomainElement(values);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CompositeDomain))
			return false;
		CompositeDomain other = (CompositeDomain) obj;
		if (!Arrays.equals(domains, other.getDomains()))
			return false;
		return true;
	}
}
