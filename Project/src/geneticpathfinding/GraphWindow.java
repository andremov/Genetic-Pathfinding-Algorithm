/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 *
 * @author Andrés Movilla
 */
public class GraphWindow  extends JFrame implements Runnable {
    /**
     * Canvas.
     */
    private Canvas c;
    /**
     * Information to display.
     */
    private final int trackInfo;
    
    private final int cSize = 250;
    
    public GraphWindow(int trackInfo) {
	this.trackInfo = trackInfo;
	
	setSize(cSize+8,cSize+31);
	setTitle(Handler.genInfo.getName(trackInfo)+ " Graph");
	setResizable(false);
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	setLocationRelativeTo(null);
	setLayout(null);
	
	init();
	
	setVisible(true);
    }
    
    private void init() {
	c = new Canvas();
	c.setSize(cSize,cSize);
	c.setLocation(1,1);
	c.setFocusable(false);
	add(c);
    }

    @Override
    public void run() {
	c.createBufferStrategy(2);
	while (true) {
	    Graphics g = c.getBufferStrategy().getDrawGraphics();
	    g.clearRect(0, 0, cSize, cSize);
	    
	    g.drawImage(Handler.genInfo.getGraphImage(trackInfo),0,0,null);
	    
	    c.getBufferStrategy().show();
	    
	    try {
		Thread.sleep(100);
	    } catch (Exception e) { }
	}
    }
    
}
