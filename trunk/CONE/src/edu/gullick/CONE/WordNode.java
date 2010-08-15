package edu.gullick.CONE;

/*
 * Word node class that acts as both a particle in the physics engine, but also a node in the visible graph. 
 */

import java.awt.Color;
import java.util.HashMap;
import java.util.Vector;

import edu.gullick.physics2D.Particle;

public class WordNode extends Particle{
	
	private int diameter = 0;
	
	private Vector<WordAttraction> attractions = new Vector<WordAttraction>();
	private Vector<WordLink> links = new Vector<WordLink>();
	private Vector<WordNode> neighbours = new Vector<WordNode>();
	
	private Vector<LinkInformation> totalInfoFromWordIndex = null;
	
	private boolean nodeOpen = false;
	private boolean userAdded = false;
	private String word = "";
	private double frequency = 0D;
	private Color color = new Color(0);
	private HashMap<Object, Object> attributes = new HashMap<Object, Object>();
	private boolean isSelected = false;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isHoveredOver() {
		return isHoveredOver;
	}

	public void setHoveredOver(boolean isHoveredOver) {
		this.isHoveredOver = isHoveredOver;
	}

	public boolean isHoveredOver = false;
		
	public WordNode(float x, float y, float w, Color color, String word, double frequency, int diameter) {
		super(w);
		this.position.setX(x);
		this.position.setY(y);
		this.color = color;
		this.word = word;
		this.frequency = frequency;
		this.diameter = diameter;
	 attractions = new Vector<WordAttraction>();
	 links = new Vector<WordLink>();
 neighbours = new Vector<WordNode>();
	
	}
	
	public void setAttribute(Object key, Object value){
		attributes.put(key,value);
	}
	
	public boolean removeAttribute(Object key){
		return (Boolean) attributes.remove(key);
	}
	
	public Object getAttribute(Object key){
		return attributes.get(key);
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int radius) {
		this.diameter = radius;
	}

	public boolean isNodeOpen() {
		return nodeOpen;
	}

	public void setNodeOpen(boolean nodeOpen) {
		this.nodeOpen = nodeOpen;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public HashMap<Object, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<Object, Object> attributes) {
		this.attributes = attributes;
	}

	public Vector<WordAttraction> getAttractions() {
		return attractions;
	}
	public void setAttractions(Vector<WordAttraction> attractions) {
		this.attractions = attractions;
	}

	public boolean getUserAdded(){
		return userAdded;
	}
	
	public void setUserAdded(boolean x){
		userAdded = x;
	}



	public void setLinks(Vector<WordLink> links) {
		this.links = links;
	}

	public Vector<WordLink> getLinks() {
		return links;
	}

	public void setNeighbours(Vector<WordNode> neighbours) {
		this.neighbours = neighbours;
	}

	public Vector<WordNode> getNeighbours() {
		return neighbours;
	}
	
	
	public void addAttraction(WordAttraction x){
		attractions.add(x);
	}
	
	public boolean removeAttraction(WordAttraction x){
		return attractions.remove(x);
	}
	
	public void addLink(WordLink x){
		links.add(x);
	}
	
	public boolean removeLink(WordLink x){
		return links.remove(x);
	}
	
	public void addNeighbour(WordNode x){
		neighbours.add(x);
	}
	
	public boolean removeNeighbour(WordNode x){
		return neighbours.remove(x);
	}
	
	public WordLink getSpringTo(WordNode x){
		for(int q = 0; q < links.size(); q++){
			WordLink temp = links.get(q);
			if(temp.getOneEnd() == x || temp.getTheOtherEnd() == x ){
				return temp;
			}
		}
		return null;
	}

	public void setTotalInfoFromWordIndex(Vector<LinkInformation> totalInfoFromWOrdIndex) {
		this.totalInfoFromWordIndex = totalInfoFromWOrdIndex;
	}

	public Vector<LinkInformation> getTotalInfoFromWordIndex() {
		return totalInfoFromWordIndex;
	}


}
