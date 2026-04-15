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
    private float[] tempArray;

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

    private static double posX = 2.5, posY = 2.5; 
    private static double dirAngle = 0.0;
    private final double FOV = Math.PI / 3;

    public Raycasting(int resolution) {
        this.resolution = resolution;
        this.tempArray = new float[resolution];
    }

    public void update() {
        handleInput();
        for (int i = 0; i < resolution; i++) {
            double rayAngle = (dirAngle - FOV / 2.0) + (i / (double)resolution) * FOV;            
            tempArray[i] = (float) castStepping(rayAngle);
        }
    }

    private double castStepping(double rayAngle) {
        double distance = 0;
        double step = 0.02;
        
        while (distance < 15.0) {
            distance += step;
            
            int tx = (int)(posX + Math.cos(rayAngle) * distance);
            int ty = (int)(posY + Math.sin(rayAngle) * distance);
            
            if (tx < 0 || tx >= 10 || ty < 0 || ty >= 10 || MAP[ty][tx] == 1) {
                return distance * Math.cos(rayAngle - dirAngle);
            }
        }
        return 15.0;
    }

    private void handleInput() {
        double moveSpeed = 0.08;
        double rotSpeed = 0.05;
        
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_W)) {
            posX += Math.cos(dirAngle) * moveSpeed;
            posY += Math.sin(dirAngle) * moveSpeed;
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_S)) {
            posX -= Math.cos(dirAngle) * moveSpeed;
            posY -= Math.sin(dirAngle) * moveSpeed;
        }
        
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_A)) {
        	dirAngle -= rotSpeed;
        }
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D)) {
        	dirAngle += rotSpeed;
        }
    }

    public float[] getDistances() {
    	return tempArray; 
    	}
}