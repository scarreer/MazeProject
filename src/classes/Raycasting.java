/*
 * @Author Levi Fowler
 * Uses Raycasting to Calculate the distance from the player to the walls.
 * Implements an auto solve feature that moves 
 */

package classes;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.event.KeyEvent;

public class Raycasting {
    public static int resolution;
    Queue<Float> distanceQueue = new Queue<>();
    private static boolean[] shadeArray;
    private static int[] colorArray;
    public static boolean autoPlay = false;

    public static int[][] map = {
	        {1,2,1,1,1,1,1,1,1,1,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,0,0,0,0,0,0,0,0,0,1},
	        {1,1,1,1,1,1,1,1,1,3,1}
	    };
    
    private static double posX = 1.5, posY = 1.5; 
    private static double dirAngle = 0;
    private final static double FOV = 60.0; 

    public Raycasting(int resolution) {
        this.resolution = resolution;
        colorArray = new int[resolution];
        shadeArray = new boolean[resolution];
    }

    public void update() {
        if (autoPlay) {
            autoSolveStep();
            
            try {         
                Thread.sleep(500);             
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            handleInput();
        }
        
        for (int i = 0; i < resolution; i++) {
            double rayAngle = (dirAngle - FOV / 2.0) + (i / (double)resolution) * FOV;
            distanceQueue.enqueue((float) castStepping(rayAngle, i));
        }
    }

    
    //Raycasting that does NOT use the DDA algorithm
    private static double castStepping(double rayAngle, int col) {
        double distance = 0;
        double step = 0.02;
        double currX = posX;
        double currY = posY;
        
        double rayRad = Math.toRadians(rayAngle);

        while (distance < 15.0) {
            distance += step;

            double nextX = posX + Math.cos(rayRad) * distance;
            double nextY = posY + Math.sin(rayRad) * distance;

            
            //Checks the map grid, for walls, Excluding tile number 9
            //Tile 9 is reserved as a path for the Autoplay to follow.
            if ((int)nextX != (int)currX) {
                int tile = map[(int)currY][(int)nextX];
                if (tile > 0 && tile != 9) { 
                    shadeArray[col] = true;
                    colorArray[col] = tile;
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currX = nextX;

            if ((int)nextY != (int)currY) {
                int tile = map[(int)nextY][(int)currX];
                if (tile > 0 && tile != 9) { 
                    shadeArray[col] = false;
                    colorArray[col] = tile;
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currY = nextY;
        }
        
        colorArray[col] = 0;
        return 15.0;
    }

    private static double finalizeDistance(double dist, double rayAngle) {
        return dist * Math.cos(Math.toRadians(rayAngle - dirAngle));
    }
    
    private void normalizeAngle() {
        dirAngle = (dirAngle % 360 + 360) % 360;
    }

    private void handleInput() {
        double moveSpeed = 0.08;
        double rotSpeed = 3.0;
        
        if (StdDraw.isKeyPressed(KeyEvent.VK_A) || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            dirAngle -= rotSpeed;
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_D) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            dirAngle += rotSpeed;
        }

        normalizeAngle();
        
        double nextX = posX;
        double nextY = posY;
        double dirRad = Math.toRadians(dirAngle);

        if (StdDraw.isKeyPressed(KeyEvent.VK_W) || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
            nextX += Math.cos(dirRad) * moveSpeed;
            nextY += Math.sin(dirRad) * moveSpeed;
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_S) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
            nextX -= Math.cos(dirRad) * moveSpeed;
            nextY -= Math.sin(dirRad) * moveSpeed;
        }

        int ix = (int)nextX;
        int iy = (int)nextY;
        
        if (iy >= 0 && iy < map.length && ix >= 0 && ix < map[0].length) {
            if (map[iy][ix] == 0) {
                posX = nextX;
                posY = nextY;
            }
        }
    }
    
    

    
    public void autoSolveStep() {
        int curX = (int) posX;
        int curY = (int) posY;

        int[][] directions = {
            {1, 0, 0},
            {0, 1, 90},
            {-1, 0, 180},
            {0, -1, 270} 
        };

        
        //Checks for a 9 in the array, and moves there
        for (int[] dir : directions) {
            int nextX = curX + dir[0];
            int nextY = curY + dir[1];
            
            if (isValid(nextX, nextY) && map[nextY][nextX] == 9) {
                posX = nextX + 0.5;
                posY = nextY + 0.5;
                dirAngle = dir[2];
                
                map[nextY][nextX] = 0; 
                return;
            }
        }
    }

    //Checks for a valid position when auto solving
    private boolean isValid(int x, int y) {
        return (y >= 0 && y < 11 && x >= 0 && x < 11);
    }
    
    
    
    public static void AutoPlay(boolean state) {
    	autoPlay = state;
    }

    public Queue<Float> getDistanceQueue() { 
    	return distanceQueue; 
    }
    
    public boolean[] getBrightness() { 
    	return shadeArray; 
    }
    
    public int[] getColorArray() { 
    	return colorArray; 
    }
    
    public static void updateMap(int[][] newMap) {
    	map = newMap; 
    }
    public static int[][] getMap() {
    	return map; 
    }
}