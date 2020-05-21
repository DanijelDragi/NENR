package lab1;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleDomainIterator implements Iterator<DomainElement> {

	private int first, last, current;
	
	public SimpleDomainIterator(int first, int last) {
		this.first = first;
		this.last = last;
		this.current = first;
	}
	
	@Override
	public boolean hasNext() {
		return current < last;
	}

	@Override
	public DomainElement next() {
		if(current >= last) throw new NoSuchElementException();
		int[] value = {current};
		current++;
		return new DomainElement(value);
	}
	
	public void reset() {
		current = first;
	}
}
