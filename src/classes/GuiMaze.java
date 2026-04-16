package classes;
/*
 * Class to take the values from the raycasting class and render them onto
 * the screen.
 * 
 * TODO: add instructions, add more detailed rendering
 * 
 * @Author Levi Fowler
 */


import java.awt.Color;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class GuiMaze {
    private static int currentResolution = 640; 
    private static Raycasting engine = new Raycasting(currentResolution);
    private static int wallColor = StdRandom.uniformInt(1, 7);

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
        StdDraw.clear(StdDraw.LIGHT_GRAY);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledRectangle(0, 0, 1280, 360);
        
        float[] distances = engine.getDistances();
        boolean[] wallBrightness = engine.getBrightness();

        for (int i = 0; i < currentResolution; i++) {
            float distance = distances[i];
            if (distance < 0.1f) distance = 0.1f;
            
            if(wallBrightness[i]) {
                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f);                
                int color = (int)(255 * brightness);
                
                switch(wallColor) {
            		case 1: StdDraw.setPenColor(new Color(color, 0, 0));
            	break;
            		case 2: StdDraw.setPenColor(new Color(0, color, 0));
            	break;
            		case 3: StdDraw.setPenColor(new Color(0, 0, color));
            	break;
            		case 4: StdDraw.setPenColor(new Color(color, color, 0));
                break;
                	case 5: StdDraw.setPenColor(new Color(0, color, color));
                break;
                	case 6: StdDraw.setPenColor(new Color(color, 0, color));
                break;
            
            }
               
            } else {
                float brightness = 1.0f - Math.min(1.0f, distance / 10.0f); 
                int color = (int)(230 * brightness);
                
                switch(wallColor) {
                		case 1: StdDraw.setPenColor(new Color(color, 0, 0));
                	break;
                		case 2: StdDraw.setPenColor(new Color(0, color, 0));
                	break;
                		case 3: StdDraw.setPenColor(new Color(0, 0, color));
                	break;
                		case 4: StdDraw.setPenColor(new Color(color, color, 0));
                    break;
                    	case 5: StdDraw.setPenColor(new Color(0, color, color));
                    break;
                    	case 6: StdDraw.setPenColor(new Color(color, 0, color));
                    break;
                
                }
            }


            double x = i + 0.5;
            double y = 360;
            double halfWidth = 0.5;
            double halfHeight = 400.0 / distance; 

            StdDraw.filledRectangle(x, y, halfWidth, halfHeight);
        }
        
        StdDraw.show();
    }
}