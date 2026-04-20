package classes;

import edu.princeton.cs.algs4.StdRandom;

public class Maze {

	public static void main(String[] args) {
		

	}
	
	public static int[][] generateMaze() {
		int[][] maze = new int[11][11];

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                maze[i][j] = 1;
            }
        }
        
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {

                if (i % 2 == 1 && j % 2 == 1) {
                    maze[i][j] = 0;

                    boolean canGoNorth = (i > 1);
                    boolean canGoWest = (j > 1);

                    if (canGoNorth && canGoWest) {
                        if (StdRandom.bernoulli(0.5)) {
                            maze[i - 1][j] = 0;
                        } else {
                            maze[i][j - 1] = 0;
                        }
                    } else if (canGoNorth) {
                        maze[i - 1][j] = 0;
                    } else if (canGoWest) {
                        maze[i][j - 1] = 0;
                    }
                }
            }
        }
        
		maze[0][1] = 2;
		maze[10][9] = 3;
		
        return maze;
	}
	
	public static void solveMaze() {
		
	}

}
