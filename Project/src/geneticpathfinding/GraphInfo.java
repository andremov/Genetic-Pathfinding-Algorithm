/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Andrés Movilla
 */
public class GraphInfo {
    
    public static int GRAPH_GENERATIONS = 30;
    
    /**
     * All min history information.
     */
    ArrayList<Integer[]> minInformation;
    /**
     * All max history information.
     */
    ArrayList<Integer[]> maxInformation;
    /**
     * All min quantity history information.
     */
    ArrayList<Integer[]> minQuantityInformation;
    /**
     * All min quantity history information.
     */
    ArrayList<Integer[]> maxQuantityInformation;
    /**
     * All average history information.
     */
    ArrayList<Float[]> avgInformation;
    /**
     * Number of information being tracked.
     */
    int trackedInfo;
    /**
     * Names of information being tracked.
     */
    String[] names;
    /**
     * Highest number reached.
     */
    int[] max;
    /**
     * Lowest number reached.
     */
    int[] min;

    public GraphInfo(String... names) {
	this.names = names;
	this.trackedInfo = names.length;
	this.minInformation = new ArrayList<>();
	this.maxInformation = new ArrayList<>();
	this.minQuantityInformation = new ArrayList<>();
	this.maxQuantityInformation = new ArrayList<>();
	this.avgInformation = new ArrayList<>();
	this.max = new int[this.trackedInfo];
	this.min = new int[this.trackedInfo];
	for (int i = 0; i < min.length; i++) {
	    min[i] = 1000;
	}
    }
    
    public void addNewMin(Integer[] ints) {
	for (int i = 0; i < ints.length; i++) {
	    if (ints[i] < min[i]) {
		min[i] = ints[i];
	    }
	}
	minInformation.add(ints);
    }
    
    public void addNewMax(Integer[] ints) {
	for (int i = 0; i < ints.length; i++) {
	    if (ints[i] > max[i]) {
		max[i] = ints[i];
	    }
	}
	maxInformation.add(ints);
    }
    
    public void addNewMinQuantity(Integer[] ints) {
	minQuantityInformation.add(ints);
    }
    
    public void addNewMaxQuantity(Integer[] ints) {
	maxQuantityInformation.add(ints);
    }
    
    public void addNewAvg(Float[] ints) {
	avgInformation.add(ints);
    }
    
    public String getName(int index) {
	return this.names[index];
    }
    
    public BufferedImage getGraphImage(int tracker) {
	int iSize = 250;
	BufferedImage img = new BufferedImage(iSize,iSize,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	
	try {

	    int dotSize = 5;

	    int stepX = (iSize-20)/GRAPH_GENERATIONS;
	    int stepY = (iSize-20)/((max[tracker]-min[tracker])+1);

	    g.setColor(Color.black);
	    for (int i = min[tracker]; i <= max[tracker]; i++) {
		g.drawString(""+i,5,(iSize-15)-(stepY*(i-min[tracker])));
	    }

	    int startGeneration = Integer.max(minInformation.size()-GRAPH_GENERATIONS,0);
	    g.setColor(Color.black);
	    g.drawLine(20,0,20,(iSize-20));
	    g.drawLine(20,(iSize-20),iSize,(iSize-20));


	    int lastX = -1;
	    int lastMinY = -1;
	    int lastAvgY = -1;
	    int lastMaxY = -1;

	    for (int i = startGeneration; i < minInformation.size(); i++) {

		int curX = 20 + stepX*(i-startGeneration);
		int curMinY = (iSize-20)-(stepY*(minInformation.get(i)[tracker]-min[tracker]));
		int curMaxY = (iSize-20)-(stepY*(maxInformation.get(i)[tracker]-min[tracker]));
		int curAvgY = (int)((iSize-20)-(stepY*(avgInformation.get(i)[tracker]-min[tracker])));

		if (lastMinY == lastMaxY && curMinY == curMaxY) {
		    g.setColor(Color.green);
		    if (lastMaxY > 0) {
			g.drawLine(lastX,lastMaxY,curX,curMaxY);
		    }
		} else {
		    g.setColor(Color.blue);
		    if (lastMaxY > 0) {
			g.drawLine(lastX,lastMaxY,curX,curMaxY);
		    }

		    g.setColor(Color.red);
		    if (lastMinY > 0) {
			g.drawLine(lastX,lastMinY,curX,curMinY);
		    }

		    g.setColor(Color.orange);
		    if (lastAvgY > 0) {
			g.drawLine(lastX,lastAvgY,curX,curAvgY);
		    }
		}

		lastX = curX;
		lastMinY = curMinY;
		lastMaxY = curMaxY;
		lastAvgY = curAvgY;
	    }

	    for (int i = startGeneration; i < minInformation.size(); i++) {

		int curX = 20 + stepX*(i-startGeneration);
		int curMinY = (iSize-20)-(stepY*(minInformation.get(i)[tracker]-min[tracker]));
		int curMaxY = (iSize-20)-(stepY*(maxInformation.get(i)[tracker]-min[tracker]));
		int curAvgY = (int)((iSize-20)-(stepY*(avgInformation.get(i)[tracker]-min[tracker])));

		if (i % 5 == 0) {
		    g.setColor(Color.black);
		    g.drawString(""+i,curX-5,(iSize-5));
		}

		if (curMinY != curMaxY) {
		    g.setColor(Color.blue);
		    g.fillRect(curX-(dotSize/2), curMaxY-(dotSize/2), dotSize, dotSize);

		    g.setColor(Color.red);
		    g.fillRect(curX-(dotSize/2), curMinY-(dotSize/2), dotSize, dotSize);

		    g.setColor(Color.orange);
		    g.fillRect(curX-(dotSize/2), curAvgY-(dotSize/2), dotSize, dotSize);
		} else {
		    g.setColor(Color.green);
		    g.fillRect(curX-(dotSize/2), curMinY-(dotSize/2), dotSize, dotSize);
		}
	    }

	    try {
		if (lastMinY != lastMaxY) {
		    g.setColor(Color.blue);
		    g.drawString(""+maxQuantityInformation.get(maxQuantityInformation.size()-1)[tracker],lastX+10,lastMaxY);

		    g.setColor(Color.red);
		    g.drawString(""+minQuantityInformation.get(minQuantityInformation.size()-1)[tracker],lastX+10,lastMinY);
		} else {
		    g.setColor(Color.green);
		    g.drawString(""+minQuantityInformation.get(minQuantityInformation.size()-1)[tracker],lastX+10,lastMinY);
		}
	    } catch (Exception ex2) { }
	} catch (Exception ex2) { }
	
	return img;
    }
    
    
}
