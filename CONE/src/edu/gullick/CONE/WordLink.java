package edu.gullick.CONE;

/*
 * Class that acts as a Spring in the physics engine, but also as an edge in the visible graph
 */
import java.awt.Color;

import edu.gullick.physics2D.Spring;


public class WordLink extends Spring{

	private Color color = null;
	private int thickness = 0;
	private Double tscore = 0D;
	private boolean isVisible = true;
	
	
	/*
	 * 0 for none
	 * 1 for A --> B
	 * 2 for B --> A
	 * 3 for A <-> B
	 */
	private int direction = 0;
	
	
	
	
	public WordLink(WordNode a, WordNode b, float length, float strength, float damping, Color color, int thickness, int direction, Double tscore) {
		super(a, b, strength,damping, length);
		this.thickness = thickness;
		this.direction = direction;
		this.tscore = tscore;
	}




	public Color getColor() {
		return color;
	}




	public void setColor(Color color) {
		this.color = color;
	}




	public int getThickness() {
		return thickness;
	}




	public void setThickness(int thickness) {
		this.thickness = thickness;
	}




	public int getDirection() {
		return direction;
	}




	public void setDirection(int direction) {
		this.direction = direction;
	}




	public void setTscore(Double tscore) {
		tscore = tscore;
	}




	public Double getTscore() {
		return tscore;
	}




	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}




	public boolean isVisible() {
		return isVisible;
	}
	
	




	

}
