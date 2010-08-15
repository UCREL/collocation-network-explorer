package edu.gullick.CONE;


import java.util.Vector;

import processing.core.PApplet;
import processing.core.PFont;

public class Screen extends PApplet{
	
	/*CONSTANTS*/
	/*Area that a mouse over selects by*/
	static final int SELECT_AREA = 30;
	
	/*Background Colors*/
	static final int BACK_R = 0xD0;
	static final int BACK_G = 0xEA;
	static final int BACK_B = 0xFF;
	
	
	/*GLOBAL VARIABLES*/
	
	private static final long serialVersionUID = 1L;
	
	/*Font used throughout */
	private PFont fontA = null;
	
	/*Reference to the physics object - used only for drawing*/
	private Physics physics = null;
	
	/*level of zoom*/
	private double zoomLevel = 1.0D;
	

	/*Offset due to user dragging system*/
	private int Xoffset = 0; 
	private int Yoffset = 0; 
	

	/*Coordinates to work out whether shapes are on or off the screen (and therefore whether necessary to draw)*/
	private int topLeftX = 0;
	private int topLeftY =  0;
	private int bottomRightX = 0;
	private int bottomRightY = 0;
	
	 
	 /*Booleans for drawing options*/
	 private boolean drawNodes = false;
	 private boolean drawEdges = true;
	 private boolean drawText = true;
	 private boolean drawDebug = false;
	 private boolean drawForces = false;
	 private boolean smoothFont = false;
	 private boolean smoothAnimation = false;
	 
	 
	 
	 private boolean followPointerWithNode = false;
	 private String wordToFollowPointer = "";
	 private boolean drawNodeInfo = true;
	 
	 private int xpos = 0;
	 private int ypos = 0;
	 
	 private String wordInfo = "";
	 
	 private double TFilter = 0D;
	 
	 
	public double getTFilter() {
		return TFilter;
	}


	public void setTFilter(double tFilter) {
		TFilter = tFilter;
	}


