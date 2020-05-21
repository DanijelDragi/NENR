package lab3;

public enum Speed {
	Backworads(5),
	Slow(20),
	Medium(35),
	Fast(50),
	VeryFast(150);
	
	public final int speed;
	
	private Speed(int speed) {
        this.speed = speed;
    }
}
