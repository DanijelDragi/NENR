package lab1;
import java.util.Iterator;

public class SimpleDomain extends Domain {
	
	private int first, last;
	
	public SimpleDomain(int first, int last) {
		if(last <= first) throw new IllegalArgumentException();
		this.first = first;
		this.last = last;
	}

	@Override
	public int getCardinality() {
		return last - first;
	}

	@Override
	public IDomain getComponent(int component) {
		if (component == 0) return this;
		else throw new IndexOutOfBoundsException();
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}

	@Override
	public int indexOfElement(DomainElement element) {
		int value =  element.getComponentValue(0);
		if(value >= last || value < first || element.getNumberOfComponents() != 1) throw new IllegalArgumentException();
		return element.getComponentValue(0) - first;
	}
	
	@Override
	public DomainElement elementForIndex(int index) {
		if(index > this.getCardinality() - 1 || index < 0) throw new IndexOutOfBoundsException();
		int[] value = {index + first};
		return new DomainElement(value);
	}
	
	@Override
	public Iterator<DomainElement> iterator() {
		return new SimpleDomainIterator(first, last);
	}
	
	public int getFirst() {
		return first;
	}
	
	public int getLast() {
		return last;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SimpleDomain))
			return false;
		SimpleDomain other = (SimpleDomain) obj;
		if (!(this.first == other.getFirst() && this.last == other.getLast()))
			return false;
		return true;
	}
}
