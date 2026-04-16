package classes;
/*
 * Calculate the 2.5d environment given a map, and passes values to
 * GuiMaze to be rendered. 
 * 
 * (a temporary map is being used for testing)
 * 
 * 
 * TODO: add collision detection, add auto move for auto solve
 * 
 * @Author Levi Fowler
 */


import edu.princeton.cs.algs4.StdDraw;

public class Raycasting {
    public int resolution;
    private float[] distanceArray;
    private boolean[] shadeArray;

    public static final int[][] MAP = {
        {1,1,1,1,1,1,1,1,1,1},
        {1,0,1,0,0,1,0,0,0,1},
        {1,0,1,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,0,1,0,1,0,0,0,1},
        {1,0,0,1,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1}
    };

    private static double posX = 1.5, posY = 1.5; 
    private static double dirAngle = 0.0;
    private final double FOV = Math.PI / 3;
    private static boolean wallLit = false;

    public Raycasting(int resolution) {
        this.resolution = resolution;
        this.distanceArray = new float[resolution];
        this.shadeArray = new boolean[resolution];
    }

    public void update() {
        handleInput();
        for (int i = 0; i < resolution; i++) {
            double rayAngle = (dirAngle - FOV / 2.0) + (i / (double)resolution) * FOV;            
            distanceArray[i] = (float) castStepping(rayAngle);
            shadeArray[i] = wallLit;
        }
    }

    private double castStepping(double rayAngle) {
        double distance = 0;
        double step = 0.02;
        
        double currX = posX;
        double currY = posY;
        

        while (distance < 15.0) {
            distance += step;

            double nextX = posX + Math.cos(rayAngle) * distance;
            double nextY = posY + Math.sin(rayAngle) * distance;

            if ((int)nextX != (int)currX) {
                if (MAP[(int)currY][(int)nextX] == 1) {
                    wallLit = true;
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currX = nextX;

            if ((int)nextY != (int)currY) {
                if (MAP[(int)nextY][(int)currX] == 1) {
                	wallLit = false;
                    return finalizeDistance(distance, rayAngle);
                }
            }
            currY = nextY;
        }
        return 15.0;
    }

    private double finalizeDistance(double dist, double rayAngle) {
        return dist * Math.cos(rayAngle - dirAngle);
    }

    private void handleInput() {
        double moveSpeed = 0.08;
        double rotSpeed = 0.05;
        
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_A) ||
        		StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_LEFT)) {
        	dirAngle -= rotSpeed;
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D) ||
        		StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_RIGHT)) {
        	dirAngle += rotSpeed;
        }
        
        double nextX = posX;
        double nextY = posY;

        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_W) || 
            StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_UP)) {
            nextX += Math.cos(dirAngle) * moveSpeed;
            nextY += Math.sin(dirAngle) * moveSpeed;
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_S) || 
            StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_DOWN)) {
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
}