package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class Maze {

	public static void main(String[] args) {
		String s = "010";
		int i = Integer.parseInt(s);
		System.out.println(s);
		System.out.println(i);
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
		int[][] array = mazeToArray(Gui.getMaze());
		int vertices = 0;
		//Writes graph.txt
		try (FileWriter writer = new FileWriter("src/classes/resources/graph.txt")) {
			//Helpers
			StringBuilder sb = new StringBuilder();
			Map<String, Integer> indexMap = new HashMap<>();
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
			Gui.setPossibility(false);
			Gui.displayErrorMessage();
		}else {
			System.out.println(dfs.pathTo(vertices-1));
			Gui.setPossibility(true);
		}
		
	}

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
	
	private static void resetMaze() {
		
	}
}
