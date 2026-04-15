package classes;

import java.awt.Color;
import edu.princeton.cs.algs4.StdDraw;

public class GuiMaze {
    // 1. Set your desired resolution here
    private static int currentResolution = 640; 
    private static Raycasting engine = new Raycasting(currentResolution);

    public static void main(String[] args) {
        StdDraw.setCanvasSize(1280, 720);
        StdDraw.setXscale(0, currentResolution);
        StdDraw.setYscale(0, 720);
        StdDraw.enableDoubleBuffering();

        while (true) {
            engine.update(); 
            Render(); 
            StdDraw.pause(10);
        }
    }

    public static void Render() {
        StdDraw.clear(StdDraw.BLACK);
        float[] distances = engine.getDistances();

        for (int i = 0; i < currentResolution; i++) {
            float distance = distances[i];
            if (distance < 0.1f) distance = 0.1f;
            
            float brightness = 1.0f - Math.min(1.0f, distance / 10.0f); 
            int r = (int)(255 * brightness);
            StdDraw.setPenColor(new Color(r, 0, 0));

            double x = i + 0.5;
            double y = 360;
            double halfWidth = 0.5;
            double halfHeight = 400.0 / distance; 

            // Draw the solid block
            StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
        }
        
        StdDraw.show();
    }
}