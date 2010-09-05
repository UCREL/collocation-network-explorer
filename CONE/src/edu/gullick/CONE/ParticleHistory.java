/*
 * 
 */
package edu.gullick.CONE;

/**
 * The Class ParticleHistory - used to record the position of a certain particle .
 */
public class ParticleHistory {
	
	/** The word. */
	String word = "";
	
	/** The x position. */
	int x = 0;
	
	/** The y position. */
	int y = 0;

	/**
	 * Instantiates a new particle history.
	 *
	 * @param word the word of the particle
	 * @param x the x position of the particle
	 * @param y the y position of the particle
	 */
	public ParticleHistory(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
	}

	
	
	/* ***********************************
	 *  GETTERS & SETTERS PAST HERE!
	 ************************************/
	
	
	
	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Sets the word.
	 *
	 * @param word the new word
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

}
