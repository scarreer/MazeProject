package classes;

import edu.princeton.cs.algs4.StdDraw;

public class Raycasting {
    public boolean useDDA = true; 
    public int resolution;
    private float[] tempArray;

    public static final int[][] MAP = {
        {1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,1,0,0,0,1},
        {1,0,1,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,1,0,0,0,1},
        {1,0,0,0,0,0,0,0,0,1},
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
            
            if (useDDA) {
                tempArray[i] = (float) castDDA(rayAngle);
            } else {
                tempArray[i] = (float) castStepping(rayAngle);
            }
        }
    }

    private double castStepping(double rayAngle) {
        double dist = 0;
        double step = 0.02;
        while (dist < 15.0) {
            dist += step;
            int tx = (int)(posX + Math.cos(rayAngle) * dist);
            int ty = (int)(posY + Math.sin(rayAngle) * dist);
            if (tx < 0 || tx >= 10 || ty < 0 || ty >= 10 || MAP[ty][tx] == 1) {
                return dist * Math.cos(rayAngle - dirAngle);
            }
        }
        return 15.0;
    }

    private double castDDA(double rayAngle) {
        double rayDirX = Math.cos(rayAngle);
        double rayDirY = Math.sin(rayAngle);
        double deltaDistX = Math.abs(1 / rayDirX);
        double deltaDistY = Math.abs(1 / rayDirY);

        int mapX = (int) posX;
        int mapY = (int) posY;
        double sideDistX, sideDistY;
        int stepX, stepY;

        if (rayDirX < 0) {
            stepX = -1;
            sideDistX = (posX - mapX) * deltaDistX;
        } else {
            stepX = 1;
            sideDistX = (mapX + 1.0 - posX) * deltaDistX;
        }

        if (rayDirY < 0) {
            stepY = -1;
            sideDistY = (posY - mapY) * deltaDistY;
        } else {
            stepY = 1;
            sideDistY = (mapY + 1.0 - posY) * deltaDistY;
        }

        double dist = 0;
        boolean hit = false;
        while (!hit && dist < 15.0) {
            if (sideDistX < sideDistY) {
                dist = sideDistX;
                sideDistX += deltaDistX;
                mapX += stepX;
            } else {
                dist = sideDistY;
                sideDistY += deltaDistY;
                mapY += stepY;
            }
            if (mapX < 0 || mapX >= 10 || mapY < 0 || mapY >= 10 || MAP[mapY][mapX] == 1) hit = true;
        }
        return dist * Math.cos(rayAngle - dirAngle);
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
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_A)) dirAngle -= rotSpeed;
        if (StdDraw.isKeyPressed(java.awt.event.KeyEvent.VK_D)) dirAngle += rotSpeed;
    }

    public float[] getDistances() {
    	return tempArray; }
}