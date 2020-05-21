package lab3;

public enum Distance {
	Critical(40),
	Close(70),
	Medium(100),
	Far(200),
	VeryFar(1301);
	
	public final int distance;
	
	private Distance(int distance) {
        this.distance = distance;
    }
}
