/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

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
    
    private final int[] start;
    private final int[] finish;
    
    private final int[][] tiles;
    
    public Map(int size) {
	tiles = new int[size][size];
	for (int i = 0; i < size; i++) {
	    for (int j = 0; j < size; j++) {
		tiles[i][j] = PATH_DIRT;
	    }
	}
	start = new int[] { 1, 1 };
	finish = new int[] { size-1, size-1 };
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(700,700,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	return img;
    }
    
}
