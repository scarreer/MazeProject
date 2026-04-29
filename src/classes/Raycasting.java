package classes;

import java.awt.event.KeyEvent;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class Raycasting {
    private int resolution;
    private static int[][] map = {
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

    private Queue<Float> distanceQueue = new Queue<>();
    private Queue<Double> wallXQueue = new Queue<>();
    private static boolean[] shadeArray;
    private static int[] colorArray;

    public static boolean autoPlay = false;
    private static double posX = 1.5, posY = 1.5; 
    private static double dirX = 1.0, dirY = 0.0; 
    private static double planeX = 0.0, planeY = 0.66; 

    public Raycasting(int resolution) {
        this.resolution = resolution;
        shadeArray = new boolean[resolution];
        colorArray = new int[resolution];
    }

    public static void updateMap(int[][] newMap) {
        map = newMap;
    }

    public void update() {
        if (autoPlay) smoothAutoSolve();
        else handleInput();

        while(!distanceQueue.isEmpty()) distanceQueue.dequeue();
        while(!wallXQueue.isEmpty()) wallXQueue.dequeue();

        for (int i = 0; i < resolution; i++) {
            double cameraX = 2 * i / (double) resolution - 1; 
            double rayDirX = dirX + planeX * cameraX;
            double rayDirY = dirY + planeY * cameraX;
            castDDA(rayDirX, rayDirY, i);
        }
    }

    private void castDDA(double rDX, double rDY, int col) {
        int mX = (int) posX, mY = (int) posY;
        double dDX = Math.abs(1 / rDX), dDY = Math.abs(1 / rDY);
        int sX = (rDX < 0) ? -1 : 1, sY = (rDY < 0) ? -1 : 1;
        double sDX = (rDX < 0) ? (posX - mX) * dDX : (mX + 1.0 - posX) * dDX;
        double sDY = (rDY < 0) ? (posY - mY) * dDY : (mY + 1.0 - posY) * dDY;

        int side = 0;
        while (mX >= 0 && mX < 11 && mY >= 0 && mY < 11) {
            if (sDX < sDY) { sDX += dDX; mX += sX; side = 0; }
            else { sDY += dDY; mY += sY; side = 1; }
            if (mX < 0 || mX >= 11 || mY < 0 || mY >= 11) break;
            if (map[mY][mX] > 0 && map[mY][mX] != 9) {
                double pWD = (side == 0) ? (sDX - dDX) : (sDY - dDY);
                double wX = (side == 0) ? posY + pWD * rDY : posX + pWD * rDX;
                wX -= Math.floor(wX);
                colorArray[col] = map[mY][mX];
                shadeArray[col] = (side == 1);
                distanceQueue.enqueue((float) pWD);
                wallXQueue.enqueue(wX);
                return;
            }
        }
        distanceQueue.enqueue(20.0f);
        wallXQueue.enqueue(0.0);
        colorArray[col] = 0;
    }

    private void handleInput() {
        double mS = 0.08, rS = 0.05;
        if (StdDraw.isKeyPressed(KeyEvent.VK_A) || StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) rotate(-rS);
        if (StdDraw.isKeyPressed(KeyEvent.VK_D) || StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) rotate(rS);
        double nX = posX, nY = posY;
        if (StdDraw.isKeyPressed(KeyEvent.VK_W) || StdDraw.isKeyPressed(KeyEvent.VK_UP)) { nX += dirX * mS; nY += dirY * mS; }
        if (StdDraw.isKeyPressed(KeyEvent.VK_S) || StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) { nX -= dirX * mS; nY -= dirY * mS; }
        if ((int)nX >= 0 && (int)nX < 11 && (int)nY >= 0 && (int)nY < 11 && map[(int)nY][(int)nX] != 1) {
            posX = nX; posY = nY;
        }
    }

    private void rotate(double r) {
        double oDX = dirX;
        dirX = dirX * Math.cos(r) - dirY * Math.sin(r);
        dirY = oDX * Math.sin(r) + dirY * Math.cos(r);
        double oPX = planeX;
        planeX = planeX * Math.cos(r) - planeY * Math.sin(r);
        planeY = oPX * Math.sin(r) + planeY * Math.cos(r);
    }

    private void smoothAutoSolve() {
        int cX = (int) posX, cY = (int) posY;
        int[][] dirs = {{1,0},{0,1},{-1,0},{0,-1}};
        for (int[] d : dirs) {
            if (cY+d[1]>=0 && cY+d[1]<11 && cX+d[0]>=0 && cX+d[0]<11 && map[cY+d[1]][cX+d[0]] == 9) {
                posX += ((cX + d[0] + 0.5) - posX) * 0.1;
                posY += ((cY + d[1] + 0.5) - posY) * 0.1;
                if (Math.abs(posX - (cX+d[0]+0.5)) < 0.1 && Math.abs(posY - (cY+d[1]+0.5)) < 0.1) map[cY+d[1]][cX+d[0]] = 0;
                return;
            }
        }
    }

    public Queue<Float> getDistanceQueue() { return distanceQueue; }
    public Queue<Double> getWallXQueue() { return wallXQueue; }
    public boolean[] getBrightness() { return shadeArray; }
    public int[] getColorArray() { return colorArray; }
    public static void AutoPlay(boolean state) { autoPlay = state; }
}