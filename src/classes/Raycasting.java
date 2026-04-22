/*
 * @Author Levi Fowler
 */

package classes;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.event.KeyEvent;

public class Raycasting {
    public static int resolution;
    private static float[] distanceArray;
    private static boolean[] shadeArray;
    private static int[] colorArray;
    public static boolean autoPlay = false;

    public static int[][] map = {
        {1,2,1,1,1,1,1,1,1,1},
        {1,0,1,0,0,1,0,0,0,1},
        {1,0,1,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,0,1,0,1,0,0,0,1},
        {1,0,0,1,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,3,1}
    };
    
    private static double posX = 1.5, posY = 1.5; 
    private static double dirAngle = 0;
    private final static double FOV = 60.0; 

    public Raycasting(int resolution) {
        this.resolution = resolution;
        distanceArray = new float[resolution];
        colorArray = new int[resolution];
        shadeArray = new boolean[resolution];
        map = Maze.generateMaze();
    }

    public void update() {
        if(autoPlay) {
            moveRight();
            
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        } else {
            handleInput();

        }
        
        for (int i = 0; i < resolution; i++) {
            double rayAngle = (dirAngle - FOV / 2.0) + (i / (double)resolution) * FOV;
            distanceArray[i] = (float) castStepping(rayAngle, i);
        }


    }

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

            if ((int)nextX != (int)currX) {
                if (map[(int)currY][(int)nextX] > 0) {
                    shadeArray[col] = true;
                    colorArray[col] = map[(int)currY][(int)nextX];
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currX = nextX;

            if ((int)nextY != (int)currY) {
                if (map[(int)nextY][(int)currX] > 0) {
                    shadeArray[col] = false;
                    colorArray[col] = map[(int)nextY][(int)currX];
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
    
    public static void moveRight() {
        dirAngle = 0;
        int nextX = (int)(posX + 1.0);
        int nextY = (int)posY;
        if (isPassable(nextX, nextY)) {
            posX += 1.0;
        }
    }

    public static void moveDown() {
        dirAngle = 90;
        int nextX = (int)posX;
        int nextY = (int)(posY + 1.0);
        if (isPassable(nextX, nextY)) {
            posY += 1.0;
        }
    }

    public static void moveLeft() {
        dirAngle = 180;
        int nextX = (int)(posX - 1.0);
        int nextY = (int)posY;
        if (isPassable(nextX, nextY)) {
            posX -= 1.0;
        }
    }

    public static void moveUp() {
        dirAngle = 270;
        int nextX = (int)posX;
        int nextY = (int)(posY - 1.0);
        if (isPassable(nextX, nextY)) {
            posY -= 1.0;
        }
    }

    private static boolean isPassable(int x, int y) {
        if (y >= 0 && y < map.length && x >= 0 && x < map[0].length) {
            return map[y][x] == 0;
        }
        return false;
    }
    
    public static void AutoPlay(boolean state) {
    	autoPlay = state;
    }

    public float[] getDistances() { return distanceArray; }
    public boolean[] getBrightness() { return shadeArray; }
    public int[] getColorArray() { return colorArray; }
    public static void updateMap(int[][] newMap) { map = newMap; }
    public static int[][] getMap() { return map; }
}