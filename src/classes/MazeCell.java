package classes;

public class MazeCell {
	private int x;
	private int y;
	private boolean filled;
	
	public boolean isFilled() {
		return filled;
	}
	
	public void setFilled(boolean fill) {
		filled = fill;
		if(fill) {
			//Color black
		}else {
			//Color white
		}
	}
}