	public Screen(Physics physics){
		this.physics = physics;	
		frameRate(15F);
		fontA = createFont("FFScala-32.vlw", 12);
	}

	
	 public void draw() {

		 try{
		 	topLeftX = (int) (0 - Xoffset) -10 ;
			topLeftY = (int) (0 - Yoffset) -10;
			bottomRightX = (int) ((width/zoomLevel) - Xoffset) +10;
			bottomRightY = (int) ((height/zoomLevel) - Yoffset) +10;
			int realX = (int) ((mouseX /zoomLevel) - Xoffset) ;
			int realY = (int) ((mouseY /zoomLevel) - Yoffset) ;
			
			background(BACK_R,BACK_G,BACK_B);
			
			
			pushMatrix();
			
			scale((float) zoomLevel);
			translate((float) Xoffset ,(float)Yoffset);
			if(followPointerWithNode){
				stroke(0,0,0,20);
				fill(0,0,0,20);
				int x = (int) ((mouseX /zoomLevel) - Xoffset);
				int y = (int) ((mouseY /zoomLevel) - Yoffset);
				textFont(fontA, 28);
						ellipse(x  , y ,20,20);
				text(wordToFollowPointer, x  ,y  ) ;
			}
			if(drawEdges){
				stroke(0,0,0,20);
					for(int a = 0;  a < physics.getSprings().size(); a++){
						WordLink wl = (WordLink) physics.getSprings().get(a);
						if(isOnScreen(wl) && wl.getTscore() < TFilter){
							if(smoothAnimation)smooth();
							strokeWeight(wl.getThickness());
							float[] mouse = {realX,realY};
							
							float[] position1 = {wl.getOneEnd().position().x(),wl.getOneEnd().position().y()};
							float[] newPos1 = scaleUP(mouse,position1, ((WordNode)wl.getOneEnd()).getDiameter());
							
							float[] position2 = {wl.getTheOtherEnd().position().x(),wl.getTheOtherEnd().position().y()};
							float[] newPos2 = scaleUP(mouse,position2,  ((WordNode)wl.getTheOtherEnd()).getDiameter());
							
							line(newPos1[0],newPos1[1],newPos2[0],newPos2[1]);
							if(smoothAnimation)noSmooth();
						}
				}
		
			}
			if(drawForces){
				stroke(100);
				strokeWeight(1);
				for(int a = 0;  a < physics.getAttractions().size(); a++){
					WordAttraction wa = (WordAttraction) physics.getAttractions().get(a);
					if(isOnScreen(wa)){
						if(smoothAnimation)smooth(); 
						
						float[] mouse = {realX,realY};
						
						float[] position1 = {wa.getOneEnd().position().x(),wa.getOneEnd().position().y()};
						float[] newPos1 = scaleUP(mouse,position1, ((WordNode)wa.getOneEnd()).getDiameter());
						
						float[] position2 = {wa.getTheOtherEnd().position().x(),wa.getTheOtherEnd().position().y()};
						float[] newPos2 = scaleUP(mouse,position2,  ((WordNode)wa.getTheOtherEnd()).getDiameter());
						
						line(newPos1[0],newPos1[1],newPos2[0],newPos2[1]);
						
						if(smoothAnimation)noSmooth();
					}
				}
			}
			
			for(int a = 0;  a < physics.getParticles().size(); a++){
				 WordNode wn = (WordNode) physics.getParticles().get(a);
				 boolean mouseOver = false;
				 if(isOnScreen(wn)){
					fill(wn.getColor().getRed(),wn.getColor().getGreen(),wn.getColor().getBlue(),20);
					stroke(0,0,0,40);
					mouseOver = mouseOver(wn);
					if(mouseOver){
						
						stroke(200,0,0,60);
						Vector<WordLink> links = wn.getLinks();
						for(int x = 0; x < links.size(); x++){
							WordLink wl = links.get(x);
							if(smoothAnimation)smooth();
							strokeWeight(wl.getThickness());
							float[] mouse = {realX,realY};
							
							float[] position1 = {wl.getOneEnd().position().x(),wl.getOneEnd().position().y()};
							float[] newPos1 = scaleUP(mouse,position1, ((WordNode)wl.getOneEnd()).getDiameter());
							
							float[] position2 = {wl.getTheOtherEnd().position().x(),wl.getTheOtherEnd().position().y()};
							float[] newPos2 = scaleUP(mouse,position2,  ((WordNode)wl.getTheOtherEnd()).getDiameter());
							
							line(newPos1[0],newPos1[1],newPos2[0],newPos2[1]);
							if(smoothAnimation)noSmooth();
						}
					}
					
					
					if(drawNodes){
						if(smoothAnimation)smooth();
						int size = 0;
					//	if(mouseOver(wn)){
					//		size = wn.getDiameter()*4;
					//	}else{
							size = wn.getDiameter();
					//	}
												
						if(wn.isNodeOpen()){
							fill(255,0,0,40);
						}
						
		
						 
						float[] mouse = {realX,realY};
						float[] position = {wn.position().x(),wn.position().y()};
						float[] newPos = scaleUP(mouse,position, size);
						
							//TODO change the scale of stuff..
						ellipse(newPos[0]  , newPos[1] , size,size);
						
						if(smoothAnimation){
							noSmooth();
						}
					}
					if(drawText){
						int textFontSize = 0;
		
						if(mouseOver(wn)){
							fill(0,255);
							textFontSize = 36;
						}else{
							fill(0,120);
							textFontSize = 14;
						}
						
						if(wn.isNodeOpen()){
							fill(180,0,0,200);
							textFontSize +=   10;
						}
						
						if(wn.isSelected()){
							fill(0,255,0,255);
						}
						
						
						
						textFont(fontA, textFontSize);
						if(smoothFont)smooth();
						float[] mouse = {realX,realY};
						
						float[] position= {wn.position().x(),wn.position().y()};
						float[] newPos = scaleUP(mouse,position, wn.getDiameter());
						text(wn.getWord(),newPos[0]  - (textWidth(wn.getWord()) / 2)  ,newPos[1]  ) ;
						if(smoothFont)noSmooth();
					}
				 }
		 	 }
			
			
			popMatrix();
			 if(drawDebug){
				fill(0x00,0x00,0x00);
			 	textFont(fontA, 14);
			 	if(smoothFont)smooth();
			 	text("FPS:" + (int)frameRate,2,15);
			 	text("Number of Nodes: " + (int) physics.getParticles().size(),2,30);
			 	text("Number of Edges: " + (int) physics.getSprings().size(),2,45);
			 	text("Number of Forces: " + (int) physics.getAttractions().size(),2,60);
			 	text("Zoom Level: " + zoomLevel,2,75);
			 	text("MouseX: " + mouseX,2,90);
			 	text("MouseY " + mouseY,2,105);
			 	text("Xoffset: " + Xoffset,2,120);
			 	text("Yoffset " + Yoffset,2,135);
			 	if(smoothFont)noSmooth();
			 }
			 
			 if(drawNodeInfo){
				 fill(0x00,0x00,0x00);
				 	textFont(fontA, 14);
				 	if(smoothFont)smooth();
				 	text(wordInfo,width - textWidth(wordInfo),15);
				 	if(smoothFont)noSmooth();
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		
	 }
	 
	 
	 public float[] scaleUP(float[] mouse, float[] position, int diameter){
		 float[] posToMouse = { position[0] - mouse[0], position[1] - mouse[1] } ;
		 float   d = sqrt(posToMouse[0]*posToMouse[0] + posToMouse[1]*posToMouse[1] );
         float   scale = 4*exp(-d/(diameter*3))+1; // even more localised: my preferred one!
         float[] result = { mouse[0] + posToMouse[0]*scale,mouse[1] + posToMouse[1]*scale,scale } ;
         return result;
	}
	 
	 
	 public boolean isOnScreen(WordNode n){
		if(n.position().x() > topLeftX && n.position().x()  < bottomRightX && n.position().y()  > topLeftY && n.position().y()  < bottomRightY){
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean isOnScreen(WordLink n){
		 //if((n.getOneEnd().position().x() > topLeftX && n.getOneEnd().position().x()  < bottomRightX && n.getOneEnd().position().y()  > topLeftY && n.getOneEnd().position().y()  < bottomRightY) || (n.getTheOtherEnd().position().x() > topLeftX && n.getTheOtherEnd().position().x()  < bottomRightX && n.getTheOtherEnd().position().y()  > topLeftY && n.getTheOtherEnd().position().y()  < bottomRightY)) {
			 return true;
		// }
		 //return false;
	 }
	 
	 public boolean isOnScreen(WordAttraction n){
		// if((n.getOneEnd().position().x() > topLeftX && n.getOneEnd().position().x()  < bottomRightX && n.getOneEnd().position().y()  > topLeftY && n.getOneEnd().position().y()  < bottomRightY) || (n.getTheOtherEnd().position().x() > topLeftX && n.getTheOtherEnd().position().x()  < bottomRightX && n.getTheOtherEnd().position().y()  > topLeftY && n.getTheOtherEnd().position().y()  < bottomRightY)) {
			 return true;
		// }
		// return false;
	 }
	
	 public boolean mouseOver(WordNode n){
		 int realX = (int) ((mouseX /zoomLevel) - Xoffset) ;
		 int realY = (int) ((mouseY /zoomLevel) - Yoffset) ;
		
		 float[] mouse = {realX,realY};
			float[] position= {n.position().x(),n.position().y()};
			float[] newPos = scaleUP(mouse,position, n.getDiameter());
			
		 
		 if( newPos[0] < (realX + SELECT_AREA) &&  newPos[0] > (realX - SELECT_AREA) &&  newPos[1] < (realY + SELECT_AREA) &&  newPos[1] > (realY- SELECT_AREA)  ){
			//for(int x = 0; x < n.getAttractions().size(); x++){
			//	n.getAttractions().get(x).setMinimumDistance( 50);
			//}
			 
			 return true; 
		 }
		 return false;
	 }
	 
	public void center(WordNode tempNode){
		 /* This Function centers the display on a certain node*/
		 	System.out.println("X  " + tempNode.position().x());
		 	System.out.println("Y  " + tempNode.position().y());
		 	setXoffset((int) (( (width /zoomLevel) - tempNode.position().x() ) -((width /zoomLevel) /2)   ) 				) ; 
			setYoffset((int) (( (height /zoomLevel) - tempNode.position().y() ) - ((height /zoomLevel) /2)    )           )  ; 
	}


	public void setDisplayNodes(boolean x){
		drawNodes = x;
	}
	
	public void setDisplayEdges(boolean x){
		drawEdges = x;
	}
	
	public void setDisplayText(boolean x){
		drawText = x;
	}
	
	public void setDisplayDebug(boolean x){
		drawDebug = x;
	}
	
	public void setDisplayForces(boolean x){
		drawForces = x;
	}
	  

	
	public void setXOffset(int newOffset){
		Xoffset = newOffset;
	}
	
	public void setYOffset(int newOffset){
		Yoffset = newOffset;
	}
	
	public void resetZoom(){
		zoomLevel = 1.0F;
	}

	public void zoomIn(){
		zoomLevel *= 1.2;
	}
	
	public void zoomOut(){
		zoomLevel /= 1.2;
	}

	public boolean isSmoothFont() {
		return smoothFont;
	}


	public void setSmoothFont(boolean smoothFont) {
		this.smoothFont = smoothFont;
	}


	public boolean isSmoothAnimation() {
		return smoothAnimation;
	}


	public void setSmoothAnimation(boolean smoothAnimation) {
		this.smoothAnimation = smoothAnimation;
	}
	
	public void setFollowPointer(boolean x, String word){
		this.followPointerWithNode = x;
		this.wordToFollowPointer = word;
	}


	public PFont getFontA() {
		return fontA;
	}


	public void setFontA(PFont fontA) {
		this.fontA = fontA;
	}


	public Physics getPhysics() {
		return physics;
	}


	public void setPhysics(Physics physics) {
		this.physics = physics;
	}


	public double getZoomLevel() {
		return zoomLevel;
	}


	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}


	public int getXoffset() {
		return Xoffset;
	}


	public void setXoffset(int xoffset) {
		Xoffset = xoffset;
	}


	public int getYoffset() {
		return Yoffset;
	}


	public void setYoffset(int yoffset) {
		Yoffset = yoffset;
	}


	public int getTopLeftX() {
		return topLeftX;
	}


	public void setTopLeftX(int topLeftX) {
		this.topLeftX = topLeftX;
	}


	public int getTopLeftY() {
		return topLeftY;
	}


	public void setTopLeftY(int topLeftY) {
		this.topLeftY = topLeftY;
	}


	public int getBottomRightX() {
		return bottomRightX;
	}


	public void setBottomRightX(int bottomRightX) {
		this.bottomRightX = bottomRightX;
	}


	public int getBottomRightY() {
		return bottomRightY;
	}


	public void setBottomRightY(int bottomRightY) {
		this.bottomRightY = bottomRightY;
	}


	public boolean isDrawNodes() {
		return drawNodes;
	}


	public void setDrawNodes(boolean drawNodes) {
		this.drawNodes = drawNodes;
	}


	public boolean isDrawEdges() {
		return drawEdges;
	}


	public void setDrawEdges(boolean drawEdges) {
		this.drawEdges = drawEdges;
	}


	public boolean isDrawText() {
		return drawText;
	}


	public void setDrawText(boolean drawText) {
		this.drawText = drawText;
	}


	public boolean isDrawDebug() {
		return drawDebug;
	}


	public void setDrawDebug(boolean drawDebug) {
		this.drawDebug = drawDebug;
	}


	public boolean isDrawForces() {
		return drawForces;
	}


	public void setDrawForces(boolean drawForces) {
		this.drawForces = drawForces;
	}


	public static int getSelectArea() {
		return SELECT_AREA;
	}


	public static int getBackR() {
		return BACK_R;
	}


	public static int getBackG() {
		return BACK_G;
	}


	public static int getBackB() {
		return BACK_B;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/**
	 * @param ypos the ypos to set
	 */
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}


	/**
	 * @return the ypos
	 */
	public int getYpos() {
		return ypos;
	}


	/**
	 * @param xpos the xpos to set
	 */
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}


	/**
	 * @return the xpos
	 */
	public int getXpos() {
		return xpos;
	}


	public void setWordInfo(String wordInfo) {
		this.wordInfo = wordInfo;
	}


	public String getWordInfo() {
		return wordInfo;
	}
	
}
