package edu.gullick.CONE;

import edu.gullick.physics2D.Spring;

/**
 * Class that acts as a Spring in the physics engine, but also as an edge in the visible graph
 * @extends edu.gullick.physics2D.Spring
 */

public class WordLink extends Spring {

	public enum DIRECTION {A_TO_B,B_TO_A,BIDIRECTIONAL}
	
	/** The thickness. */
	private int thickness = 0;
	
	/** The is visible. */
	private boolean isVisible = true;


	/** The direction. */
	private int direction = 0;

	
	/**
	 * Instantiates a new word link.
	 *
	 * @param a the first WordNode (oneEnd)
	 * @param b the second WordNode (theOtherEnd)
	 * @param length the length
	 * @param strength the strength
	 * @param damping the damping
	 * @param thickness the thickness
	 * @param direction the direction
	 */
	public WordLink(WordNode a, WordNode b, float length, float strength, float damping, int thickness, int direction) {
		super(a, b, strength, damping, length);
		this.thickness = thickness;
		this.direction = direction;
	}

	
	/*********************************
	 *  GETTERS AND SETTERS BELOW THIS
	 *********************************/
	

	/**
	 * Gets the thickness.
	 *
	 * @return the thickness
	 */
	public int getThickness() {
		return thickness;
	}

	/**
	 * Sets the thickness.
	 *
	 * @param thickness the new thickness
	 */
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the direction.
	 *
	 * @param direction the new direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}


	/**
	 * Sets the visible.
	 *
	 * @param isVisible the new visible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible() {
		return isVisible;
	}

}
