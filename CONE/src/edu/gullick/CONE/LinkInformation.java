package edu.gullick.CONE;


/**
 * The Class LinkInformation - holds all of the information about a link between two words.
 */
public class LinkInformation implements Comparable{
	
	/** The word1. */
	public String word1 = "";
	
	/** The word2. */
	public String word2 = "";
	
	/** The affinity. */
	public double affinity = 0.0D;
	
	/** The tscore. */
	public double tscore = 0.0D;
	
	/** The frequency1. */
	public double frequency1 = 0.0D;
	
	/** The frequency2. */
	public double frequency2 = 0.0D;
	
	/** The frequency of both words together. */
	public double frequencyBoth = 0.0D;




	/**
	 * Instantiates a new link information.
	 *
	 * @param word1 the word1
	 * @param word2 the word2
	 * @param affinity the affinity
	 * @param tscore the tscore
	 * @param frequency1 the frequency of Word1
	 * @param frequency2 the frequency of Word2
	 */
	public LinkInformation(String word1, String word2, double affinity,
			double tscore, Double frequency1, Double frequency2, Double frequencyBoth ) {
		this.word1 = word1;
		this.word2 = word2;
		this.affinity = affinity;
		this.tscore = tscore;
		this.frequency1 = frequency1;
		this.frequency2 = frequency2;
		this.frequencyBoth = frequencyBoth;
	}

	
	
	
	/* ******************************
	 * GETTERS & SETTERS PAST HERE!
	 *******************************/
	
	/**
	 * Gets the word1.
	 *
	 * @return the word1
	 */
	public String getWord1() {
		return word1;
	}

	/**
	 * Sets the word1.
	 *
	 * @param word1 the new word1
	 */
	public void setWord1(String word1) {
		this.word1 = word1;
	}

	/**
	 * Gets the word2.
	 *
	 * @return the word2
	 */
	public String getWord2() {
		return word2;
	}

	/**
	 * Sets the word2.
	 *
	 * @param word2 the new word2
	 */
	public void setWord2(String word2) {
		this.word2 = word2;
	}

	/**
	 * Gets the affinity.
	 *
	 * @return the affinity
	 */
	public double getAffinity() {
		return affinity;
	}

	/**
	 * Sets the affinity.
	 *
	 * @param affinity the new affinity
	 */
	public void setAffinity(double affinity) {
		this.affinity = affinity;
	}

	/**
	 * Gets the tscore.
	 *
	 * @return the tscore
	 */
	public double getTscore() {
		return tscore;
	}

	/**
	 * Sets the tscore.
	 *
	 * @param tscore the new tscore
	 */
	public void setTscore(double tscore) {
		this.tscore = tscore;
	}

	public boolean setFrequencyOfWord(String x, Double y){
		if(word1.equals(x)){
			frequency1 = y;
			return true;
		}else if(word2.equals(x)){
			frequency2 = y;
			return true;
		}else{
			return false;
		}
	}
	
	public String getTheOtherWord(String x){
		if(x.equals(word1)){
			return word2;
		}else if(x.equals(word2)){
			return word1;
		}else{
			return null;
		}
	}
	
	public Double getFrequencyOfWord(String x){
		if(word1.equals(x)){
			return this.frequency1;
		}else if(word2.equals(x)){
			return this.frequency1;
		}else{
			return null;
		}
	}
	

	public double getFrequencyBoth() {
		return frequencyBoth;
	}




	public void setFrequencyBoth(double frequencyBoth) {
		this.frequencyBoth = frequencyBoth;
	}




	public int compareTo(Object o) {
		  if (this.getAffinity() == ((LinkInformation) o).getAffinity())
	            return 0;
	        else if (this.getAffinity() > ((LinkInformation) o).getAffinity())
	            return 1;
	        else
	            return -1;
	}

	
}
