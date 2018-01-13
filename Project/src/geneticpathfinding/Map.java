/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andrés Movilla
 */
public class Map {

    // REGULAR WALKABLE
    public static final int PATH_TILE = 0;
    public static final int PATH_DIRT = 1;
    public static final int PATH_MOSS_TILE = 2;
    public static final int PATH_GRASS = 3;
    
    // BLOCKED 
    public static final int PATH_WALL = 4;
    public static final int PATH_STONE = 5;
    
    // DEATH
    public static final int PATH_LAVA = 6;
    public static final int PATH_HOLE = 7;
    
    // SLOW
    public static final int PATH_WATER = 8;
    
    public static final Color[] tileColors = {
	GP.color(0f, 0f, 66.3f), GP.color(27.6f, 60.7f, 95.7f), GP.color(147.6f, 60.7f, 95.7f),
	GP.color(147.7f, 92.9f, 38.4f), GP.color(0f, 0f, 9.8f), GP.color(180f, 40.5f, 31f),
	GP.color(0f, 100f, 54.5f), GP.color(0f, 0f, 2f), GP.color(209.7f, 83.9f, 100f),
    };
    
    public static final String[] tileText = {
	"TILE", "DIRT", "MOSS", "GRASS", "WALL", "STONE", "LAVA", "HOLE", "WATER"
    };
    
    private final int[] start;
    private final int[] finish;
    
    private final float tileSize;
    
    private final int[][] tiles;
    
    public Map(int size) {
	tiles = new int[size][size];
	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		tiles[i][j] = GP.r.nextInt(9);
	    }
	}
	start = new int[] { 1, 1 };
	finish = new int[] { size-1, size-1 };
	tileSize = 700/size;
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(700,700,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	for (int y = 0; y < tiles.length; y++) {
	    for (int x = 0; x < tiles.length; x++) {
		g.setColor(tileColors[tiles[y][x]]);
		g.fillRect((int)(x*tileSize), (int)(y*tileSize), (int)tileSize, (int)tileSize);
		g.setColor(Color.white);
		g.drawString(tileText[tiles[y][x]], (int)(x*tileSize), (int)(y*tileSize));
	    }
	}
	
	return img;
    }
    
}
