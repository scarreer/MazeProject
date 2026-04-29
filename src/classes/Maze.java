package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

/**
* Class that handles maze generation and solving.
*
* @author Davis Martin && Levi Fowler
*/
public class Maze {

	
	//uses the binary tree maze algorithm to generate a random maze.
	private static final int[] TEXS = {1, 4, 5, 6};
    private static Random r = new Random();

    public static int[][] generateMaze() {
        int[][] maze = new int[11][11];
        for (int i=0; i<11; i++) for (int j=0; j<11; j++) maze[i][j] = TEXS[r.nextInt(4)];
        for (int i=1; i<10; i++) {
            for (int j=1; j<10; j++) {
                if (i%2==1 && j%2==1) {
                    maze[i][j] = 0;
                    if (i>1 && j>1) {
                        if (Math.random()<0.5) maze[i-1][j]=0; else maze[i][j-1]=0;
                    } else if (i>1) maze[i-1][j]=0; else if (j>1) maze[i][j-1]=0;
                }
            }
        }
        maze[0][1] = 2; maze[10][9] = 3;
        return maze;
	}

	/**
	* Finds a solution to the maze and returns an array indicating the solution.
	*/
	public static int[][] solveMaze() {
		int[][] array = mazeToArray(Gui.getMaze());
		int vertices = 0;
		LinearProbingHashST<String, Integer> indexMap = new LinearProbingHashST<>();
		//Writes graph.txt
		try (FileWriter writer = new FileWriter("src/classes/resources/graph.txt")) {
			//Helpers
			StringBuilder sb = new StringBuilder();
			
			List<int[]> reverseMap = new ArrayList<>();
			
			int edges = 0;
			
			//Maps out the vertices to prepare for graphing
			for(int i = 0; i < 11; i++) {
				for(int j = 0; j < 11; j++) {
					if(array[i][j] == 0) { // array[i][j] != 1 to include start and end points
						String key = i + "," + j;
						indexMap.put(key, vertices);
						reverseMap.add(new int[] {i, j});
						vertices++;
					}
				}
			}
			
			//Prepares to write vertices and edges into file
			for(int i = 0; i < 11; i++) {
				for(int j = 0; j < 11; j++) {
					if(array[i][j] == 0) {
						String key = i + "," + j;
						int vertex = indexMap.get(key);
						
						//Is there a path down?
						if(array[i+1][j] == 0) {
							edges++;
							int neighbor = indexMap.get((i+1) + "," + j);
							sb.append(vertex + " " + neighbor + "\n");
						}
						
						//Is there a path right?
						if(array[i][j+1] == 0) {
							edges++;
							int neighbor = indexMap.get(i + "," + (j+1));
							sb.append(vertex + " " + neighbor + "\n");
						}
					}
				}
			}
			
			
			writer.write(vertices + "\n");
			writer.write(edges + "\n");
			writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		//Creates graph
		Graph graph = new Graph(new In("src/classes/resources/graph.txt"));
		DepthFirstPaths dfs = new DepthFirstPaths(graph, 0);
		
		if(dfs.pathTo(vertices-1) == null) {
			Gui.isMazePossible(false);
			Gui.displayErrorMessage();
		}else {
			Gui.isMazePossible(true);

			for(int index : dfs.pathTo(vertices-1)) {
				for(int i = 0; i < 11; i++) {
					for(int j = 0; j < 11; j++) {
						String key = i + "," + j;
						if(array[i][j] == 0 && indexMap.get(key) == index) {
							array[i][j] = 9;
						}
					}
				}
			}
		}
		return array;
		
	}

	/**
	* Converts the maze into an integer array that can be interpreted by the 
	* raycasting's auto solve method
	*/
	private static int[][] mazeToArray(MazeCell[][] maze){
		int[][] array = new int[11][11];
		for(int i = 0; i < 11; i++) {
			for(int j = 0; j < 11; j++) {
				array[i][j] = (maze[i][j].isFilled() ? 1 : 0);
			}
		}
		array[0][1] = 2;
		array[10][9] = 3;
		
		return array;
	}
}
