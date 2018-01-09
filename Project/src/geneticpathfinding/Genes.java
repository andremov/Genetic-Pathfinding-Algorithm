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
    
    
    public static final int STRAND_LENGTH = 4;
    public static final int VALUE_LENGTH = 2;
    
    public static final int FORCE_MUTATE_STATS = 0;
    public static final int FORCE_MUTATE_ACTIONS = 1;
    public static final int FORCE_NOTHING = 2;
    
    public static final String STRAND_ACTION_UP = "10";
    public static final String STRAND_ACTION_DOWN = "00";
    public static final String STRAND_ACTION_LEFT = "01";
    public static final String STRAND_ACTION_RIGHT = "11";
    
    public static final int NUM_STRANDS = 20;
    
    /**
     * Strands of genes.
     */
    private final String[] strands;
    
    public Genes() {
	strands = newStrands();
    }
    
    public Genes(String[] strands) {
	this.strands = strands;
    }
    
    public static Genes mutateStrand(Genes original, int force) {
	String[] actions = original.getActionStrands();
	String[] stats = original.getStatStrands();
	
	int chosenStrand;
	switch (force) {
	    case FORCE_MUTATE_STATS:
		chosenStrand = AGA.r.nextInt(10);
		break;
	    case FORCE_MUTATE_ACTIONS:
		chosenStrand = AGA.r.nextInt(5) + 10;
		break;
	    default:
		chosenStrand = AGA.r.nextInt(15);
		break;
	}
	
	if (chosenStrand < 10) {
	    stats[chosenStrand] = newStrand();
	} else {
	    chosenStrand -= 10;
	    actions[chosenStrand] = newStrand();
	}
	
	return new Genes(stats, actions);
    }
    
    public static Genes mutateBit(Genes original, int force) {
	String[] actions = original.getActionStrands();
	String[] stats = original.getStatStrands();
	char[] inverse = new char[] {'1', '0'};
	
	int chosenStrand;
	switch (force) {
	    case FORCE_MUTATE_STATS:
		chosenStrand = AGA.r.nextInt(10);
		break;
	    case FORCE_MUTATE_ACTIONS:
		chosenStrand = AGA.r.nextInt(5) + 10;
		break;
	    default:
		chosenStrand = AGA.r.nextInt(15);
		break;
	}
	
	int chosenBit = AGA.r.nextInt(4);
	if (chosenStrand < 10) {
	    String strand = stats[chosenStrand];
	    char newBit = inverse[Integer.parseInt(""+strand.charAt(chosenBit))];
	    stats[chosenStrand] = strand.substring(0, chosenBit) + newBit + strand.substring(chosenBit+1);
	} else {
	    chosenStrand -= 10;
	    String strand = actions[chosenStrand];
	    char newBit = inverse[Integer.parseInt(""+strand.charAt(chosenBit))];
	    actions[chosenStrand] = strand.substring(0, chosenBit) + newBit + strand.substring(chosenBit+1);
	}
	
	return new Genes(stats, actions);
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
	
	String[] fatherStats = father.getStatStrands();
	String[] motherStats = mother.getStatStrands();
	String[] fatherActions = father.getActionStrands();
	String[] motherActions = mother.getActionStrands();
	String[] mixedActions = mixStrands(fatherActions, motherActions);
	String[] mixedStats = mixStrands(fatherStats, motherStats);
	
	ArrayList<Genes> results = new ArrayList<>();
	
	if (Handler.p.getSingleBitMutations()) {
	    //SINGLE 1B ANYTHING
	    results.add(Genes.mutateBit(father, FORCE_NOTHING));
	    results.add(Genes.mutateBit(mother, FORCE_NOTHING));
	}
	
	if (Handler.p.getSingleSpecificBitMutations()) {
	    // SINGLE 1B ACTIONS
	    results.add(Genes.mutateBit(father, FORCE_MUTATE_ACTIONS));
	    results.add(Genes.mutateBit(mother, FORCE_MUTATE_ACTIONS));
	
	    // SINGLE 1B STATS
	    results.add(Genes.mutateBit(father, FORCE_MUTATE_STATS));
	    results.add(Genes.mutateBit(mother, FORCE_MUTATE_STATS));
	}
	
	if (Handler.p.getSingleStrandMutations()) {
	    //SINGLE 1 ANYTHING
	    results.add(Genes.mutateStrand(father, FORCE_NOTHING));
	    results.add(Genes.mutateStrand(mother, FORCE_NOTHING));
	}
	
	if (Handler.p.getSingleSpecificStrandMutations()) {
	    //SINGLE 1 STAT
	    results.add(Genes.mutateStrand(father, FORCE_MUTATE_STATS));
	    results.add(Genes.mutateStrand(mother, FORCE_MUTATE_STATS));
	    
	    //SINGLE 1 ACTION
	    results.add(Genes.mutateStrand(father, FORCE_MUTATE_ACTIONS));
	    results.add(Genes.mutateStrand(mother, FORCE_MUTATE_ACTIONS));
	}
	
	if (Handler.p.getSingleBlockMutations()) {
	    //SINGLE ALL ACTIONS
	    results.add(new Genes(fatherStats, newActionStrands()));
	    results.add(new Genes(motherStats, newActionStrands()));
	    
	    //SINGLE ALL STATS
	    results.add(new Genes(newStatStrands(), fatherActions));
	    results.add(new Genes(newStatStrands(), motherActions));
	}
	
	if (Handler.p.getBreedForceKeep()) {
	    //MIXED ACTIONS
	    results.add(new Genes(fatherStats, mixedActions));
	    results.add(new Genes(motherStats, mixedActions));
	    
	    //MIXED STATS
	    results.add(new Genes(mixedStats, fatherActions));
	    results.add(new Genes(mixedStats, motherActions));
	}

	if (Handler.p.getBreedMakeNew()) {
	    //MIXED + NEW
	    results.add(new Genes(newStatStrands(), mixedActions));
	    results.add(new Genes(mixedStats, newActionStrands()));
	}
	
	if (Handler.p.getDirectBreed()) {
	    //MIXED ALL
	    results.add(new Genes(mixedStats, mixedActions));
	}
	
	if (Handler.p.getBreedBitMutations()) {
	    results.add(Genes.mutateBit(new Genes(mixedStats, mixedActions), FORCE_NOTHING));
	}
	
	if (Handler.p.getBreedStrandMutations()) {
	    results.add(Genes.mutateStrand(new Genes(mixedStats, mixedActions), FORCE_NOTHING));
	}
	
	if (Handler.p.getBreedSpecificBitMutations()) {
	    results.add(Genes.mutateBit(new Genes(mixedStats, mixedActions), FORCE_MUTATE_ACTIONS));
	    results.add(Genes.mutateBit(new Genes(mixedStats, mixedActions), FORCE_MUTATE_STATS));
	}
	
	if (Handler.p.getBreedSpecificStrandMutations()) {
	    results.add(Genes.mutateStrand(new Genes(mixedStats, mixedActions), FORCE_MUTATE_ACTIONS));
	    results.add(Genes.mutateStrand(new Genes(mixedStats, mixedActions), FORCE_MUTATE_STATS));
	}
	
	return results;
    }
    
    public Action getAction(int num) {
	String s = getActionStrands()[num].substring(0, VALUE_LENGTH);
	int action = Action.ACTION_WAIT;
	double delay = 1.0;
	
	if (s.equals(STRAND_ACTION_ATTACK)) {
	    action = Action.ACTION_ATTACK;
	} else if (s.equals(STRAND_ACTION_SHIELD_DOWN)) {
	    action = Action.ACTION_SHIELD_DOWN;
	} else if (s.equals(STRAND_ACTION_SHIELD_UP)) {
	    action = Action.ACTION_SHIELD_UP;
	}
	
	for (int i = VALUE_LENGTH; i < STRAND_LENGTH; i++) {
	    if (getActionStrands()[num].charAt(i) == '1') {
		delay += 0.5;
	    }
	}
	
	return new Action(action, delay);
    }
    
    private static String[] newStatStrands() {
	String[] newStrands = new String[NUM_STRANDS_STAT];
	for (int i = 0; i < NUM_STRANDS_STAT; i++) {
	    newStrands[i] = newStrand();
	}
	return newStrands;
    }
    
    private static String[] newActionStrands() {
	String[] newStrands = new String[NUM_STRANDS_ACTION];
	for (int i = 0; i < NUM_STRANDS_ACTION; i++) {
	    newStrands[i] = newStrand();
	}
	return newStrands;
    }
    
    private static String newStrand() {
	String gene = "";
	for (int i = 0; i < STRAND_LENGTH; i++) {
	    double rnd = Math.random() * 10;
	    rnd = Math.round(rnd/10);
	    gene += (int) rnd;
	}
	
	return gene;
    }
    
    private int findStat(String strand, String stat) {
	int startIndex = 0;
	int number = 0;
	while (startIndex+VALUE_LENGTH <= strand.length()) {
	    if (strand.subSequence(startIndex, startIndex+VALUE_LENGTH).equals(stat)) {
		number++;
	    }
	    startIndex += VALUE_LENGTH;
	}
	return number;
    }

    public int getHealthStat() {
	int healthStat = 1;
	for (int i = 0; i < getStatStrands().length; i++) {
	    String s = getStatStrands()[i];
	    healthStat += findStat(s,STRAND_STAT_HEALTH);
	}
	return healthStat;
    }
    
    public int getAttackStat() {
	int attackStat = 1;
	for (int i = 0; i < getStatStrands().length; i++) {
	    String s = getStatStrands()[i];
	    attackStat += findStat(s,STRAND_STAT_ATTACK);
	}
	return attackStat;
    }
    
    public int getDefenseStat() {
	int defenseStat = 1;
	for (int i = 0; i < getStatStrands().length; i++) {
	    String s = getStatStrands()[i];
	    defenseStat += findStat(s,STRAND_STAT_DEFENSE);
	}
	return defenseStat;
    }
    
    public String[] getStatStrands() {
	return statStrands;
    }

    public String[] getActionStrands() {
	return actionStrands;
    }
    
    public BufferedImage getImage(int alignment) {
	BufferedImage img = new BufferedImage(180,130,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
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
	
	return img;
    }
}
