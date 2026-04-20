
/*
 * @Author Levi Fowler
 */

package classes;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.event.KeyEvent;

public class Raycasting {
    public int resolution;
    private static float[] distanceArray;
    private static boolean[] shadeArray;
    private static int[] colorArray;

    public static int[][] MAP = {
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
    private static double dirAngle = 0.0;
    private final double FOV = Math.PI / 3;

    public Raycasting(int resolution) {
        this.resolution = resolution;
        this.distanceArray = new float[resolution];
        this.colorArray = new int[resolution];
        this.shadeArray = new boolean[resolution];
    }

    public void update() {
        handleInput();
        for (int i = 0; i < resolution; i++) {
            double rayAngle = (dirAngle - FOV / 2.0) + (i / (double)resolution) * FOV;
            distanceArray[i] = (float) castStepping(rayAngle, i);
        }
    }

    private double castStepping(double rayAngle, int col) {
        double distance = 0;
        double step = 0.01;
        double currX = posX;
        double currY = posY;

        while (distance < 15.0) {
            distance += step;

            double nextX = posX + Math.cos(rayAngle) * distance;
            double nextY = posY + Math.sin(rayAngle) * distance;

            if ((int)nextX != (int)currX) {
                if (MAP[(int)currY][(int)nextX] > 0) {
                    shadeArray[col] = true;
                    colorArray[col] = MAP[(int)currY][(int)nextX];
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currX = nextX;

            if ((int)nextY != (int)currY) {
                if (MAP[(int)nextY][(int)currX] > 0) {
                    shadeArray[col] = false;
                    colorArray[col] = MAP[(int)nextY][(int)currX];
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currY = nextY;
        }
        
        colorArray[col] = 0;
        return 15.0;
    }

    private double finalizeDistance(double dist, double rayAngle) {
        return dist * Math.cos(rayAngle - dirAngle);
    }

    private void handleInput() {
        double moveSpeed = 0.08;
        double rotSpeed = 0.05;
        
        if (StdDraw.isKeyPressed(KeyEvent.VK_A) || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            dirAngle -= rotSpeed;
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_D) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            dirAngle += rotSpeed;
        }
        
        double nextX = posX;
        double nextY = posY;

        if (StdDraw.isKeyPressed(KeyEvent.VK_W) || StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
            nextX += Math.cos(dirAngle) * moveSpeed;
            nextY += Math.sin(dirAngle) * moveSpeed;
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_S) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
            nextX -= Math.cos(dirAngle) * moveSpeed;
            nextY -= Math.sin(dirAngle) * moveSpeed;
        }

        int ix = (int)nextX;
        int iy = (int)nextY;
        
        if (iy >= 0 && iy < MAP.length && ix >= 0 && ix < MAP[0].length) {
            if (MAP[iy][ix] == 0) {
                posX = nextX;
                posY = nextY;
            }
        }
    }

    public float[] getDistances() { 
    	return distanceArray; 
    }
    
    public boolean[] getBrightness() { 
    	return shadeArray; 
    }
    
    public int[] getColorArray() { 
    	return colorArray; 
    }
    
    public static void updateMap(int[][] newMap) {
    	MAP = newMap;
    }
}