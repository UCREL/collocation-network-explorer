package edu.gullick.CONE;
import java.util.LinkedHashMap;

import edu.gullick.physics2D.Particle;

/**
 * Word node class that acts as both a particle in the physics engine, but also a node in the visible graph. 
 * It has an associated word, frequency and keeps a record of all links, neighbours and forces acting upon it.
 * @extends edu.gullick.physics2D.Particle
 */

public class WordNode extends Particle {

	/** The attractions acting upon the particle. */
	private LinkedHashMap<String,WordAttraction> attractions = new LinkedHashMap<String,WordAttraction>();
	
	/** The links that are acting upon the particle */
	private LinkedHashMap<String, WordLink> links = new LinkedHashMap<String, WordLink>();
	
	/** The neighbours next to the particle */
	private LinkedHashMap<String,WordNode> neighbours = new LinkedHashMap<String,WordNode>();

	/** boolean representing the open status of the node */
	private boolean nodeOpen = false;

	/** The word that the node is representing */
	private String word = "";
	
	/** The frequency of the word in the corpus */
	private double frequency = 0D;
	
	/** boolean representing whether the node is currently selected */
	private boolean isSelected = false;
	
	
	
	
	/**
	 * Instantiates a new word node.
	 *
	 * @param x the x position
	 * @param y the y position
	 * @param w the w weight of the node
	 * @param word the word the the node represents
	 * @param frequency the frequency the frequency of the Node
	 */
	public WordNode(float x, float y, float w, String word,	double frequency) {
		super(w);
		this.position.setX(x);
		this.position.setY(y);
		this.word = word;
		this.frequency = frequency;
		attractions = new LinkedHashMap<String,WordAttraction>();
		links = new LinkedHashMap<String,WordLink>();
		neighbours = new LinkedHashMap<String, WordNode>();

	}

	
	
	/* **************************************************
	 *  GETTERS AND SETTERS BELOW THIS LINE 
	 * **************************************************/ 

	
	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param isSelected the new selected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * Checks if is hovered over.
	 *
	 * @return true, if is hovered over
	 */
	public boolean isHoveredOver() {
		return isHoveredOver;
	}

	/**
	 * Sets the hovered over.
	 *
	 * @param isHoveredOver the new hovered over
	 */
	public void setHoveredOver(boolean isHoveredOver) {
		this.isHoveredOver = isHoveredOver;
	}

	/** The is hovered over. */
	public boolean isHoveredOver = false;



	/**
	 * Checks if is node open.
	 *
	 * @return true, if is node open
	 */
	public boolean isNodeOpen() {
		return nodeOpen;
	}

	/**
	 * Sets the node open.
	 *
	 * @param nodeOpen the new node open
	 */
	public void setNodeOpen(boolean nodeOpen) {
		this.nodeOpen = nodeOpen;
	}

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
	 * Gets the frequency.
	 *
	 * @return the frequency
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * Sets the frequency.
	 *
	 * @param frequency the new frequency
	 */
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}




	/**
	 * Gets the attractions.
	 *
	 * @return the attractions
	 */
	public LinkedHashMap<String,WordAttraction> getAttractions() {
		return attractions;
	}

	/**
	 * Sets the attractions.
	 *
	 * @param attractions the new attractions
	 */
	public void setAttractions(LinkedHashMap<String,WordAttraction> attractions) {
		this.attractions = attractions;
	}


	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(LinkedHashMap<String,WordLink> links) {
		this.links = links;
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public LinkedHashMap<String,WordLink> getLinks() {
		return links;
	}

	/**
	 * Sets the neighbours.
	 *
	 * @param neighbours the new neighbours
	 */
	public void setNeighbours(LinkedHashMap<String,WordNode> neighbours) {
		this.neighbours = neighbours;
	}

	/**
	 * Gets the neighbours.
	 *
	 * @return the neighbours
	 */
	public LinkedHashMap<String,WordNode> getNeighbours() {
		return neighbours;
	}

	/**
	 * Adds the attraction.
	 *
	 * @param x the x
	 */
	public void addAttraction(WordAttraction x) {
		if(x.getOneEnd() == this){
			attractions.put(((WordNode)x.getTheOtherEnd()).getWord(),x);
		}else{
			attractions.put(((WordNode)x.getOneEnd()).getWord(),x);
		}
	}

	/**
	 * Removes the attraction.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeAttraction(WordAttraction x) {
		if(x.getOneEnd() == this){
			if(attractions.remove(((WordNode)x.getTheOtherEnd()).getWord()) != null){
				return true;
			}else{
				return false;
			}
		}else{
			if(attractions.remove(((WordNode)x.getOneEnd()).getWord()) != null){
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * Removes the attraction.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeAttraction(String x) {
	
			if(attractions.remove(x) != null){
				return true;
			}else{
				return false;
			}

	}

	
	

	/**
	 * Adds the link.
	 *
	 * @param x the x
	 */
	public void addLink(WordLink x) {
		if(x.getOneEnd() == this){
			links.put(((WordNode)x.getTheOtherEnd()).getWord(),x);
		}else{
			links.put(((WordNode)x.getOneEnd()).getWord(),x);
		}
	}

	/**
	 * Removes the link.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeLink(WordLink x) {
		if(x.getOneEnd() == this){
			if(links.remove(((WordNode)x.getTheOtherEnd()).getWord()) != null){
				return true;
			}else{
				return false;
			}
		}else{
			if(links.remove(((WordNode)x.getOneEnd()).getWord()) != null){
				return true;
			}else{
				return false;
			}
		}
	}
	
	
	/**
	 * Removes the link.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeLink(String x) {
		if(links.remove(x) != null){
				return true;
			}else{
				return false;
		}
	}

	

	/**
	 * Adds the neighbour.
	 *
	 * @param x the x
	 */
	public void addNeighbour(WordNode x) {
			neighbours.put(((WordNode)x).getWord(),x);
	}

	/**
	 * Removes the neighbour.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeNeighbour(WordNode x) {
		if(neighbours.remove(x.getWord()) != null){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * Removes the neighbour.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeNeighbour(String x) {
		if(neighbours.remove(x) != null){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	
	

	/**
	 * Gets the spring to.
	 *
	 * @param x the x
	 * @return the spring to
	 */
	public WordLink getSpringTo(WordNode x) {
		return links.get(x.getWord());
	}


	
	

}
 