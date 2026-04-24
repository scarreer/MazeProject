package classes;
/*
 * Class to take the values from the raycasting class and render them onto
 * the screen.
 * 
 *
 * 
 * @Author Levi Fowler
 */


import java.awt.Color;
import java.lang.reflect.Field;

import javax.swing.JFrame;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class GuiMaze {
    private static int currentResolution = 320; 
    static Raycasting engine;

    public static void main(String[] args) {
        
        engine = new Raycasting(currentResolution);



        StdDraw.setCanvasSize(1280, 720);
        StdDraw.setXscale(0, currentResolution);
        StdDraw.setYscale(0, 720);
        StdDraw.enableDoubleBuffering();
        removeMenuBar();

        while (true) {
            engine.update();
            Render();
            StdDraw.pause(10);
        }
    }

    public static void Render() {
    	
    	//Draws Background
        StdDraw.clear(StdDraw.LIGHT_GRAY);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(0, 0, 1280, 360);
        
        float[] distances = engine.getDistances();
        boolean[] wallBrightness = engine.getBrightness();
        int[] wallColors = engine.getColorArray();

        for (int i = 0; i < currentResolution; i++) {
            float distance = distances[i];
            if (distance < 0.1f) {
            	distance = 0.1f;
            }
                       
            //Sets wall colors and brightness
            if(wallColors[i] == 1) {
	            if(wallBrightness[i]) {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f);                
	            	StdDraw.setPenColor(new Color(0, 0, (int)(255 * brightness)));
	            } else {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f); 
	                StdDraw.setPenColor(new Color(0, 0, (int)(230 * brightness)));
	            }
            } else if(wallColors[i] == 2) {
	            if(wallBrightness[i]) {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f);                
	            	StdDraw.setPenColor(new Color(0, (int)(255 * brightness), 0));
	            } else {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f); 
	                StdDraw.setPenColor(new Color(0, (int)(230 * brightness), 0));
	            }
            }  else if(wallColors[i] == 3) {
	            if(wallBrightness[i]) {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f);                
	            	StdDraw.setPenColor(new Color((int)(255 * brightness), 0, 0));
	            } else {
	                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f); 
	                StdDraw.setPenColor(new Color((int)(230 * brightness), 0, 0));
	            }
            } 


            double x = i + 0.5;
            double y = 360;
            double halfWidth = 0.5;
            double halfHeight = 400.0 / distance; 

            
            //Uses rectangles instead of lines because for some reason
            //it renders faster.
            StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
        }
        
        StdDraw.show();
    }
    
    private static void removeMenuBar() {
        try {
            Field frameField = StdDraw.class.getDeclaredField("frame");
            frameField.setAccessible(true);
            JFrame frame = (JFrame) frameField.get(null);
            
            frame.setJMenuBar(null);
            frame.revalidate();
        } catch (Exception e) {
            System.out.println("Could not remove menu bar: " + e.getMessage());
        }
    }
}