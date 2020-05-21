package lab1;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompositeDomainIterator implements Iterator<DomainElement> {
	
	private SimpleDomain[] domains;
	private int[] cardinalities, current = null;
	private SimpleDomainIterator[] iterators;
	
	public CompositeDomainIterator(SimpleDomain[] domains) {
		this.domains = domains;
		cardinalities = new int[domains.length];
		iterators = new SimpleDomainIterator[domains.length];
		for(int i = 0; i < domains.length; i++) {
			cardinalities[i] = domains[i].getCardinality();
			iterators[i] = (SimpleDomainIterator) domains[i].iterator();
		}
	}

	@Override
	public boolean hasNext() {
		for(int i = 0; i < iterators.length; i++) {
			if(iterators[i].hasNext()) return true;
		}
		return false;
	}

	@Override
	public DomainElement next() {
		if(!this.hasNext()) throw new NoSuchElementException();
		else if(current == null) {
			current = new int[domains.length];
			for(int i = 0; i < domains.length; i++) current[i] = iterators[i].next().getComponentValue(0);
			return DomainElement.of(current);
		}
		else {
			for(int i = 0; i < iterators.length; i++) {
				if(iterators[i].hasNext()) {
					current[i] = iterators[i].next().getComponentValue(0);
					if(i > 0) {
						for(int j = 0; j < i; j++) {
							iterators[j].reset();
							current[j] = iterators[j].next().getComponentValue(0);
						}
					}
					return DomainElement.of(current);
				}
			}
		}
		//Should be unreachable code!
		return null;
	}

}
