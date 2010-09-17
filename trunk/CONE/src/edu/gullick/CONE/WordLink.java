package edu.gullick.CONE;

import edu.gullick.physics2D.Spring;

/**
 * Class that acts as a Spring in the physics engine, but also as an edge in the visible graph
 * @extends edu.gullick.physics2D.Spring
 */

public class WordLink extends Spring {
	/** The thickness. */
	private int thickness = 0;

	/**
	 * Instantiates a new word link.
	 *
	 * @param a the first WordNode (oneEnd)
	 * @param b the second WordNode (theOtherEnd)
	 * @param length the length
	 * @param strength the strength
	 * @param damping the damping
	 * @param thickness the thickness
	 */
	public WordLink(WordNode a, WordNode b, float length, float strength, float damping, int thickness) {
		super(a, b, strength, damping, length);
		this.thickness = thickness;

	}

	
	/**
	 * 
	 * @param s a string to search
	 * @return the Node on the Edge representing the given string
	 */
	public WordNode getEndRepresentingWord( String s){
		if(  ((WordNode)getOneEnd()).getWord().equals(s)    ){
			return (WordNode)getOneEnd();
		}else{
			return (WordNode) getTheOtherEnd();
		}
	}
	
	/**
	 * 
	 * @param s a string to search
	 * @return the opposite Node on the Edge representing the given string
	 */
	public WordNode getEndNotRepresentingWord( String s){
		if(!((WordNode)getOneEnd()).getWord().equals(s)    ){
			return (WordNode)getOneEnd();
		}else{
			return (WordNode) getTheOtherEnd();
		}
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
}
