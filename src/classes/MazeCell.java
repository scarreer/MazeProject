package classes;

/**
* An individual maze cell in the maze
*
* @author Davis Martin
*/
public class MazeCell {
	private int x;
	private int y;
	private boolean filled;

	/**
	* Returns whether the cell is filled.
	*/
	public boolean isFilled() {
		return filled;
	}

	/**
	* Sets the cell to be filled or not.
	*/
	public void setFilled(boolean fill) {
		filled = fill;
		if(fill) {
			//Color black
		}else {
			//Color white
		}
	}
}
