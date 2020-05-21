package lab2;

public class StandardFuzzySets {
	
	private StandardFuzzySets() {};
	
	public static IIntUnaryFunction lFunction(int alpha, int beta) {
		if(alpha >= beta) throw new IllegalArgumentException();
		return new IIntUnaryFunction() {
			
			@Override
			public double valueAt(int x) {
				if(x < alpha) return 1;
				else if(x >= alpha && x < beta) return ((double)(beta - x))/(beta - alpha);
				else return 0;
			}
		};
	}
	
	public static IIntUnaryFunction gammaFunction(int alpha, int beta) {
		if(alpha >= beta) throw new IllegalArgumentException();
		return new IIntUnaryFunction() {
			
			@Override
			public double valueAt(int x) {
				if(x < alpha) return 0;
				else if(x >= alpha && x < beta) return ((double)(x - alpha))/(beta - alpha);
				else return 1;
			}
		};
	}
	
	public static IIntUnaryFunction lambdaFunction(int alpha, int beta, int gamma) {
		if(!(alpha < beta && beta < gamma)) throw new IllegalArgumentException();
		return new IIntUnaryFunction() {
			
			@Override
			public double valueAt(int x) {
				if(x < alpha) return 0;
				else if(x >= alpha && x < beta) return ((double)(x - alpha))/(beta - alpha);
				else if(x >= beta && x < gamma) return ((double)(gamma - x))/(gamma - beta);
				else return 0;
			}
		};
	}
}
