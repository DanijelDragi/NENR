package lab2;
import lab1.DomainElement;
import lab1.IDomain;

public class Operations {
	
	private Operations() {};
	
	public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function) {
		return new IFuzzySet() {
			
			@Override
			public double getValueAt(DomainElement element) {
				return function.valueAt(set.getValueAt(element));
			}
			
			@Override
			public IDomain getDomain() {
				return set.getDomain();
			}
		};
	}
	
	public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function) {
		return new IFuzzySet() {
			
			@Override
			public double getValueAt(DomainElement element) {
				return function.valueAt(set1.getValueAt(element), set2.getValueAt(element));
			}
			
			@Override
			public IDomain getDomain() {
				return set1.getDomain();
			}
		};
	}
	
	public static IUnaryFunction zadehNot() {
		return new IUnaryFunction() {
			
			@Override
			public double valueAt(double x) {
				return 1.0 - x;
			}
		};
	}
	
	public static IBinaryFunction zadehAnd() {
		return new IBinaryFunction() {
			
			@Override
			public double valueAt(double x, double y) {
				return x > y ? y : x;
			}
		};
	}
	
	public static IBinaryFunction zadehOr() {
		return new IBinaryFunction() {
			
			@Override
			public double valueAt(double x, double y) {
				return x > y ? x : y;
			}
		};
	}
	
	public static IBinaryFunction hamacherTNorm(double v) {
		return new IBinaryFunction() {
			
			@Override
			public double valueAt(double x, double y) {
				return (x * y)/(v + (1 - v) * (x + y - x * y));
			}
		};
	}
	
	public static IBinaryFunction hamacherSNorm(double v) {
		return new IBinaryFunction() {
			
			@Override
			public double valueAt(double x, double y) {
				return (x + y - ((2 - v) * x * y))/(1 - ((1 - v) * x * y));
			}
		};
	}
	
	public static IBinaryFunction algebraicProduct() {
		return new IBinaryFunction() {
			
			@Override
			public double valueAt(double x, double y) {
				return x * y;
			}
		};
	}
}
