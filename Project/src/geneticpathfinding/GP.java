/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticpathfinding;

import java.util.Random;

/**
 *
 * @author Andrés Movilla
 */
public class GP {
    public static Random r;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	r = new Random();
	
	new Thread(new Handler()).start();
    }
    
    /**
     * devuelve el color dado por parametros en HSB
     * @param h
     * @param s
     * @param b
     * @return 
     */
    public static java.awt.Color color(double h, double s, double b) {
	return java.awt.Color.getHSBColor((float)(h/360f),(float)(s/100f),(float)(b/100f));
    }
}
