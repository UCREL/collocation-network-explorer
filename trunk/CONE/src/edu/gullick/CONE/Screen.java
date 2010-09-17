
package edu.gullick.CONE;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * The Class Screen - Uses the Processing core.jar to create an Applet that is drawable with Processing commands .
 */
public class Screen extends PApplet {

	/* CONSTANTS */
	
	/* Area that a mouse over selects by */
	/** The Constant SELECT_AREA. */
	static final int SELECT_AREA = 30;

	/* Background Colors */
	/** The Constant BACK_R. */
	static final int BACK_R = 0xD0;
	/** The Constant BACK_G. */
	static final int BACK_G = 0xEA;
	/** The Constant BACK_B. */
	static final int BACK_B = 0xFF;

	
	
	/* GLOBAL VARIABLES */
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/* Font used throughout */
	/** The font a. */
	private PFont fontA = null;

	/* Reference to the physics object - used only for drawing */
	/** The physics. */
	private Physics physics = null;

	/* level of zoom */
	/** The zoom level. */
	private double zoomLevel = 1.0D;

	/* Offset due to user dragging system */
	/** The Xoffset. */
	private int Xoffset = 0;
	
	/** The Yoffset. */
	private int Yoffset = 0;

	/*
	 * Coordinates to work out whether shapes are on or off the screen (and
	 * therefore whether necessary to draw)
	 */
	/** The top left x. */
	private int topLeftX = 0;
	
	/** The top left y. */
	private int topLeftY = 0;
	
	/** The bottom right x. */
	private int bottomRightX = 0;
	
	/** The bottom right y. */
	private int bottomRightY = 0;

	/* Booleans for drawing options */
	/** The draw nodes. */
	private boolean drawNodes = false;
	
	/** The draw edges. */
	private boolean drawEdges = true;
	
	/** The draw text. */
	private boolean drawText = true;
	
	/** The draw debug. */
	private boolean drawDebug = false;
	
	/** The draw forces. */
	private boolean drawForces = false;
	
	/** The smooth font. */
	private boolean smoothFont = false;
	
	/** The smooth animation. */
	private boolean smoothAnimation = false;

	/** The follow pointer with node. */
	private boolean followPointerWithNode = false;
	
	/** The word to follow pointer. */
	private String wordToFollowPointer = "";
	
	/** The draw node info. */
	private boolean drawNodeInfo = true;

	/** The xpos. */
	private int xpos = 0;
	
	/** The ypos. */
	private int ypos = 0;

	/** The word info. */
	private String wordInfo = "";

	
	
	/**
	 * Instantiates a new screen.
	 *
	 * @param physics the physics
	 */
	public Screen(Physics physics) {
		this.physics = physics;
		setup();
	}
	

	
	
	public void setup(){
		frameRate(15F);
		fontA = createFont("FFScala-32.vlw",36);
	}
	
