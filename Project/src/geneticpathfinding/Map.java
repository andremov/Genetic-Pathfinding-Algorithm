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

    public Map() {
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(350,700,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	return img;
    }
    
}
