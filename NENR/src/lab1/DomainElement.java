package lab1;
import java.util.Arrays;

public class DomainElement {

	private int[] values;
	
	public DomainElement(int[] values) {
		this.values = values;
	}
	
	public int getNumberOfComponents() {
		return values.length;
	}
	
	public int getComponentValue(int component) {
		return values[component];
	}
	
	public int[] getAllComponentValues() {
		return values;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(values);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DomainElement))
			return false;
		DomainElement other = (DomainElement) obj;
		if (!Arrays.equals(values, other.values))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
	
	public static DomainElement of(int[] values) {
		return new DomainElement(values);
	}
}
