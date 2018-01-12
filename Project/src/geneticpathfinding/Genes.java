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
import java.util.ArrayList;

/**
 *
 * @author Andrés Movilla
 */
public class Genes {
    
    
    public static final int STRAND_LENGTH = 3;
    
    public static final int NUM_STRANDS = 10;
    
    /**
     * Strands of genes.
     */
    private String[] identities;
    private String[] reactions;
    
    public Genes() {
	identities = new String[10];
	createIdentities();
	reactions = newReactions();
    }
    
    public String[] getReactions() {
	return reactions;
    }
    
    public String[] getIdentities() {
	return identities;
    }
    
    private void createIdentities() {
	int numIdentities = 0;
	for (int i = 0; i < 2; i++) {
	    for (int j = 0; j < 2; j++) {
		for (int k = 0; k < 2; k++) {
		    this.identities[numIdentities] = i+""+j+""+k;
		    numIdentities++;
		}
	    }
	}
    }
    
    private String[] newReactions() {
	String[] r = new String[10];
	for (int i = 0; i < STRAND_LENGTH; i++) {
	    r[i] = newReaction();
	}
	return r;
    }
    
    public Genes(String[] identities, String[] reactions) {
	this.identities = identities;
	this.reactions = reactions;
    }
    
    public static Genes mutateStrand(Genes original) {
	String[] reactions = original.getReactions();
	
	int chosenStrand;
	chosenStrand = GP.r.nextInt(NUM_STRANDS);
	
	reactions[chosenStrand] = newReaction();
	
	return new Genes(original.getIdentities(), reactions);
    }
    
    public static Genes mutateBit(Genes original) {
	String[] reactions = original.getReactions();
	char[] inverse = new char[] {'1', '0'};
	
	int chosenStrand;
	chosenStrand = GP.r.nextInt(NUM_STRANDS);
	
	int chosenBit = GP.r.nextInt(STRAND_LENGTH);
	
	String strand = reactions[chosenStrand];
	char newBit = inverse[Integer.parseInt(""+strand.charAt(chosenBit))];
	reactions[chosenStrand] = strand.substring(0, chosenBit) + newBit + strand.substring(chosenBit+1);
	
	return new Genes(original.getIdentities(), reactions);
    }
    
    private static String[] mixStrands(String[] father, String[] mother) {
	if (father.length != mother.length)
	    return null;
	
	int length = father.length;
	String[] child = new String[length];
	for (int i = 0; i < length; i++) {
	    double parent = Math.random();
	    if (parent <= 0.4) {
		child[i] = father[i];
	    } else {
		child[i] = mother[i];
	    }
	}
	
	return child;
    }
    
    public static ArrayList<Genes> breed(Genes father, Genes mother) {
	String[] identities = father.getIdentities();
	
	String[] fatherReactions = father.getReactions();
	String[] motherReactions = mother.getReactions();
	String[] mixedReactions = mixStrands(fatherReactions, motherReactions);
	
	ArrayList<Genes> results = new ArrayList<>();
	
	if (Handler.p.getSingleBitMutations()) {
	    //SINGLE 1B ANYTHING
	    results.add(Genes.mutateBit(father));
	    results.add(Genes.mutateBit(mother));
	}
	
	if (Handler.p.getSingleStrandMutations()) {
	    //SINGLE 1 ANYTHING
	    results.add(Genes.mutateStrand(father));
	    results.add(Genes.mutateStrand(mother));
	}
	
	if (Handler.p.getDirectBreed()) {
	    //MIXED ALL
	    results.add(new Genes(identities, mixedReactions));
	}
	
	if (Handler.p.getBreedBitMutations()) {
	    results.add(Genes.mutateBit(new Genes(identities, mixedReactions)));
	}
	
	if (Handler.p.getBreedStrandMutations()) {
	    results.add(Genes.mutateStrand(new Genes(identities, mixedReactions)));
	}
	
	return results;
    }
    
    private static String newReaction() {
	String gene = "";
	for (int i = 0; i < STRAND_LENGTH; i++) {
	    double rnd = Math.random() * 10;
	    rnd = Math.round(rnd/10);
	    gene += (int) rnd;
	}
	
	return gene;
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(180,130,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	/*
	int[] columns = new int[] {10, 70, 130};
	int titleX = 20;
	
	g.setColor(Color.black);
	g.fillRect(0, 0, 180, 130);
	g.setColor(Color.white);
	g.fillRect(2, 2, 176, 126);
	
	g.setFont(new Font("Arial",Font.PLAIN,18));
	int currentCol = alignment*2;
	g.setColor(Color.red);
	for (int i = 0; i < NUM_STRANDS_ACTION; i++) {
	    g.drawString(actionStrands[i],columns[currentCol],56+(i*16));
	}
	currentCol++;
	currentCol%=3;
	g.setColor(Color.blue);
	for (int i = 0; i < NUM_STRANDS_ACTION; i++) {
	    g.drawString(statStrands[i],columns[currentCol],56+(i*16));
	}
	currentCol++;
	for (int i = NUM_STRANDS_ACTION; i < NUM_STRANDS_STAT; i++) {
	    g.drawString(statStrands[i],columns[currentCol],56+((i-NUM_STRANDS_ACTION)*16));
	}
	
	g.setFont(new Font("Arial",Font.BOLD,20));
	g.setColor(Color.BLACK);
	g.drawString("Genetic Code",titleX,30);
	*/
	return img;
    }
}
