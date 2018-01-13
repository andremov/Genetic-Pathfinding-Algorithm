/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

/**
 *
 * @author Andrés Movilla
 */
public class Window extends JFrame implements Runnable {
    /**
     * Canvas.
     */
    private Canvas c;
    /**
     * Simulation handler.
     */
    private final Handler parent;
    
    public Window(Handler parent) {
	this.parent = parent;
	setSize(808,831);
	setTitle("Simulations");
	setResizable(false);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setLayout(null);
	
	init();
	
	setVisible(true);
    }
    
    private void init() {
	c = new Canvas();
	c.setSize(800,800);
	c.setLocation(1,1);
	c.setFocusable(false);
	add(c);
	
	addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		    parent.settingsKey(Handler.SETTINGS_KEY_LEFT);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		    parent.settingsKey(Handler.SETTINGS_KEY_RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
		    parent.settingsKey(Handler.SETTINGS_KEY_UP);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
		    parent.settingsKey(Handler.SETTINGS_KEY_DOWN);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    parent.settingsKey(Handler.SETTINGS_KEY_DONE);
		}
	    }
	    
	});
    }

    @Override
    public void run() {
	c.createBufferStrategy(2);
	while (true) {
	    Graphics g = c.getBufferStrategy().getDrawGraphics();
	    g.clearRect(0, 0, 800, 800);
	    
	    g.drawImage(parent.getImage(),0,0,null);
	    
	    
	    c.getBufferStrategy().show();
	}
    }
    
}

