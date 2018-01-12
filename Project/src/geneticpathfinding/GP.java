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
    
}
