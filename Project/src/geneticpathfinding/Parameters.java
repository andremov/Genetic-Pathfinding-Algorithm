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
public class Parameters {
    
    private final static int POPULATION_MAX = 500;
    private final static int POPULATION_TICK = 10;
    
    private int singleBitMutations;
    private int singleStrandMutations;
    private int directBreed;
    private int breedBitMutations;
    private int breedStrandMutations;
    
    private float percentKept;
    private float percentRandom;
    private float percentNew;
    
    private int startingPopulation;
    private int breedsPerPair;
    private int endSimulationParameter;
    
    private int breedOffspring;
    private int numberKept;
    private int numberRandom;
    private int numberNew;
    private int numberBreeds;
    private int realPopulation;

    public Parameters() {
	this.directBreed = 0;
	this.breedBitMutations = 1;
	this.breedStrandMutations = 0;
	this.percentKept = 0.1f;
	this.percentRandom = 0.1f;
	this.percentNew = 0.1f;
	this.startingPopulation = 200;
	this.singleStrandMutations = 0;
	this.singleBitMutations = 0;
	this.breedsPerPair = 1;
	this.endSimulationParameter = 5;
	calcRealNumbers();
    }
    
    public void modifySetting(int setting, int change) {
	switch(setting) {
	    case 0:
		this.startingPopulation += (POPULATION_TICK*change);
		if (this.startingPopulation <= 0) {
		    this.startingPopulation = POPULATION_MAX;
		} else if (this.startingPopulation > POPULATION_MAX) {
		    this.startingPopulation = POPULATION_TICK;
		}
		break;
	    case 1:
		this.percentKept = ((((this.percentKept*10)+change)%10)/10);
		break;
	    case 2:
		this.percentRandom = ((((this.percentRandom*10)+change)%10)/10);
		break;
	    case 3:
		this.percentNew = ((((this.percentNew*10)+change)%10)/10);
		break;
	    case 4:
		this.breedsPerPair = Integer.max((this.getBreedsPerPair()+change),1);
		break;
	    case 5:
		this.directBreed = (this.directBreed+change)%2;
		break;
	    case 6:
		this.breedBitMutations = (breedBitMutations+change)%2;
		break;
	    case 7:
		this.breedStrandMutations = (breedStrandMutations+change)%2;
		break;
	    case 8:
		this.singleStrandMutations = (this.singleStrandMutations+change)%2;
		break;
	    case 9:
		this.singleBitMutations = (this.singleBitMutations+change)%2;
		break;
	    case 10:
		this.endSimulationParameter = (this.getEndSimulationParameter()+change);
		if (this.getEndSimulationParameter() <= 0) {
		    this.endSimulationParameter = 1;
		}
		break;
	}
	calcRealNumbers();
    }
    
    public Object getSettingValue(int setting) {
	Object obj = 0;
	switch(setting) {
	    case 0:
		obj = this.startingPopulation;
		break;
	    case 1:
		obj = this.percentKept;
		break;
	    case 2:
		obj = this.percentRandom;
		break;
	    case 3:
		obj = this.percentNew;
		break;
	    case 4:
		obj = this.getBreedsPerPair();
		break;
	    case 5:
		obj = this.directBreed == 1;
		break;
	    case 6:
		obj = breedBitMutations == 1;
		break;
	    case 7:
		obj = breedStrandMutations == 1;
		break;
	    case 8:
		obj = this.singleStrandMutations == 1;
		break;
	    case 9:
		obj = this.singleBitMutations == 1;
		break;
	    case 10:
		obj = this.getEndSimulationParameter();
		break;
	}
	return obj;
    }
    
    public String getSettingName(int setting) {
	String name = "";
	switch(setting) {
	    case 0:
		name = "Starting population";
		break;
	    case 1:
		name = "Percentage of best bots kept";
		break;
	    case 2:
		name = "Percentage of random bots kept";
		break;
	    case 3:
		name = "Percentage of new bots";
		break;
	    case 4:
		name = "Breeds per pair";
		break;
	    case 5:
		name = "Direct breeding";
		break;
	    case 6:
		name = "Mutate offspring by bits";
		break;
	    case 7:
		name = "Mutate offspring by strands";
		break;
	    case 8:
		name = "Mutate clone strands";
		break;
	    case 9:
		name = "Mutate clone bits";
		break;
	    case 10:
		name = "End simulation parameter";
		break;
	}
	return name;
    }
    
    public int lastSetting() {
	// starts at 0.
	return 11;
    }
    
    private void calcRealNumbers() {
	this.breedOffspring = (singleBitMutations*2)+(singleStrandMutations*2)+
	    (directBreed* 1)+(breedBitMutations*1)+(breedStrandMutations*1);
		    
	this.numberKept = (int)(startingPopulation * percentKept);
	this.numberRandom = (int)(startingPopulation * percentRandom);
	this.numberNew = (int)(startingPopulation * percentNew);
	
	if (this.numberKept%2 == 1) {
	    this.numberKept++;
	}
	
	if (this.numberRandom%2 == 1) {
	    this.numberRandom++;
	}
	
	int pop = this.numberKept + this.numberRandom + this.numberNew;
	
	this.numberBreeds = (this.numberRandom+this.numberKept)/2;
	
	pop += numberBreeds*breedOffspring*breedsPerPair;
	
	this.realPopulation = pop;
    }

    /**
     * @return the singleBitMutations
     */
    public boolean getSingleBitMutations() {
	return singleBitMutations == 1;
    }

    /**
     * @return the singleStrandMutations
     */
    public boolean getSingleStrandMutations() {
	return singleStrandMutations == 1;
    }

    /**
     * @return the directBreed
     */
    public boolean getDirectBreed() {
	return directBreed == 1;
    }

    /**
     * @return the percentKept
     */
    public float getPercentKept() {
	return percentKept;
    }

    /**
     * @return the percentRandom
     */
    public float getPercentRandom() {
	return percentRandom;
    }

    /**
     * @return the percentNew
     */
    public float getPercentNew() {
	return percentNew;
    }

    /**
     * @return the startingPopulation
     */
    public int getStartingPopulation() {
	return startingPopulation;
    }

    /**
     * @return the realPopulation
     */
    public int getRealPopulation() {
	return realPopulation;
    }

    /**
     * @return the breedBitMutations
     */
    public boolean getBreedBitMutations() {
	return breedBitMutations == 1;
    }

    /**
     * @return the breedStrandMutations
     */
    public boolean getBreedStrandMutations() {
	return breedStrandMutations == 1;
    }

    /**
     * @return the numberKept
     */
    public int getNumberKept() {
	return numberKept;
    }

    /**
     * @return the numberRandom
     */
    public int getNumberRandom() {
	return numberRandom;
    }

    /**
     * @return the numberNew
     */
    public int getNumberNew() {
	return numberNew;
    }

    /**
     * @return the numberBreeds
     */
    public int getNumberBreeds() {
	return numberBreeds;
    }
    
    public int getSurvivors() {
	return numberRandom+numberKept;
    }

    /**
     * @return the breedsPerPair
     */
    public int getBreedsPerPair() {
	return breedsPerPair;
    }

    /**
     * @return the endSimulationParameter
     */
    public int getEndSimulationParameter() {
	return endSimulationParameter;
    }
}
