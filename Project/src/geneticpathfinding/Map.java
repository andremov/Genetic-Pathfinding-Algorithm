/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Andrés Movilla
 */
public class Map {

    // REGULAR WALKABLE
    public static final int TILE_DIRT = 0;
    public static final int TILE_PATH = 1;
    public static final int TILE_MOSS = 2;
    public static final int TILE_GRASS = 3;
    
    // BLOCKED 
    public static final int TILE_WALL = 4;
    public static final int TILE_ROCKS = 5;
    
    // DEATH
    public static final int TILE_LAVA = 6;
    public static final int TILE_HOLE = 7;
    
    // SLOW
    public static final int TILE_WATER = 8;
    
    
    public static final int PERCENT_WALL = 40;
    public static final int PERCENT_GRASS = 50;
    public static final int PERCENT_LAVA = 35;
    public static final int PERCENT_WATER = 40;
    public static final int PERCENT_HOLE = 35;
    
    private static BufferedImage[] tileImages;
    
    public static final Color[] tileColors = {
	GP.color(27.6f, 60.7f, 95.7f), GP.color(0f, 0f, 66.3f), GP.color(147.6f, 60.7f, 95.7f),
	GP.color(147.7f, 92.9f, 38.4f), GP.color(0f, 0f, 25.8f), GP.color(180f, 40.5f, 31f),
	GP.color(0f, 100f, 54.5f), GP.color(0f, 0f, 2f), GP.color(209.7f, 83.9f, 100f),
    };
    
    public static final String[] tileText = {
	"dirt", "tile", "moss", "grass", "wall", "rocks", "lava", "hole", "water"
    };
    
    private final int[] start;
    private final int[] finish;
    
    private final float tileSize;
    
    private final int size;
    
    private final int[][] tiles;
    
    public Map(int size) {
	this.size = size;
	start = new int[] { 8, 8 };
	finish = new int[] { size-10, size-10 };
	tiles = generateMap();
	tileSize = 800/size;
	try {
	    tileImages = new BufferedImage[tileText.length];
	    for (int i = 0; i < tileText.length; i++) {
		tileImages[i] = ImageIO.read(new File("resources/" + tileText[i] + ".png"));
	    }
	} catch (Exception e) { }
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(800,800,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	for (int y = 0; y < tiles.length; y++) {
	    for (int x = 0; x < tiles.length; x++) {
		int tileID = tiles[y][x];
//		g.setColor(tileColors[tileID]);
//		ImageIO.read(new File("resouces/" + tileText[tileID] + ".png"));
//		g.fillRect((int)(x*tileSize), (int)(y*tileSize), (int)tileSize, (int)tileSize);
		try {
		    g.drawImage(tileImages[tileID], (int)(x*tileSize), (int)(y*tileSize), (int) tileSize, (int) tileSize, null);
		} catch (Exception e) {
		    e.printStackTrace();
		}
//		g.setColor(Color.white);
//		g.drawString(tileText[tileID], (int)(x*tileSize), (int)(y*tileSize)+10);
	    }
	}
	
	return img;
    }
    
    public int[][] generateMap() {
	int[][] finalMap = new int[size][size];
	
	int[][] layer = generateWalls();
	
	for (int i = 0; i < 3; i++) {
	    layer = smoothMap(layer, 4);
	}
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		finalMap[y][x] = layer[y][x]*TILE_WALL;
	    }
	}
	
	finalMap[start[1]][start[0]] = TILE_PATH;
	finalMap[finish[1]][finish[0]] = TILE_PATH;
	
	int[] tracePath = start;
	while (tracePath[0] != finish[0] || tracePath[1] != finish[1]) {
	    if (tracePath[0] != finish[0]) {
		if (finalMap[tracePath[1]][tracePath[0]+1] == 0) {
		    tracePath[0]++;
		} else {
		    tracePath[1]++;
		}
		finalMap[tracePath[1]][tracePath[0]] = TILE_PATH;
	    } else if (tracePath[1] != finish[1]) {
		tracePath[1]++;
		finalMap[tracePath[1]][tracePath[0]] = TILE_PATH;
	    }
	}
	
	layer = generateLayer(PERCENT_WATER);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_WATER;
		}
	    }
	}
	
	layer = generateLayer(PERCENT_LAVA);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_LAVA;
		} else if (finalMap[y][x] == TILE_WATER && layer[y][x] == 1) {
		    finalMap[y][x] = TILE_ROCKS;
		}
	    }
	}
	
	layer = generateLayer(PERCENT_GRASS);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_MOSS;
		}
	    }
	}
	
	layer = smoothMap(layer, 6);
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		if (finalMap[y][x] == TILE_MOSS && layer[y][x] == 1) {
		    finalMap[y][x] = TILE_GRASS;
		}
	    }
	}
	
	layer = generateLayer(PERCENT_HOLE);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_HOLE;
		}
	    }
	}
	
	return finalMap;
    }
    
    public int[][] generateLayer(int percent) {
	int[][] walls = new int[size][size];
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		walls[y][x] = GP.r.nextInt(100) < percent? 1 : 0;
	    }
	}
	
	return walls;
    }
    
    public int[][] generateWalls() {
	int[][] walls = new int[size][size];
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {    
		if (x == 0 || x == size-1 || y == 0 || y == size-1 ) {
		    walls[y][x] = 1;
		} else {
		    walls[y][x] = GP.r.nextInt(100) < PERCENT_WALL? 1 : 0;
		}
	    }
	}
	
	return walls;
    }
    
    public int[][] smoothMap(int[][] map, int threshold) {
	int[][] walls = map;
	
	for (int y = 0; y < size; y++) {
	    for (int x = 0; x < size; x++) {
		int count = surroundingTiles(walls, x, y);
		if (count > threshold) {
		    walls[y][x] = 1;
		} else if (count < threshold) {
		    walls[y][x] = 0;
		}
	    }
	}
	
	return walls;
    }
    
    public int surroundingTiles(int[][] map, int searchX, int searchY) {
	int count = 0;
	
	for (int y = searchY-1; y <= searchY+1; y++) {
	    for (int x = searchX-1; x <= searchX+1; x++) {
		if (x >= 0 && x <= size-1 && y >= 0 && y <= size-1) {
		    if (x != searchX || y != searchY) {
			count += map[y][x];
		    }
		} else {
		    count++;
		}
	    }
	}
	
	return count;
    }
}
