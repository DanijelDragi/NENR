package lab2;
import lab1.DomainElement;
import lab1.IDomain;

public interface IFuzzySet {

	public IDomain getDomain();
	
	public double getValueAt(DomainElement element);
}
