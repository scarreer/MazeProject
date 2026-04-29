package classes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class GuiMaze {
    private static int currentResolution = 640; 
    static Raycasting engine;
    
    private static final String RESOURCE_DIR = "src/classes/Resources/";
    private static String[] TEX_PATHS = {
        "", RESOURCE_DIR + "wall1.png", "", "", RESOURCE_DIR + "wall4.png", 
        RESOURCE_DIR + "wall5.png", RESOURCE_DIR + "wall6.png"
    };
    private static String[] TEX_URLS = {
        "", "https://bghq.com/textures/doom/079.png", "", "", 
        "https://bghq.com/textures/doom/190.png", "https://bghq.com/textures/doom/442.png", 
        "https://bghq.com/textures/doom/544.png"
    };

    private static int[][] texData = new int[7][]; 
    private static int[] texW = new int[7], texH = new int[7];

    public static void main(String[] args) {
        for (int i : new int[]{1, 4, 5, 6}) cache(TEX_URLS[i], TEX_PATHS[i], i);
        
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

    private static void cache(String urlStr, String path, int id) {
        try {
            if (!Files.exists(Paths.get(path))) {
                Files.createDirectories(Paths.get(RESOURCE_DIR));
                try (BufferedInputStream in = new BufferedInputStream(new URL(urlStr).openStream());
                     FileOutputStream out = new FileOutputStream(path)) {
                    byte[] buf = new byte[1024]; int r;
                    while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
                }
            }
            BufferedImage img = ImageIO.read(new File(path));
            texW[id] = img.getWidth(); texH[id] = img.getHeight();
            texData[id] = img.getRGB(0, 0, texW[id], texH[id], null, 0, texW[id]);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void Render() {
        // Floor and Ceiling (Solid colors for optimization)
        StdDraw.clear(new Color(15, 15, 15)); 
        StdDraw.setPenColor(new Color(35, 35, 35)); 
        StdDraw.filledRectangle(currentResolution / 2.0, 180, currentResolution / 2.0, 180);
        
        Queue<Float> distances = engine.getDistanceQueue();
        Queue<Double> wallXCoords = engine.getWallXQueue();
        boolean[] wallBrightness = engine.getBrightness();
        int[] wallColors = engine.getColorArray();

        for (int i = 0; i < currentResolution; i++) {
            float dist = distances.dequeue();
            double wallX = wallXCoords.dequeue();
            int type = wallColors[i];
            double hH = 360.0 / Math.max(dist, 0.1);
            
            if (type > 0 && type != 2 && type != 3 && texData[type] != null) {
                int txX = (int)(wallX * texW[type]) % texW[type];
                int segments = 64; 
                double sH = (hH * 2) / segments;
                for (int s = 0; s < segments; s++) {
                    int txY = (s * texH[type] / segments) % texH[type];
                    Color c = new Color(texData[type][txY * texW[type] + txX]);
                    if (wallBrightness[i]) c = c.darker();
                    float shadow = (float) Math.max(0.2, 1.0 - (dist / 12.0));
                    c = new Color((int)(c.getRed()*shadow), (int)(c.getGreen()*shadow), (int)(c.getBlue()*shadow));
                    StdDraw.setPenColor(c);
                    StdDraw.filledRectangle(i + 0.5, (360 + hH) - (s * sH) - (sH/2), 0.5, sH / 2);
                }
            } else if (type == 2 || type == 3) {
                StdDraw.setPenColor(type == 2 ? Color.GREEN : Color.RED);
                StdDraw.filledRectangle(i + 0.5, 360, 0.5, hH);
            }
        }
        StdDraw.show();
    }

    private static void removeMenuBar() {
        try {
            Field f = StdDraw.class.getDeclaredField("frame");
            f.setAccessible(true);
            JFrame frame = (JFrame) f.get(null);
            frame.setJMenuBar(null);
            frame.revalidate();
        } catch (Exception e) {}
    }
}