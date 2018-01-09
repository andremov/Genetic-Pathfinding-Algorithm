/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

/**
 *
 * @author Andrés Movilla
 */
public class Action {
    
    public final static int ACTION_WAIT = 0;
    public final static int ACTION_ATTACK = 1;
    public final static int ACTION_SHIELD_UP = 2;
    public final static int ACTION_SHIELD_DOWN = 3;
    
    /**
     * Code of action.
     */
    private final int actionCode;
    /**
     * Time delay from last action to perform this action.
     */
    private final double timeDelay;

    public Action(int actionCode, double timeDelay) {
	this.timeDelay = timeDelay;
	this.actionCode = actionCode;
    }
    
    public double getDelay() {
	return timeDelay;
    }
    
    public int getAction() {
	return actionCode;
    }
    
    public String getDescription() {
	String d = "";
	switch (actionCode) {
	    case ACTION_WAIT:
		d = "Wait";
		break;
	    case ACTION_ATTACK:
		d = "Attack";
		break;
	    case ACTION_SHIELD_UP:
		d = "Shield Up";
		break;
	    case ACTION_SHIELD_DOWN:
		d = "Shield Down";
		break;
	}
	return d;
    }
    
}
