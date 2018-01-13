/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Color;
import java.awt.Graphics;
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
    
    public static final int[] EFFECTS = {
	0, 0, 0, 0,
	1, 1, 2, 2,
	3
    };
    
    public static final int EFFECT_NOTHING = 0;
    public static final int EFFECT_BLOCKED = 1;
    public static final int EFFECT_DEATH = 2;
    public static final int EFFECT_SLOW = 3;
    
    private static final int MAP_SIZE = Handler.MAP_SIZE;
    
    private static BufferedImage[] tileImages;
    
//    public static final String[] tileBinaries = {
//	"0000", "0001", "0010", 
//	"0011", "0100", "0101", 
//	"0110", "0111", "1000"
//    };
    
    public static final Color[] tileColors = {
	GP.color(27.6f, 60.7f, 95.7f), GP.color(0f, 0f, 66.3f), GP.color(147.6f, 60.7f, 95.7f),
	GP.color(147.7f, 92.9f, 38.4f), GP.color(0f, 0f, 25.8f), GP.color(180f, 40.5f, 31f),
	GP.color(0f, 100f, 54.5f), GP.color(0f, 0f, 2f), GP.color(209.7f, 83.9f, 100f),
    };
    
    public static final String[] tileText = {
	"dirt", "tile", "moss", "grass", "wall", "rocks", "lava", "hole", "water"
    };
    
    private int[] start;
    private int[] finish;
    
    public static final int TILE_SIZE = 800/Handler.MAP_SIZE;
    
    private int[][] tiles;
    
    public Map() {
	
	while (true) {
	    start = new int[] { 1, 1 };
	    finish = new int[] { MAP_SIZE-1, MAP_SIZE-1 };
	    try {
		tiles = generateMap();
		break;
	    } catch (Exception e) { }
	}
	
	try {
	    tileImages = new BufferedImage[tileText.length];
	    for (int i = 0; i < tileText.length; i++) {
		tileImages[i] = ImageIO.read(new File("resources/" + tileText[i] + ".png"));
	    }
	} catch (Exception e) { }
    }
    
//    public static int getTileID(String binary) {
//	int id = 0;
//	for (int i = 0; i < tileBinaries.length; i++) {
//	    if (tileBinaries[i].equals(binary)) {
//		id = i;
//	    }
//	}
//	return id;
//    }

    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(800,800,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	for (int y = 0; y < tiles.length; y++) {
	    for (int x = 0; x < tiles.length; x++) {
		int tileID = tiles[y][x];
		
		try {
		    g.drawImage(tileImages[tileID], (int)(x*TILE_SIZE), (int)(y*TILE_SIZE), (int) TILE_SIZE, (int) TILE_SIZE, null);
		} catch (Exception e) { }
		
	    }
	}
	
	return img;
    }
    
    public int getTileID(int x, int y) {
	return tiles[y][x];
    }
    
    public int[][] generateMap() throws Exception {
	int[][] finalMap = new int[MAP_SIZE][MAP_SIZE];
	
	int[][] layer = generateWalls();
	
	for (int i = 0; i < 5; i++) {
	    layer = smoothMap(layer, 4);
	}
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		finalMap[y][x] = layer[y][x]*TILE_WALL;
	    }
	}
	
	layer = generateLayer(PERCENT_WATER);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_WATER;
		}
	    }
	}
	
	layer = generateLayer(PERCENT_LAVA);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_LAVA;
		} else if (finalMap[y][x] == TILE_WATER && layer[y][x] == 1) {
		    finalMap[y][x] = TILE_ROCKS;
		}
	    }
	}
	
	while (finalMap[start[1]][start[0]] != 0) {
	    if (start[1] == start[0]) {
		start[0]++;
	    } else {
		start[1]++;
	    }
	}
	finalMap[start[1]][start[0]] = TILE_PATH;
	
	while (finalMap[finish[1]][finish[0]] != 0) {
	    if (finish[1] == finish[0]) {
		finish[0]--;
	    } else {
		finish[1]--;
	    }
	}
	finalMap[finish[1]][finish[0]] = TILE_PATH;
	
	int[] tracePath = new int[] {start[0],start[1]};
	while (tracePath[0] != finish[0] || tracePath[1] != finish[1]) {
	    if (tracePath[0] != finish[0]) {
		if (finalMap[tracePath[1]][tracePath[0]+1] == 0) {
		    tracePath[0]++;
		} else {
		    tracePath[1]++;
		}
	    } else if (tracePath[1] != finish[1]) {
		tracePath[1]++;
	    }
	    
//	    if (finalMap[tracePath[1]][tracePath[0]] == 0) {
		finalMap[tracePath[1]][tracePath[0]] = TILE_PATH;
//	    } else {
//		throw new Exception("Woops.");
//	    }
	}
	
	layer = generateLayer(PERCENT_GRASS);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_MOSS;
		}
	    }
	}
	
	layer = smoothMap(layer, 6);
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		if (finalMap[y][x] == TILE_MOSS && layer[y][x] == 1) {
		    finalMap[y][x] = TILE_GRASS;
		}
	    }
	}
	
	layer = generateLayer(PERCENT_HOLE);
	layer = smoothMap(layer, 4);
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		if (finalMap[y][x] == 0) {
		    finalMap[y][x] = layer[y][x]*TILE_HOLE;
		}
	    }
	}
	
	return finalMap;
    }
    
    public int[][] generateLayer(int percent) {
	int[][] walls = new int[MAP_SIZE][MAP_SIZE];
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
		walls[y][x] = GP.r.nextInt(100) < percent? 1 : 0;
	    }
	}
	
	return walls;
    }
    
    public int[][] generateWalls() {
	int[][] walls = new int[MAP_SIZE][MAP_SIZE];
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {    
		if (x == 0 || x == MAP_SIZE-1 || y == 0 || y == MAP_SIZE-1 ) {
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
	
	for (int y = 0; y < MAP_SIZE; y++) {
	    for (int x = 0; x < MAP_SIZE; x++) {
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
		if (x >= 0 && x <= MAP_SIZE-1 && y >= 0 && y <= MAP_SIZE-1) {
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

    public int[] getStart() {
	return start;
    }
    
    public int[] getFinish() {
	return finish;
    }
}
