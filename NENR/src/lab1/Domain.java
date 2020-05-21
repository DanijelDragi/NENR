package lab1;

public abstract class Domain implements IDomain {
	
	public static IDomain intRange(int start, int end) {
		return new SimpleDomain(start, end);
	}
	
	public static Domain combine(IDomain first, IDomain second) {
		int firstNumComponents = first.getNumberOfComponents();
		int secondNumComponents = second.getNumberOfComponents();
		SimpleDomain[] domains = new SimpleDomain[firstNumComponents + secondNumComponents];
		for(int i = 0; i < firstNumComponents; i++) {
			domains[i] = (SimpleDomain) first.getComponent(i);
		}
		for(int i = 0; i < secondNumComponents; i++) {
			domains[i + firstNumComponents] = (SimpleDomain) second.getComponent(i);
		}
		return new CompositeDomain(domains);
	}
}
