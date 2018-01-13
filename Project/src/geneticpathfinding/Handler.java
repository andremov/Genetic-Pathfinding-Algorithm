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
public class Handler implements Runnable {
    
    static final int STATE_SETTINGS = -1;
    static final int STATE_START = 0;
    static final int STATE_SIMULATION = 1;
    static final int STATE_RANK = 2;
    static final int STATE_DELETE = 3;
    static final int STATE_BREED = 4;
    static final int STATE_CREATE = 5;
    static final int STATE_MIX = 6;
    static final int STATE_CHECK = 7;
    
    static final int SETTINGS_KEY_UP = 0;
    static final int SETTINGS_KEY_DOWN = 1;
    static final int SETTINGS_KEY_LEFT = 2;
    static final int SETTINGS_KEY_RIGHT = 3;
    static final int SETTINGS_KEY_DONE = 4;
    
    public static final Parameters p = new Parameters();
    public static GraphInfo genInfo;
    
    //<editor-fold defaultstate="collapsed" desc="Fields">
    /**
     * Current active state.
     */
    private int currentState;
    /**
     * List of bots.
     */
    public ArrayList<Bot> botList;
    /**
     * Battling bot 1.
     */
    private int battleBot1;
    /**
     * Battling bot 2.
     */
    private int battleBot2;
    /**
     * Selected bot 1.
     */
    private int breedBot1;
    /**
     * Selected bot 2.
     */
    private int breedBot2;
    /**
     * Current generation.
     */
    private int currentGen;
    /**
     * Current bot in creation.
     */
    private int creatingBot;
    /**
     * Current bot in ranking.
     */
    private int rankingBot;
    /**
     * Current bot in mixing.
     */
    private int mixBot;
    /**
     * Current bot being bred.
     */
    private int breedingsDone;
    /**
     * Current setting in editing.
     */
    private int editSetting;
    /**
     * Battle timer.
     */
    private double battleTick;
    /**
     * Internal battle timer.
     */
    private double internalTick;
    /**
     * Delay for state transitions.
     */
    private double waitTicks;
    /**
     * End of internal battle timer.
     */
    private final double maxInternalTick = 100000;
    /**
     * Force draw.
     */
    private final double maxBattleTick = 200;
    /**
     * Max wait time for state transitions.
     */
    private final double maxWaitTicks = 10000;
    //</editor-fold>
    
    public Handler() {
	botList = new ArrayList<>();
	currentState = -1;
	currentGen = 0;
	waitTicks = 0;
	editSetting = 0;
//	genInfo = new GraphInfo("Health Stat", "Attack Stat", "Defense Stat", "Strategy Length", "Wait Numbers", "Attack Numbers","Shield Up Numbers","Shield Down Numbers");
	new Thread(new Window(this)).start();
//	for (int i = 0; i < genInfo.trackedInfo; i++) {
//	    new Thread(new GraphWindow(i)).start();
//	}
    }

    private void startBattle() {
	battleTick = 0;
	currentState = STATE_SIMULATION;
    }
    
    public void settingsKey(int key) {
	if (currentState == STATE_SETTINGS && waitTicks == 0) {
	    if (key == SETTINGS_KEY_DONE) {
		waitTicks++;
	    } else if (key == SETTINGS_KEY_LEFT || key == SETTINGS_KEY_RIGHT) {
		p.modifySetting(editSetting,key == SETTINGS_KEY_LEFT? -1 : 1);
	    } else if (key == SETTINGS_KEY_DOWN) {
		editSetting++;
	    } else if (key == SETTINGS_KEY_UP) {
		editSetting--;
	    }
	    if (editSetting < 0) {
		editSetting = p.lastSetting();
	    } else if (editSetting > p.lastSetting()) {
		editSetting = 0;
	    }
	}
    }
    