	/** 
	 * draw() method. Pressing mthod to redraw the Applet (where the real work happens)
	 *
	 * Represents everything in the physics object with a graphical representation
	 * 
	 * @see processing.core.PApplet#draw()
	 * 
	 *
	 */
	public void draw() {

		try {
			//setting screensize variables
			topLeftX = (int) (0 - Xoffset) - 10;
			topLeftY = (int) (0 - Yoffset) - 10;
			bottomRightX = (int) ((width / zoomLevel) - Xoffset) + 10;
			bottomRightY = (int) ((height / zoomLevel) - Yoffset) + 10;

			//setting the background color
			background(BACK_R, BACK_G, BACK_B);

			//pushing the current matrix onto the stack
			pushMatrix();

			//scale up to the current zoomLevel
			scale((float) zoomLevel);
			
			
			//move to the current offsets
			translate((float) Xoffset, (float) Yoffset);
			
			//if placing a new node, draw the ghost node around tte mouse
			if (followPointerWithNode) {
				stroke(0, 0, 0, 20);
				fill(0, 0, 0, 20);
				int x = (int) ((mouseX / zoomLevel) - Xoffset);
				int y = (int) ((mouseY / zoomLevel) - Yoffset);
				textFont(fontA, 28);
				ellipse(x, y, 20, 20);
				text(wordToFollowPointer, x, y);
			}
			
			//if drawing edges, draw lines to represent them
			if (drawEdges) {
				stroke(0, 0, 0, 20);
				for (int a = 0; a < physics.getSprings().size(); a++) {
					WordLink wl = (WordLink) physics.getSprings().get(a);
					if (isOnScreen(wl)) {
						if (smoothAnimation)
							smooth();
						strokeWeight(wl.getThickness());
						//float[] mouse = { realX, realY };

						float[] position1 = { wl.getOneEnd().position().x(),
								wl.getOneEnd().position().y() };
						//float[] position1 = scaleUP(mouse, position1,10);

						float[] position2 = {
								wl.getTheOtherEnd().position().x(),
								wl.getTheOtherEnd().position().y() };
					//	float[] position2 = scaleUP(mouse, position2,10);

						line(position1[0], position1[1], position2[0], position2[1]);
						if (smoothAnimation)
							noSmooth();
					}
				}

			}
			
			//if drawing forces, draw them
			if (drawForces) {
				stroke(100);
				strokeWeight(1);
				for (int a = 0; a < physics.getAttractions().size(); a++) {
					WordAttraction wa = (WordAttraction) physics
							.getAttractions().get(a);
					if (isOnScreen(wa)) {
						if (smoothAnimation)
							smooth();

						//float[] mouse = { realX, realY };

						float[] position1 = { wa.getOneEnd().position().x(),
								wa.getOneEnd().position().y() };
						//float[] position1 = scaleUP(mouse, position1,10);

						float[] position2 = {
								wa.getTheOtherEnd().position().x(),
								wa.getTheOtherEnd().position().y() };
						//float[] position2 = scaleUP(mouse, position2,10);

						line(position1[0], position1[1], position2[0], position2[1]);

						if (smoothAnimation)
							noSmooth();
					}
				}
			}

			
			//for all particles (wordnodes)
			for (int a = 0; a < physics.getParticles().size(); a++) {
				WordNode wn = (WordNode) physics.getParticles().get(a);
				boolean mouseOver = false;
			
				// if it is on screen
				if (isOnScreen(wn)) {
					fill(0,0,255, 20);
					stroke(0, 0, 0, 40);
					mouseOver = mouseOver(wn);
			
					//if mouse is over it
					if (mouseOver) {

						stroke(200, 0, 0, 60);
						LinkedHashMap<String,WordLink> links = wn.getLinks();
						Object[] springs = links.values().toArray();
						for (Object  entry : springs) {
							WordLink wl = (WordLink) entry;
							
							if (smoothAnimation)
								smooth();
							strokeWeight(wl.getThickness());
						//	float[] mouse = { realX, realY };

							float[] position1 = {
									wl.getOneEnd().position().x(),
									wl.getOneEnd().position().y() };
							//float[] position1 = scaleUP(mouse, position1,10);

							float[] position2 = {
									wl.getTheOtherEnd().position().x(),
									wl.getTheOtherEnd().position().y() };
							//float[] position2 = scaleUP(mouse, position2,10);

							line(position1[0], position1[1], position2[0], position2[1]);
							if (smoothAnimation)
								noSmooth();
						}
					}
					
					//if drawing nodes, draw them
					if (drawNodes) {
						if (smoothAnimation)
							smooth();
						int size = 10;

						if (wn.isNodeOpen()) {
							fill(255, 0, 0, 40);
						}

						//float[] mouse = { realX, realY };
						float[] position = { wn.position().x(),
								wn.position().y() };
					//	float[] position = scaleUP(mouse, position, size);

						ellipse(position[0], position[1], size, size);

						if (smoothAnimation) {
							noSmooth();
						}
					}
					
					//if drawing text, draw it
					if (drawText) {
						int textFontSize = 0;

						Double freq = wn.getFrequency();

						//TODO: something more elegant here..
						freq = freq * 50000;

						if (freq < 1) {
							textFontSize = 16;
						} else if (freq < 25) {
							textFontSize = 20;
						} else if (freq < 50) {
							textFontSize = 26;
						} else if (freq < 75) {
							textFontSize = 32;
						} else if (freq < 100) {
							textFontSize = 38;
						} else {
							textFontSize = 44;
						}

						if (mouseOver(wn)) {
							fill(0, 255);
							textFontSize *= 2;
							;
						} else {
							fill(0, 120);
						}

						if (wn.isNodeOpen()) {
							fill(255, 0, 0, 255);
							textFontSize *= 1.1;
						}

						if (wn.isSelected()) {
							fill(0, 255, 0, 255);
						}

						textFont(fontA, textFontSize);
						if (smoothFont)
							smooth();
						//float[] mouse = { realX, realY };

						float[] position = { wn.position().x(),
								wn.position().y() };
						//float[] position = scaleUP(mouse, position,10);
						
						
						text(wn.getWord(), position[0]
								- (textWidth(wn.getWord()) / 2), position[1]);
						if (smoothFont)
							noSmooth();

						if (wn.getFrequency() <= 0) {
							stroke(255, 0, 0, 128);
							strokeWeight(5);
							line(position[0] - textFontSize, position[1]
									- textFontSize, position[0] + textFontSize,
									position[1] + textFontSize);
							line(position[0] - textFontSize, position[1]
									+ textFontSize, position[0] + textFontSize,
									position[1] - textFontSize);
						}

					}

				}
			}

			//restore the matrix from the stack
			popMatrix();
			
			//draw debug (if selected)
			if (drawDebug) {
				fill(0x00, 0x00, 0x00);
				textFont(fontA, 14);
				if (smoothFont)
					smooth();
				text("FPS:" + (int) frameRate, 2, 15);
				text("Number of Nodes: " + (int) physics.getParticles().size(),
						2, 30);
				text("Number of Edges: " + (int) physics.getSprings().size(),
						2, 45);
				text("Number of Forces: "
						+ (int) physics.getAttractions().size(), 2, 60);
				text("Zoom Level: " + zoomLevel, 2, 75);
				text("MouseX: " + mouseX, 2, 90);
				text("MouseY " + mouseY, 2, 105);
				text("Xoffset: " + Xoffset, 2, 120);
				text("Yoffset " + Yoffset, 2, 135);
				if (smoothFont)
					noSmooth();
			}

			//draw nodeInfo if selected
			if (drawNodeInfo) {
				fill(0x00, 0x00, 0x00);
				textFont(fontA, 14);
				if (smoothFont)
					smooth();
				text(wordInfo, width - textWidth(wordInfo), 15);
				if (smoothFont)
					noSmooth();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	/* NO LONGER USED - THE LENSE EFFECT MADE IT HARD TO CLICK NODES. CODE KEPT FOR FUTURE USE.
	 * Scale up. Is used to scale/move things on the applet depending on mouse position.Results in the lense effect.
	 * -- Thanks to Francois Taiani for this code!
	 * @param mouse the mouse
	 * @param position the position
	 * @param diameter the diameter
	 * @return the float[]
	
	 
	public float[] scaleUP(float[] mouse, float[] position, int diameter) {
		float[] posToMouse = { position[0] - mouse[0], position[1] - mouse[1] };
		float d = sqrt(posToMouse[0] * posToMouse[0] + posToMouse[1]
				* posToMouse[1]);
		float scale = 4 * exp(-d / (diameter * 3)) + 1; // even more localised:
		float[] result = { mouse[0] + posToMouse[0] * scale,
				mouse[1] + posToMouse[1] * scale, scale };
		return result;
	}
	*/

	/**
	 * Checks if a given WordNode is on screen
	 *
	 * @param n the WordNode
	 * @return true, if is on screen
	 */
	public boolean isOnScreen(WordNode n) {
		if (n.position().x() > topLeftX && n.position().x() < bottomRightX
				&& n.position().y() > topLeftY
				&& n.position().y() < bottomRightY) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a given WordLink is on screen.
	 * Currently broken - always returns true
	 *
	 * @param n the WordLink
	 * @return true, if is on screen
	 */
	public boolean isOnScreen(WordLink n) {
		return true;
	}

	/**
	 * Checks if a given WordAttraction is on screen.
	 * Currently broken - always returns true
	 *
	 * @param n the WordAttraction
	 * @return true, if is on screen
	 */
	public boolean isOnScreen(WordAttraction n) {
		return true;
	}

	/**
	 * Checks if the mouse is over a particular WordNode.
	 *
	 * @param n the WordNode
	 * @return true, if successful
	 */
	public boolean mouseOver(WordNode n) {
		
		//calculate the mouse position in respect to the whole canvas (not jsut what is on screen)
		int realX = (int) ((mouseX / zoomLevel) - Xoffset);
		int realY = (int) ((mouseY / zoomLevel) - Yoffset);
		//float[] mouse = { realX, realY };
		float[] position = { n.position().x(), n.position().y() };
		
		//also take into account the scaling of the items
		//float[] position = scaleUP(mouse, position, 10);
		
		
		if (position[0] < (realX + SELECT_AREA)
				&& position[0] > (realX - SELECT_AREA)
				&& position[1] < (realY + SELECT_AREA)
				&& position[1] > (realY - SELECT_AREA)){
			return true;
		}
		return false;
	}

	
	
	
	/**
	 * Centers the screen on a given WordNode
	 *
	 * @param tempNode the temp node
	 */
	public void center(WordNode tempNode) {
		/* This Function centers the display on a certain node */ 
		setXoffset((int) (((width / zoomLevel) - tempNode.position().x()) - ((width / zoomLevel) / 2)));
		setYoffset((int) (((height / zoomLevel) - tempNode.position().y()) - ((height / zoomLevel) / 2)));
	}
	
	
	
	public int getCenterX(){
		return (int) 	((width/2 / zoomLevel) - Xoffset);

		
	}
	
	public int getCenterY(){
		return (int) 	((height/2 / zoomLevel) - Xoffset);
	}
	
	
	
	public int getPhysicsWidth(){
		return (int) 	((width/ zoomLevel));
	}
	
	public int getPhysicsHeight(){
		return (int) 	((height/ zoomLevel));
	}
	
	/* ************************************
	 * GETTERS AND SETTERS AFTER THIS!
	 *************************************/
	
	
	

	/**
	 * Sets the display nodes.
	 *
	 * @param x the new display nodes
	 */
	public void setDisplayNodes(boolean x) {
		drawNodes = x;
	}

	/**
	 * Sets the display edges.
	 *
	 * @param x the new display edges
	 */
	public void setDisplayEdges(boolean x) {
		drawEdges = x;
	}

	/**
	 * Sets the display text.
	 *
	 * @param x the new display text
	 */
	public void setDisplayText(boolean x) {
		drawText = x;
	}

	/**
	 * Sets the display debug.
	 *
	 * @param x the new display debug
	 */
	public void setDisplayDebug(boolean x) {
		drawDebug = x;
	}

	/**
	 * Sets the display forces.
	 *
	 * @param x the new display forces
	 */
	public void setDisplayForces(boolean x) {
		drawForces = x;
	}

	/**
	 * Sets the x offset.
	 *
	 * @param newOffset the new x offset
	 */
	public void setXOffset(int newOffset) {
		Xoffset = newOffset;
	}

	/**
	 * Sets the y offset.
	 *
	 * @param newOffset the new y offset
	 */
	public void setYOffset(int newOffset) {
		Yoffset = newOffset;
	}

	/**
	 * Reset zoom.
	 */
	public void resetZoom() {
		zoomLevel = 1.0F;
	}

	/**
	 * Zoom in.
	 */
	public void zoomIn() {
		zoomLevel *= 1.2;
	}

	/**
	 * Zoom out.
	 */
	public void zoomOut() {
		zoomLevel /= 1.2;
	}

	/**
	 * Checks if is smooth font.
	 *
	 * @return true, if is smooth font
	 */
	public boolean isSmoothFont() {
		return smoothFont;
	}

	/**
	 * Sets the smooth font.
	 *
	 * @param smoothFont the new smooth font
	 */
	public void setSmoothFont(boolean smoothFont) {
		this.smoothFont = smoothFont;
	}

	/**
	 * Checks if is smooth animation.
	 *
	 * @return true, if is smooth animation
	 */
	public boolean isSmoothAnimation() {
		return smoothAnimation;
	}

	/**
	 * Sets the smooth animation.
	 *
	 * @param smoothAnimation the new smooth animation
	 */
	public void setSmoothAnimation(boolean smoothAnimation) {
		this.smoothAnimation = smoothAnimation;
	}

	/**
	 * Sets the follow pointer.
	 *
	 * @param x the x
	 * @param word the word
	 */
	public void setFollowPointer(boolean x, String word) {
		this.followPointerWithNode = x;
		this.wordToFollowPointer = word;
	}

	/**
	 * Gets the font a.
	 *
	 * @return the font a
	 */
	public PFont getFontA() {
		return fontA;
	}

	/**
	 * Sets the font a.
	 *
	 * @param fontA the new font a
	 */
	public void setFontA(PFont fontA) {
		this.fontA = fontA;
	}

	/**
	 * Gets the physics.
	 *
	 * @return the physics
	 */
	public Physics getPhysics() {
		return physics;
	}

	/**
	 * Sets the physics.
	 *
	 * @param physics the new physics
	 */
	public void setPhysics(Physics physics) {
		this.physics = physics;
	}

	/**
	 * Gets the zoom level.
	 *
	 * @return the zoom level
	 */
	public double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * Sets the zoom level.
	 *
	 * @param zoomLevel the new zoom level
	 */
	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	/**
	 * Gets the xoffset.
	 *
	 * @return the xoffset
	 */
	public int getXoffset() {
		return Xoffset;
	}

	/**
	 * Sets the xoffset.
	 *
	 * @param xoffset the new xoffset
	 */
	public void setXoffset(int xoffset) {
		Xoffset = xoffset;
	}

	/**
	 * Gets the yoffset.
	 *
	 * @return the yoffset
	 */
	public int getYoffset() {
		return Yoffset;
	}

	/**
	 * Sets the yoffset.
	 *
	 * @param yoffset the new yoffset
	 */
	public void setYoffset(int yoffset) {
		Yoffset = yoffset;
	}

	/**
	 * Gets the top left x.
	 *
	 * @return the top left x
	 */
	public int getTopLeftX() {
		return topLeftX;
	}

	/**
	 * Sets the top left x.
	 *
	 * @param topLeftX the new top left x
	 */
	public void setTopLeftX(int topLeftX) {
		this.topLeftX = topLeftX;
	}

	/**
	 * Gets the top left y.
	 *
	 * @return the top left y
	 */
	public int getTopLeftY() {
		return topLeftY;
	}

	/**
	 * Sets the top left y.
	 *
	 * @param topLeftY the new top left y
	 */
	public void setTopLeftY(int topLeftY) {
		this.topLeftY = topLeftY;
	}

	/**
	 * Gets the bottom right x.
	 *
	 * @return the bottom right x
	 */
	public int getBottomRightX() {
		return bottomRightX;
	}

	/**
	 * Sets the bottom right x.
	 *
	 * @param bottomRightX the new bottom right x
	 */
	public void setBottomRightX(int bottomRightX) {
		this.bottomRightX = bottomRightX;
	}

	/**
	 * Gets the bottom right y.
	 *
	 * @return the bottom right y
	 */
	public int getBottomRightY() {
		return bottomRightY;
	}

	/**
	 * Sets the bottom right y.
	 *
	 * @param bottomRightY the new bottom right y
	 */
	public void setBottomRightY(int bottomRightY) {
		this.bottomRightY = bottomRightY;
	}

	/**
	 * Checks if is draw nodes.
	 *
	 * @return true, if is draw nodes
	 */
	public boolean isDrawNodes() {
		return drawNodes;
	}

	/**
	 * Sets the draw nodes.
	 *
	 * @param drawNodes the new draw nodes
	 */
	public void setDrawNodes(boolean drawNodes) {
		this.drawNodes = drawNodes;
	}

	/**
	 * Checks if is draw edges.
	 *
	 * @return true, if is draw edges
	 */
	public boolean isDrawEdges() {
		return drawEdges;
	}

	/**
	 * Sets the draw edges.
	 *
	 * @param drawEdges the new draw edges
	 */
	public void setDrawEdges(boolean drawEdges) {
		this.drawEdges = drawEdges;
	}

	/**
	 * Checks if is draw text.
	 *
	 * @return true, if is draw text
	 */
	public boolean isDrawText() {
		return drawText;
	}

	/**
	 * Sets the draw text.
	 *
	 * @param drawText the new draw text
	 */
	public void setDrawText(boolean drawText) {
		this.drawText = drawText;
	}

	/**
	 * Checks if is draw debug.
	 *
	 * @return true, if is draw debug
	 */
	public boolean isDrawDebug() {
		return drawDebug;
	}

	/**
	 * Sets the draw debug.
	 *
	 * @param drawDebug the new draw debug
	 */
	public void setDrawDebug(boolean drawDebug) {
		this.drawDebug = drawDebug;
	}

	/**
	 * Checks if is draw forces.
	 *
	 * @return true, if is draw forces
	 */
	public boolean isDrawForces() {
		return drawForces;
	}

	/**
	 * Sets the draw forces.
	 *
	 * @param drawForces the new draw forces
	 */
	public void setDrawForces(boolean drawForces) {
		this.drawForces = drawForces;
	}

	/**
	 * Gets the select area.
	 *
	 * @return the select area
	 */
	public static int getSelectArea() {
		return SELECT_AREA;
	}

	/**
	 * Gets the back r.
	 *
	 * @return the back r
	 */
	public static int getBackR() {
		return BACK_R;
	}

	/**
	 * Gets the back g.
	 *
	 * @return the back g
	 */
	public static int getBackG() {
		return BACK_G;
	}

	/**
	 * Gets the back b.
	 *
	 * @return the back b
	 */
	public static int getBackB() {
		return BACK_B;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Sets the ypos.
	 *
	 * @param ypos the ypos to set
	 */
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	/**
	 * Gets the ypos.
	 *
	 * @return the ypos
	 */
	public int getYpos() {
		return ypos;
	}

	/**
	 * Sets the xpos.
	 *
	 * @param xpos the xpos to set
	 */
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	/**
	 * Gets the xpos.
	 *
	 * @return the xpos
	 */
	public int getXpos() {
		return xpos;
	}

	/**
	 * Sets the word info.
	 *
	 * @param wordInfo the new word info
	 */
	public void setWordInfo(String wordInfo) {
		this.wordInfo = wordInfo;
	}

	/**
	 * Gets the word info.
	 *
	 * @return the word info
	 */
	public String getWordInfo() {
		return wordInfo;
	}

}
