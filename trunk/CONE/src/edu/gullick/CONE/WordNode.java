package edu.gullick.CONE;
import java.util.HashMap;
import java.util.Vector;
import edu.gullick.physics2D.Particle;

/**
 * Word node class that acts as both a particle in the physics engine, but also a node in the visible graph. 
 * It has an associated word, frequency and keeps a record of all links, neighbours and forces acting upon it.
 * @extends edu.gullick.physics2D.Particle
 */

public class WordNode extends Particle {

	/** The attractions acting upon the particle. */
	private Vector<WordAttraction> attractions = new Vector<WordAttraction>();
	
	/** The links that are acting upon the particle */
	private Vector<WordLink> links = new Vector<WordLink>();
	
	/** The neighbours next to the particle */
	private Vector<WordNode> neighbours = new Vector<WordNode>();

	/** boolean representing the open status of the node */
	private boolean nodeOpen = false;

	/** The word that the node is representing */
	private String word = "";
	
	/** The frequency of the word in the corpus */
	private double frequency = 0D;
	
	/** boolean representing whether the node is currently selected */
	private boolean isSelected = false;
	
	/** boolean representing whether the node is in the current corpus */
	private boolean isInCurrentCorpus = true;

	
	
	
	
	/**
	 * Instantiates a new word node.
	 *
	 * @param x the x
	 * @param y the y
	 * @param w the w
	 * @param word the word
	 * @param frequency the frequency
	 */
	public WordNode(float x, float y, float w, String word,	double frequency) {
		super(w);
		this.position.setX(x);
		this.position.setY(y);
		this.word = word;
		this.frequency = frequency;
		attractions = new Vector<WordAttraction>();
		links = new Vector<WordLink>();
		neighbours = new Vector<WordNode>();

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
	public Vector<WordAttraction> getAttractions() {
		return attractions;
	}

	/**
	 * Sets the attractions.
	 *
	 * @param attractions the new attractions
	 */
	public void setAttractions(Vector<WordAttraction> attractions) {
		this.attractions = attractions;
	}


	/**
	 * Sets the links.
	 *
	 * @param links the new links
	 */
	public void setLinks(Vector<WordLink> links) {
		this.links = links;
	}

	/**
	 * Gets the links.
	 *
	 * @return the links
	 */
	public Vector<WordLink> getLinks() {
		return links;
	}

	/**
	 * Sets the neighbours.
	 *
	 * @param neighbours the new neighbours
	 */
	public void setNeighbours(Vector<WordNode> neighbours) {
		this.neighbours = neighbours;
	}

	/**
	 * Gets the neighbours.
	 *
	 * @return the neighbours
	 */
	public Vector<WordNode> getNeighbours() {
		return neighbours;
	}

	/**
	 * Adds the attraction.
	 *
	 * @param x the x
	 */
	public void addAttraction(WordAttraction x) {
		attractions.add(x);
	}

	/**
	 * Removes the attraction.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeAttraction(WordAttraction x) {
		return attractions.remove(x);
	}

	/**
	 * Adds the link.
	 *
	 * @param x the x
	 */
	public void addLink(WordLink x) {
		links.add(x);
	}

	/**
	 * Removes the link.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeLink(WordLink x) {
		return links.remove(x);
	}

	/**
	 * Adds the neighbour.
	 *
	 * @param x the x
	 */
	public void addNeighbour(WordNode x) {
		neighbours.add(x);
	}

	/**
	 * Removes the neighbour.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean removeNeighbour(WordNode x) {
		return neighbours.remove(x);
	}

	/**
	 * Gets the spring to.
	 *
	 * @param x the x
	 * @return the spring to
	 */
	public WordLink getSpringTo(WordNode x) {
		for (int q = 0; q < links.size(); q++) {
			WordLink temp = links.get(q);
			if (temp.getOneEnd() == x || temp.getTheOtherEnd() == x) {
				return temp;
			}
		}
		return null;
	}


	/**
	 * Sets the in current corpus.
	 *
	 * @param isInCurrentCorpus the new in current corpus
	 */
	public void setInCurrentCorpus(boolean isInCurrentCorpus) {
		this.isInCurrentCorpus = isInCurrentCorpus;
	}

	/**
	 * Checks if is in current corpus.
	 *
	 * @return true, if is in current corpus
	 */
	public boolean isInCurrentCorpus() {
		return isInCurrentCorpus;
	}

}
 