    private void settingsTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    creatingBot = 0;
	    waitTicks = 0;
	    currentState = STATE_START;
	} else {
//	    if (creatingBot == realPopulation) {
//		waitTicks++;
//	    } else {
//		botList.add(new Bot(currentGen,creatingBot));
//		creatingBot++;
//	    }
	}
    }
    
    private void startTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    creatingBot = 0;
	    currentGen++;
	    waitTicks = 0;

	    saveHistory();
	    
	    startBattle();
	} else {
	    if (creatingBot == p.getStartingPopulation()) {
		waitTicks++;
	    } else {
		botList.add(new Bot(currentGen,creatingBot));
		creatingBot++;
	    }
	}
    }
    
    private void saveHistory() {
	/*
	Integer[] step;
	int amount;
	int index;
	int startMax = 2000;
	int startMin = -1;
	int quantity;
	quantity = 0;
	
	//<editor-fold desc="MIN">
	step = new Integer[8];
	// MIN HEALTH
	amount = startMax;
	index = 0;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatHealth() < amount) {
		amount = botList.get(i).getStatHealth();
	    }
	}
	step[index] = amount;
	
	// MIN ATTACK
	amount = startMax;
	index = 1;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatAttack() < amount) {
		amount = botList.get(i).getStatAttack();
	    }
	}
	step[index] = amount;
	
	// MIN DEFENSE
	amount = startMax;
	index = 2;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatDefense() < amount) {
		amount = botList.get(i).getStatDefense();
	    }
	}
	step[index] = amount;
	
	// MIN STR LENGTH
	amount = startMax;
	index = 3;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getStrategyLength() < amount) {
		amount = botList.get(i).getStrategy().getStrategyLength();
	    }
	}
	step[index] = amount;
	
	// MIN WAIT
	amount = startMax;
	index = 4;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumWait() < amount) {
		amount = botList.get(i).getStrategy().getNumWait();
	    }
	}
	step[index] = amount;
	
	// MIN ATTACK
	amount = startMax;
	index = 5;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumAttack() < amount) {
		amount = botList.get(i).getStrategy().getNumAttack();
	    }
	}
	step[index] = amount;
	
	// MIN SHIELD UP
	amount = startMax;
	index = 6;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldUp() < amount) {
		amount = botList.get(i).getStrategy().getNumShieldUp();
	    }
	}
	step[index] = amount;
	
	// MIN SHIELD DOWN
	amount = startMax;
	index = 7;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldDown() < amount) {
		amount = botList.get(i).getStrategy().getNumShieldDown();
	    }
	}
	step[index] = amount;
	
	genInfo.addNewMin(step);
	//</editor-fold>
	
	//<editor-fold desc="MAX">
	step = new Integer[8];
	// MAX HEALTH
	amount = startMin;
	index = 0;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatHealth() > amount) {
		amount = botList.get(i).getStatHealth();
	    }
	}
	step[index] = amount;
	
	// MAX ATTACK
	amount = startMin;
	index = 1;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatAttack() > amount) {
		amount = botList.get(i).getStatAttack();
	    }
	}
	step[index] = amount;
	
	// MAX DEFENSE
	amount = startMin;
	index = 2;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatDefense() > amount) {
		amount = botList.get(i).getStatDefense();
	    }
	}
	step[index] = amount;
	
	// MAX STR LENGTH
	amount = startMin;
	index = 3;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getStrategyLength() > amount) {
		amount = botList.get(i).getStrategy().getStrategyLength();
	    }
	}
	step[index] = amount;
	
	// MAX WAIT
	amount = startMin;
	index = 4;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumWait() > amount) {
		amount = botList.get(i).getStrategy().getNumWait();
	    }
	}
	step[index] = amount;
	
	// MAX ATTACK
	amount = startMin;
	index = 5;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumAttack() > amount) {
		amount = botList.get(i).getStrategy().getNumAttack();
	    }
	}
	step[index] = amount;
	
	// MAX SHIELD UP
	amount = startMin;
	index = 6;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldUp() > amount) {
		amount = botList.get(i).getStrategy().getNumShieldUp();
	    }
	}
	step[index] = amount;
	
	// MAX SHIELD DOWN
	amount = startMin;
	index = 7;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldDown() > amount) {
		amount = botList.get(i).getStrategy().getNumShieldDown();
	    }
	}
	step[index] = amount;
	genInfo.addNewMax(step);
	//</editor-fold>
	
	//<editor-fold desc="AVG">
	Float[] ftep = new Float[8];
	// AVG HEALTH
	amount = 0;
	index = 0;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStatHealth();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG ATTACK
	amount = 0;
	index = 1;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStatAttack();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG DEFENSE
	amount = 0;
	index = 2;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStatDefense();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG STR LENGTH
	amount = 0;
	index = 3;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStrategy().getStrategyLength();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG WAIT
	amount = 0;
	index = 4;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStrategy().getNumWait();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG ATTACK
	amount = 0;
	index = 5;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStrategy().getNumAttack();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG SHIELD UP
	amount = 0;
	index = 6;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStrategy().getNumShieldUp();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	
	// AVG SHIELD DOWN
	amount = 0;
	index = 7;
	for (int i = 0; i < botList.size(); i++) {
	    amount += botList.get(i).getStrategy().getNumShieldDown();
	}
	ftep[index] = (Float)(float)amount/botList.size();
	genInfo.addNewAvg(ftep);
	//</editor-fold>
	
	//<editor-fold desc="MIN QUANTITY">
	step = new Integer[8];
	// MIN HEALTH
	amount = startMax;
	quantity = 1;
	index = 0;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatHealth() < amount) {
		amount = botList.get(i).getStatHealth();
		quantity = 1;
	    } else if (botList.get(i).getStatHealth() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN ATTACK
	amount = startMax;
	quantity = 1;
	index = 1;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatAttack() < amount) {
		amount = botList.get(i).getStatAttack();
		quantity = 1;
	    } else if (botList.get(i).getStatAttack() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN DEFENSE
	amount = startMax;
	quantity = 1;
	index = 2;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatDefense() < amount) {
		amount = botList.get(i).getStatDefense();
		quantity = 1;
	    } else if (botList.get(i).getStatDefense() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN STR LENGTH
	amount = startMax;
	quantity = 1;
	index = 3;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getStrategyLength() < amount) {
		amount = botList.get(i).getStrategy().getStrategyLength();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getStrategyLength() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN WAIT
	amount = startMax;
	quantity = 1;
	index = 4;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumWait() < amount) {
		amount = botList.get(i).getStrategy().getNumWait();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumWait() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN ATTACK
	amount = startMax;
	quantity = 1;
	index = 5;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumAttack() < amount) {
		amount = botList.get(i).getStrategy().getNumAttack();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumAttack() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN SHIELD UP
	amount = startMax;
	quantity = 1;
	index = 6;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldUp() < amount) {
		amount = botList.get(i).getStrategy().getNumShieldUp();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumShieldUp() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN SHIELD DOWN
	amount = startMax;
	quantity = 1;
	index = 7;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldDown() < amount) {
		amount = botList.get(i).getStrategy().getNumShieldDown();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumShieldDown() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	genInfo.addNewMinQuantity(step);
	//</editor-fold>
	
	//<editor-fold desc="MAX QUANTITY">
	step = new Integer[8];
	// MIN HEALTH
	amount = startMin;
	quantity = 1;
	index = 0;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatHealth() > amount) {
		amount = botList.get(i).getStatHealth();
		quantity = 1;
	    } else if (botList.get(i).getStatHealth() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN ATTACK
	amount = startMin;
	quantity = 1;
	index = 1;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatAttack() > amount) {
		amount = botList.get(i).getStatAttack();
		quantity = 1;
	    } else if (botList.get(i).getStatAttack() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN DEFENSE
	amount = startMin;
	quantity = 1;
	index = 2;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStatDefense() > amount) {
		amount = botList.get(i).getStatDefense();
		quantity = 1;
	    } else if (botList.get(i).getStatDefense() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN STR LENGTH
	amount = startMin;
	quantity = 1;
	index = 3;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getStrategyLength() > amount) {
		amount = botList.get(i).getStrategy().getStrategyLength();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getStrategyLength() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN WAIT
	amount = startMin;
	quantity = 1;
	index = 4;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumWait() > amount) {
		amount = botList.get(i).getStrategy().getNumWait();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumWait() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN ATTACK
	amount = startMin;
	quantity = 1;
	index = 5;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumAttack() > amount) {
		amount = botList.get(i).getStrategy().getNumAttack();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumAttack() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN SHIELD UP
	amount = startMin;
	quantity = 1;
	index = 6;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldUp() > amount) {
		amount = botList.get(i).getStrategy().getNumShieldUp();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumShieldUp() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	// MIN SHIELD DOWN
	amount = startMin;
	quantity = 1;
	index = 7;
	for (int i = 0; i < botList.size(); i++) {
	    if (botList.get(i).getStrategy().getNumShieldDown() > amount) {
		amount = botList.get(i).getStrategy().getNumShieldDown();
		quantity = 1;
	    } else if (botList.get(i).getStrategy().getNumShieldDown() == amount) {
		quantity++;
	    }
	}
	step[index] = quantity;
	
	genInfo.addNewMaxQuantity(step);
	//</editor-fold>
	*/
    }
    
    private void createTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    creatingBot = 0;
	    currentGen++;
	    waitTicks = 0;

	    startBattle();
	} else {
	    if (botList.size() == p.getRealPopulation()) {
		waitTicks++;
	    } else {
		botList.add(new Bot(currentGen,creatingBot));
		creatingBot++;
	    }
	}
    }
    
    private void mixTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    waitTicks = 0;
	    currentState = STATE_DELETE;
	} else {
	    if (mixBot == botList.size()-2) {
		waitTicks++;
	    } else {
		int mixSwitch = GP.r.nextInt(botList.size()-mixBot)+mixBot;
		Bot temp = botList.get(mixSwitch);
		botList.set(mixSwitch, botList.get(mixBot));
		botList.set(mixBot, temp);

		mixBot++;
	    }
	}
    }
    
    private void deleteTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    breedBot1 = 0;
	    breedBot2 = 1;
	    breedingsDone = 0;
	    
	    for (int i = p.getNumberKept(); i < botList.size(); i++) {
		botList.get(i).survivedSimulation();
	    }
	    
	    currentState = STATE_BREED;
	    waitTicks = 0;
	} else {
	    if (botList.size() == p.getSurvivors()) {
		waitTicks++;
	    } else {
		botList.remove(botList.size()-1);
	    }
	}
    }
    
    private void breedTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    waitTicks = 0;
	    
	    saveHistory();
	    
	    currentState = STATE_CREATE;
	} else {
	    if (breedingsDone == p.getNumberBreeds()*p.getBreedsPerPair()) {
		waitTicks++;
	    } else {
		ArrayList<Genes> newGenes = Genes.breed(botList.get(breedBot1).getGenes(), botList.get(breedBot2).getGenes());
		for (int i = 0; i < newGenes.size(); i++) {
		    botList.add(new Bot(currentGen, creatingBot, newGenes.get(i)));
		    creatingBot++;
		}

		breedingsDone++;
		if (breedingsDone%p.getBreedsPerPair() == 0) {
		    breedBot1 += 2;
		    breedBot2 += 2;
		}
	    }
	}
    }
    
    private void rankTick() {
	if (waitTicks > 0 && waitTicks < maxWaitTicks) {
	    waitTicks++;
	} else if (waitTicks >= maxWaitTicks) {
	    waitTicks = 0;
	    
	    mixBot = p.getNumberKept();
	    currentState = STATE_CHECK;
	} else {
	    if (rankingBot < botList.size()) {
		int best = 0;
		int score = -1;
		
		for (int j = rankingBot; j < botList.size(); j++) {
		    if (botList.get(j).getFitness()> score) {
			best = j;
			score = botList.get(j).getFitness();
		    }
		}

		Bot temp = botList.get(best);
		botList.set(best, botList.get(rankingBot));
		botList.set(rankingBot, temp);
		rankingBot++;
	    } else {
		waitTicks++;
	    }
	}
    }
    
    private void checkTick() {
//	if (botList.get(0).getBestGeneration() >= p.getEndSimulationParameter()) {
	    
//	} else {
	    currentState = STATE_MIX;
//	}
    }
    
    private void simTick() {
	if (battleTick > maxBattleTick) {
//	    botList.get(battleBot1).kill();
//	    botList.get(battleBot2).kill();
	} else {
	    battleTick++;
	    
	}
	
    }
    
    @Override
    public void run() {
	while (true) {
	    internalTick++;
	    if (internalTick > maxInternalTick) {
		internalTick = 0;
		switch(currentState) {
		    case STATE_CHECK:
			checkTick();
			break;
		    case STATE_SETTINGS:
			settingsTick();
			break;
		    case STATE_START:
			startTick();
			break;
		    case STATE_CREATE:
			createTick();
			break;
		    case STATE_DELETE:
			deleteTick();
			break;
		    case STATE_MIX:
			mixTick();
			break;
		    case STATE_BREED:
			breedTick();
			break;
		    case STATE_RANK:
			rankTick();
			break;
		    case STATE_SIMULATION:
			simTick();
			break;
		}
	    }
//	    try { 
//		Thread.sleep(1);
//	    } catch (Exception e) { }
	}
    }
    
    public BufferedImage getImage() {
	BufferedImage img = new BufferedImage(700,700,BufferedImage.TYPE_INT_ARGB);
	Graphics g = img.getGraphics();
	
	g.setColor(Color.blue);
	float waitPercent = (float) ((maxWaitTicks-waitTicks)/maxWaitTicks);
	g.fillRect(0, 680, (int) (700*waitPercent), 20);
	float processPercent;
		
	switch(currentState) {
	    case STATE_SETTINGS:
		//<editor-fold desc="SETTINGS">
		int lineSpacing = 30;
		int fontSize = 22;
		int arrowY = 52+(editSetting*lineSpacing);
		int arrowX = 530;
		int arrowSpacingX = 100;
		
		g.setColor(Color.BLACK);
		g.fillRect(arrowX+5,arrowY-5,arrowSpacingX-10,30);
		g.setColor(Color.WHITE);
		g.fillRect(arrowX+7,arrowY-3,arrowSpacingX-14,26);

		g.setColor(Color.BLACK);
		g.drawLine(arrowX,arrowY,arrowX,arrowY+20);
		g.drawLine(arrowX+arrowSpacingX,arrowY,arrowX+arrowSpacingX,arrowY+20);

		g.drawLine(arrowX-15,arrowY+10,arrowX,arrowY);
		g.drawLine(arrowX+arrowSpacingX+15,arrowY+10,arrowX+arrowSpacingX,arrowY);

		g.drawLine(arrowX-15,arrowY+10,arrowX,arrowY+20);
		g.drawLine(arrowX+arrowSpacingX+15,arrowY+10,arrowX+arrowSpacingX,arrowY+20);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString("Settings",310,30);
		
		for (int i = 0; i <= p.lastSetting(); i++) {
		    g.setColor(Color.BLACK);
		    g.setFont(new Font("Arial",Font.PLAIN,fontSize));
		    g.drawString(p.getSettingName(i),10,70+(lineSpacing*i));
		    Object value = p.getSettingValue(i);
		    g.setFont(new Font("Arial",Font.BOLD,fontSize));
		    
		    if (value instanceof Boolean) {
			boolean value2 = (boolean) p.getSettingValue(i);
			g.setColor(value2? Color.green : Color.red);
			g.drawString(""+value2,550,70+(lineSpacing*i));
		    } else if (value instanceof Float) {
			float value2 = (float) p.getSettingValue(i);
			g.setColor(Color.magenta);
			g.drawString((int)(value2*100)+"%",550,70+(lineSpacing*i));
		    } else if (value instanceof Integer) {
			int value2 = (int) p.getSettingValue(i);
			g.setColor(Color.magenta);
			g.drawString(""+value2,550,70+(lineSpacing*i));
		    }
		}
		
		g.setColor(Color.BLACK);
		float setIndex = p.lastSetting()+2.5f;
		g.setFont(new Font("Arial",Font.PLAIN,fontSize));
		g.drawString("Real population",10,(int)(70+(lineSpacing*setIndex)));
		int value = p.getRealPopulation();
		g.setFont(new Font("Arial",Font.BOLD,fontSize));

		g.setColor(Color.magenta);
		g.drawString(""+value,550,(int)(70+(lineSpacing*setIndex)));
		//</editor-fold>
		break;
	    case STATE_CHECK:
		//<editor-fold desc="CHECK">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("End of simulation.",400,100);
		try {
		    g.drawImage(botList.get(0).getLastImage( ),0,0,null);
		} catch (Exception e) { }
		//</editor-fold>
		break;
	    case STATE_START:
		//<editor-fold desc="START">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Creating starting generation...",140,100);
		
		g.fillRect(150, 150, 400, 50);
		g.setColor(Color.white);
		g.fillRect(153, 153, 394, 44);
		
		processPercent = (float)creatingBot/p.getStartingPopulation();
		
		g.setColor(Color.green);
		g.fillRect(153, 153, (int)(394*processPercent), 44);

		g.setColor(Color.BLACK);
		g.drawString((int)(processPercent*100)+"%",310,190);
		//</editor-fold>
		break;
	    case STATE_CREATE:
		//<editor-fold desc="CREATE">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Creating new bots...",200,100);
		
		g.fillRect(150, 150, 400, 50);
		g.setColor(Color.white);
		g.fillRect(153, 153, 394, 44);
		
		processPercent = (float)(botList.size()-(p.getRealPopulation()-p.getNumberNew()))/p.getNumberNew();
		
		g.setColor(Color.green);
		g.fillRect(153, 153, (int)(394*processPercent), 44);

		g.setColor(Color.BLACK);
		g.drawString((int)(processPercent*100)+"%",310,190);
		//</editor-fold>
		break;
	    case STATE_DELETE:
		//<editor-fold desc="DELETE">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Deleting...",280,100);
		
		g.fillRect(150, 150, 400, 50);
		g.setColor(Color.white);
		g.fillRect(153, 153, 394, 44);
		
		int botsToDelete = botList.size()-(p.getSurvivors());
		processPercent = 1.0f-((float)botsToDelete/botList.size());
		
		g.setColor(Color.green);
		g.fillRect(153, 153, (int)(394*processPercent), 44);

		g.setColor(Color.BLACK);
		g.drawString((int)(processPercent*100)+"%",310,190);
		//</editor-fold>
		break;
	    case STATE_BREED:
		//<editor-fold desc="BREED">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Breeding...",280,100);
		
		g.fillRect(150, 150, 400, 50);
		g.setColor(Color.white);
		g.fillRect(153, 153, 394, 44);
		
		processPercent = (float)breedingsDone/(p.getNumberBreeds()*p.getBreedsPerPair());
		
		g.setColor(Color.green);
		g.fillRect(153, 153, (int)(394*processPercent), 44);

		g.setColor(Color.BLACK);
		g.drawString((int)(processPercent*100)+"%",310,190);
		//</editor-fold>
		break;
	    case STATE_MIX:
		//<editor-fold desc="MIX">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Mixing...",290,100);
		//</editor-fold>
		break;
	    case STATE_RANK:
		//<editor-fold desc="RANK">
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		g.drawString("Organizing...",280,100);
		
		g.setFont(new Font("Arial",Font.BOLD,25));
		
		g.drawString("Best:",330,180);
		g.drawImage(botList.get(0).getBriefImage(),175,200,null);
		
		g.setFont(new Font("Arial",Font.PLAIN,20));
//		g.drawString("Number of bests: ",190,300);
//		g.drawString(""+botList.get(0).getBestGeneration(),480,300);
		
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString("Worst:",325,400);
		g.drawImage(botList.get(botList.size()-1).getBriefImage(),175,420,null);
		//</editor-fold>
		break;
	    case STATE_SIMULATION:
		//<editor-fold desc="BATTLE">
		g.setFont(new Font("Arial",Font.PLAIN,18));
		g.setColor(Color.BLACK);
		try {
		    g.drawImage(botList.get(battleBot1).getImage(0),0,0,null);
		    g.drawString("Bot "+(battleBot1+1)+"/"+botList.size(),180,50);
		    g.drawImage(botList.get(battleBot2).getImage(1),350,0,null);
		    g.drawString("Bot "+(battleBot2+1)+"/"+botList.size(),530,50);
		} catch (Exception e) { }
		
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(Color.BLACK);
		int time = (int) ((Math.floor((maxBattleTick-battleTick)*10))/10);
		g.drawString(time+"",580,670);
		g.drawString("Current Generation: "+(currentGen-1),5,670);
		//</editor-fold>
		break;
	}
	    
	return img;
    }
    
}

