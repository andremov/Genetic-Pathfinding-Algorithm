/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andrés Movilla
 */
public class Strategy {
    
    private final Action[] actions;
    private int nextAction;
    private double nextActionTime;
	    
    private int[] numActions;
    private int strategyLength;
    
    public Strategy(Genes g) {
	actions = new Action[5];
	for (int i = 0; i < actions.length; i++) {
	    actions[i] = g.getAction(i);
	}
	nextAction = 0;
	nextActionTime = actions[nextAction].getDelay();
	calcValues();
    }
    
    private void calcValues() {
	numActions = new int[4];
	strategyLength = 0;
	for (int i = 0; i < actions.length; i++) {
	    strategyLength += actions[i].getDelay();
	    numActions[actions[i].getAction()]++;
	}
    }
    
    public double getNextActionTime() {
	return nextActionTime;
    }
    
    public void reset() {
	nextAction = 0;
	nextActionTime = actions[nextAction].getDelay();
    }
    
    public int nextAction() {
	int actionCode = actions[nextAction].getAction();
	nextAction++;
	
	if (nextAction == actions.length) {
	    nextAction = 0;
	}
	nextActionTime += actions[nextAction].getDelay();
	
	return actionCode;
    }
    
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(170,300,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	g.setColor(Color.black);
	g.fillRect(0, 0, 170, 300);
	g.setColor(Color.white);
	g.fillRect(2, 2, 166, 296);
	
	g.setColor(Color.black);
	g.setFont(new Font("Arial",Font.BOLD,20));
	
	for (int i = 0; i < actions.length; i++) {
	    g.setColor(Color.black);
	    
	    g.setFont(new Font("Arial",Font.BOLD,20));
	    g.drawString(actions[i].getDescription(),45,30+(i*60));
	    
	    g.setFont(new Font("Arial",Font.PLAIN,18));
	    g.drawString("Delay: "+actions[i].getDelay()+"s",70,50+(i*60));
	    
	    if (i == nextAction) {
		g.setColor(Color.red);
		g.fillOval(10, 12+(i*60), 20, 20);
	    }
	}
	return img;
    }

    public int getStrategyLength() {
	return strategyLength;
    }
    
    public int getNumWait() {
	return numActions[Action.ACTION_WAIT];
    }
    
    public int getNumAttack() {
	return numActions[Action.ACTION_ATTACK];
    }
    
    public int getNumShieldUp() {
	return numActions[Action.ACTION_SHIELD_UP];
    }
    
    public int getNumShieldDown() {
	return numActions[Action.ACTION_SHIELD_DOWN];
    }
    
}
