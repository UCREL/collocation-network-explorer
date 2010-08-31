package edu.gullick.CONE;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.gullick.CONE.Corpus.LIMIT_TYPE;
import edu.gullick.physics2D.Particle;
import edu.gullick.physics2D.Spring;

public class Controller extends JApplet implements ActionListener, ItemListener, MouseListener, ChangeListener, MouseMotionListener, WindowListener{
	private static final long serialVersionUID = 1L;
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private Physics physics = null;
	private WordIndex wordIndex = null;
	private Screen theScreen = null;
	private GUI theGUI = null;
	private SidePane sidePane = null;
	private WordNode beingDragged = null;
	private boolean dragging = false;
	private int particleDragStartX = 0;
	private int particleDragStartY = 0;
	private boolean locked = false;
	private int dragStartX = 0;
	private int dragStartY = 0;
	private int dragStartOffsetX = 0;
	private int dragStartOffsetY = 0;
	private boolean addingNewNode = false;
	private String newNodeWord = "undefined";
	private HashMap<String, WordNode> currentNodes = new HashMap<String,WordNode>();
	private Vector<HistoryObject> history = new Vector<HistoryObject>();
	private WordNode lastSelected = null;
	private Corpus currentCorpus = null;
	private int oldFilterValue = 50;
	
	private HashMap<Integer, Corpus> corpuses = new HashMap<Integer, Corpus>();
	
	public static void main(String[] args){
		new Controller();
	}

	/*The constructor method, creates each of the necessary objects, and adds listeners etc*/
	public Controller(){
		physics = new Physics();
		Timer timer = new Timer(); 
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try{
					physics.step();
				}catch(Exception e){}
			}
		}, 0, 67); 
		wordIndex = new WordIndex();
		sidePane = new SidePane(this);
		theScreen = new Screen(physics);
		theGUI = new GUI(this,theScreen,physics,wordIndex, WIDTH,HEIGHT);
		theScreen.addMouseListener(this);
		theScreen.addMouseMotionListener(this);
		theGUI.updateLabels(null);
	}

	/*listens for button presses in the GUI and calls other methods appropriately*/
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == theGUI.importButton){
			importPressed(false);
		}else if(e.getSource() == theGUI.resetButton){
			resetPressed(false);
		}else if(e.getSource() == theGUI.centerButton){
			centerPressed(true);
		}else if(e.getSource() == theGUI.deleteWordButton){
			deletePressed(true);
		}else if(e.getSource() == theGUI.addButton){
			addPressed(true);
		}else if(e.getSource() == theGUI.tFilterButton){
			adjustTFilterPressed(true);
		}else if(e.getSource() == theGUI.aboutButton){
			aboutPressed(false);
		}else if(e.getSource() == theGUI.undoButton){
			 undoPressed(false);
		}else if(e.getSource() == theGUI.helpButton){
			helpPressed(false);
		}else if (e.getSource() == theGUI.zoomInButton) {
			zoomInPressed(true);
	    }else if (e.getSource() == theGUI.zoomOutButton) {
			zoomOutPressed(true);
	    } else if(e.getSource() == theGUI.linkGoogleButton){
	    	openLinkInGoogle();
	    } else if(e.getSource() == theGUI.linkWikipediaButton){
	    	openLinkInWikipedia();
	    } else if(e.getSource() == theGUI.linkWMatrixButton){
	    	openLinkInWMatrix();
	    } else if(e.getSource() == theGUI.linkThesaurusButton){
	    	openLinkInThesaurus();
	    } else if(e.getSource() == theGUI.linkDictionaryButton){
	    	openLinkInDictionary();
	    }   else if(e.getSource() == sidePane.searchButton){
	    	searchPressed(false);
	    }
		theGUI.menuBar.requestFocus();
	}

	/*Listens for button presses (toggle switches) in the GUI and calls the methods appropriately*/
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		if (source == theGUI.displayNodes) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleDisplayNodes(false, true);
			}else{
				toggleDisplayNodes(true, true);
			}
	    } else if (source == theGUI.displayEdges) {
	    	if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleDisplayEdges(false, true);
			}else{
				toggleDisplayEdges(true, true);
			}
	    } else if (source == theGUI.displayWords) {
	     	if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleDisplayWords(false, true);
			}else{
				toggleDisplayWords(true, true);
			}
	      } else if (source == theGUI.displayDebug) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleDisplayDebug(false,true);
			}else{
				toggleDisplayDebug(true,true);
			}
	    } else if (source == theGUI.displayForces) {
	    	if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleDisplayForces(false,true);
			}else{
				toggleDisplayForces(true,true);
			}
	    } else if (source == theGUI.smoothFontButton) {
	    	if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleSmoothFont(false,true);
			}else{
				toggleSmoothFont(true,true);
			}
	    } else if (source == theGUI.smoothAnimationButton) {
	    	if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleSmoothAnimation(false,true);
			}else{
				toggleSmoothAnimation(true,true);
			}
	    } else if (source == theGUI.pauseButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				togglePause(false,true);
			}else{
				togglePause(true,true);
			}
	    }else if (source == theGUI.showIndex) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				toggleShowIndex(false,true);
			}else{
				toggleShowIndex(true,true);
			}
	    }
		theGUI.menuBar.requestFocus();
	}

	/*Method called if the mouse was released*/
	public void mouseReleased(MouseEvent arg0) {
		if(beingDragged != null){
			dragging = false;
			if(!beingDragged.isNodeOpen()){
				beingDragged.makeFree();
			}
			if(beingDragged.position().x() != particleDragStartX ||  beingDragged.position().y() != particleDragStartY){
				history.add( 0,new HistoryObject(HistoryObject.ACTION.DRAG_NODE, new ParticleHistory(beingDragged.getWord(),(int)( particleDragStartX/theScreen.getZoomLevel()),(int)(particleDragStartY/theScreen.getZoomLevel())) ));
			}
			beingDragged = null;
		}else if(locked){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.DRAG_CANVAS, new Point(dragStartOffsetX ,dragStartOffsetY )));
			dragging = false;
			locked = false;
		}
		theGUI.menuBar.requestFocus();
	}

	/*Method called if the user has dragged the mouse*/
	public void mouseDragged(MouseEvent e) {
		if(dragging){
			 beingDragged.position().setX((float) ((e.getX() /theScreen.getZoomLevel()) - theScreen.getXoffset()));
			 beingDragged.position().setY( (float)((e.getY() /theScreen.getZoomLevel()) - theScreen.getYoffset()));
		}else if(locked) {
			theScreen.setXoffset((int) ((e.getX()-dragStartX) / theScreen.getZoomLevel())); 
			theScreen.setYoffset((int) ((e.getY()-dragStartY)/ theScreen.getZoomLevel())); 
		}
	}
	
	/*Method called if the mouse is pressed*/
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount()==1){
			if(addingNewNode){
				addNodeToSystem(newNodeWord,(int)((mouseEvent.getX() / theScreen.getZoomLevel()) - theScreen.getXoffset()   ),(int)((mouseEvent.getY() / theScreen.getZoomLevel()) - theScreen.getYoffset()), true   );
				addingNewNode = false;
				theScreen.setFollowPointer(false,"");
			}else{
				WordNode wn = null; 
				int xpos = (int) (mouseEvent.getX() - (theScreen.getXoffset()* theScreen.getZoomLevel()));
				int ypos = (int) (mouseEvent.getY() - (theScreen.getYoffset()* theScreen.getZoomLevel()));
				
				for(int a = 0;  a < physics.getParticles().size(); a++){
					if(theScreen.mouseOver((WordNode)physics.getParticles().get(a))){
						wn = (WordNode) physics.getParticles().get(a);
					}
				}
				if(wn != null){
					wn.makeFixed();
					if(lastSelected != null){
						lastSelected.setSelected(false);
					}
					wn.setSelected(true);
					updateDetailsInScreen(wn);
					
					lastSelected = wn;
					
					theGUI.updateLabels(wn);
					particleDragStartX =  xpos; 
					particleDragStartY =  ypos; 
					beingDragged = wn;
					dragging = true;
				 }else{
					locked = true; 
				 	dragStartX =  xpos; 
				 	dragStartY =  ypos; 
				 	dragStartOffsetX = theScreen.getXoffset();
				 	dragStartOffsetY = theScreen.getYoffset();
				 }
			}
		}else if(mouseEvent.getClickCount()==2){
			for(int a = 0;  a < physics.getParticles().size(); a++){
				if(theScreen.mouseOver((WordNode)physics.getParticles().get(a))){
					WordNode wn = (WordNode) physics.getParticles().get(a);
					if(wn.isNodeOpen()){
						closeNode(wn, true);
					}else{
						openNode(wn, true);
					}
					break;
				}
			}
		}
	}
		
	/*Removes all of the attractions to a particular particle in the system*/
	public void removeAllAttractionsToNode(WordNode wn){
		while(wn.getAttractions().size() > 0){
			WordAttraction temp = wn.getAttractions().get(0);
			((WordNode)temp.getTheOtherEnd()).removeAttraction(temp);
			((WordNode)temp.getOneEnd()).removeAttraction(temp);
			physics.removeAttraction(temp);
		}
	
	}	
	
	/*method called to open a given node*/
	public void openNode(WordNode wn, boolean addToHistory){
	System.out.println("Opening: " + wn.getWord());
		try {
			wn.makeFixed();
			wn.setNodeOpen(true);
			Vector<LinkInformation> neighbours = currentCorpus.getNeighbours(wn.getWord(), Corpus.LIMIT_TYPE.PERCENTAGE, theGUI.filterSlider.getValue()); 
			for(int x = 0;  x < neighbours.size();x++){
				LinkInformation tempInfo = neighbours.get(x);
				String word = "";
				boolean wasWord1 = false;
				if(wn.getWord().equals(tempInfo.getWord1())){
					word = tempInfo.getWord2().trim();
					wasWord1 = false;
				}else{
					word = tempInfo.getWord1().trim();
					wasWord1 = true;
				}
				if(!wn.getWord().equals(word)){
					WordNode tempNode = null;
					if(currentNodes.containsKey(word)){
						// current node Exists
						tempNode = currentNodes.get(word);	
					}else{
						//node doesnt already exist (so have to create a new node first)
						tempNode = new WordNode( wn.position().x() +25 + ((int)(100*Math.random())), wn.position().y() + ((int)(100*Math.random())), 1, Color.BLUE,  word, 0, 10);
						if(wasWord1){
							tempNode.setFrequency(tempInfo.frequency1);
						}else{
							tempNode.setFrequency(tempInfo.frequency2);
						}
						physics.addParticle(tempNode);
						currentNodes.put(word.trim(), tempNode);
					}
					if(physics.getSpring(tempNode, wn) == null){
						double offset = 0-wordIndex.totalMinAffinity;
						double lineLength = 500 - (((tempInfo.getAffinity() + offset)/(wordIndex.totalMaxAffinity + offset))*400) ;
						double lineWidth = 50 - (lineLength/10);
						WordLink tempSpring = new WordLink(wn,tempNode,(int)lineLength,0.2F,0.4F,Color.BLUE, (int)lineWidth, 1,tempInfo.getTscore());
						physics.addSpring(tempSpring);
						tempNode.addNeighbour(wn);
						wn.addNeighbour(tempNode);
						tempNode.addLink(tempSpring);
						wn.addLink(tempSpring);
						//add a repulsion between every other node and this one
						for(int a = 0; a < physics.getParticles().size(); a++){
							WordNode n = (WordNode) physics.getParticles().get(a);
							
							if(n != tempNode ){
								WordAttraction tempAttraction = new WordAttraction(tempNode,n, -50000, 15);
								n.addAttraction(tempAttraction);
								tempNode.addAttraction(tempAttraction);
								physics.addAttraction( tempAttraction);
							}	
						}
					}
					
				}
					
			}
		}catch (Exception e) {}
		
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.EXPAND_NODE, wn.getWord()));
		}
	}
	
	
	public void closeNode(WordNode wn, boolean addToHistory){
		closeNode(wn,addToHistory,true);
	}
	
	public void closeNode(WordNode wn, boolean addToHistory, boolean removeNodes){
		System.out.println("Closing: " + wn.getWord());
		Vector<WordLink> links = wn.getLinks();
		
		for(int x = 0; x < links.size();){
			boolean didDelete = false;
			WordLink tempSpring = links.get(x);
			WordNode tempNode = null;
			if(tempSpring.getOneEnd() == wn){
				tempNode = (WordNode) tempSpring.getTheOtherEnd();
			}else{
				tempNode = (WordNode) tempSpring.getOneEnd();
			}
		
			if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() == 1){
				// other node is a closed node, and only connected to this one. Remove it
			 	
				if(removeNodes){
				removeAllAttractionsToNode(tempNode);
				}
				wn.getNeighbours().remove(tempNode);
				physics.removeSpring(tempSpring);
				if(removeNodes){
					physics.removeParticle(tempNode);
				}
				
				wn.removeLink(tempSpring);
				if(removeNodes){
						currentNodes.remove(tempNode.getWord());
				}
				didDelete = true;
			}else if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() > 1){
				//remove references to each other
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				//remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
				didDelete = true;
			}else if(tempNode.isNodeOpen() && !tempNode.isInCurrentCorpus()){
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				//remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
				didDelete = true;
			}
			
			if(!didDelete){
				x++;
			}
		}
		wn.setNodeOpen(false);
		wn.makeFree();
		
		if(addToHistory){

			history.add( 0,new HistoryObject(HistoryObject.ACTION.CLOSE_NODE, wn.getWord()));
		}
	}

	
	
	
	
	
	
	/*Is called if the user closes the sidePane window*/
	public void windowClosing(WindowEvent e) {
		sidePane.theFrame.setVisible(false);
		theGUI.showIndex.setSelected(false);
	}
	
	//this function removes all springs connected to a node
	public void removeAllSpringsToNode(WordNode wn) {
		while(wn.getLinks().size() > 0){
			WordLink tempLink = wn.getLinks().get(0);
		
				((WordNode)tempLink.getTheOtherEnd()).getLinks().remove(tempLink);
			
				((WordNode)tempLink.getOneEnd()).getLinks().remove(tempLink);
			
				physics.removeSpring(tempLink);
		}
	}

	//this function will update the node details as shown within the screen
	//TODO: update to seperate thread.
	public void updateDetailsInScreen(WordNode wn){
		Vector<LinkInformation> neighbours = null;
		String wordInfo = "";
		try {
			neighbours =  currentCorpus.getNeighbours(wn.getWord(), Corpus.LIMIT_TYPE.PERCENTAGE, theGUI.filterSlider.getValue()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(neighbours != null){
			wordInfo += "-------------------\n";
			wordInfo += wn.getWord() + "( " + neighbours.size() + " links ) \n";
			wordInfo += "-------------------\n";
			wordInfo += "Top " + Math.min(10, neighbours.size()) + ":\n";
			int limit = Math.min(10, neighbours.size());
			for(int x = 0; x < limit; x++){
				LinkInformation tempInfo = neighbours.get(x);
				if(tempInfo.word1.equals(wn.getWord().trim())){
					wordInfo += tempInfo.word2 + "\t" + tempInfo.affinity + "\n";
				}else{
					wordInfo += tempInfo.word1 + "\t" + tempInfo.affinity + "\n";
				}		
			}
		}else{
			wordInfo = "Error looking up word '" + wn.getWord() + "'";
		}
		theScreen.setWordInfo(wordInfo);
	}
	
	//this function adds a node to the system, returning true if succesful, and false otherwise
	//TODO: update to seperate thread.
	public  WordNode addNodeToSystem(String word, int x , int y, boolean addToHistory){
		if(!currentNodes.containsKey(word) && wordIndex.lookUpWord(word, currentCorpus)){
			WordNode temp = new WordNode(x,y, 1, Color.BLUE,  word, 0, 10);
			
			Vector<LinkInformation> wordInfo = currentCorpus.getNeighbours(word, Corpus.LIMIT_TYPE.NUMBER, 1);
			
			if(wordInfo.get(0).word1.equals(word)){
				temp.setFrequency(wordInfo.get(0).frequency1);
			}else{
				temp.setFrequency(wordInfo.get(0).frequency2);
			}
			
	
	
			for(int a = 0; a < physics.getParticles().size(); a++){
				WordNode n = (WordNode) physics.getParticles().get(a);
				if(n != temp ){
					WordAttraction tempAttraction = new WordAttraction(temp,n, -50000, 15);
					n.addAttraction(tempAttraction);
					temp.addAttraction(tempAttraction);
					physics.addAttraction( tempAttraction);
				}	
			}
		
			
			
			
			physics.addParticle(temp);
			if(addToHistory){
				history.add( 0,new HistoryObject(HistoryObject.ACTION.ADD_WORD, temp.getWord()));
			}
			currentNodes.put(word.trim(), temp);
			try {
				temp.setTotalInfoFromWordIndex(wordIndex.lookupWordNeighbours(word, currentCorpus));
			} catch (Exception e) {}
			return temp;
		}else if(currentNodes.containsKey(word)){
			return currentNodes.get(word);
		}else{
			return null;
		}
	}
	
	//this function removes the node from the system given a word
	//TODO: update to seperate thread.
	public boolean removeNodeFromSystem(String word, boolean close, boolean removeSprings, boolean removeAttractions, boolean addToHistory){
		WordNode wn = null;
		if(currentNodes.containsKey(word)){
			wn = currentNodes.get(word);
		}else{
			return false;
		}
		//close the node if necessary
		if(close){
			closeNode(wn, false);
		}
		//remove attractions if necesary
		if(removeAttractions){
			removeAllAttractionsToNode(wn);
		}
		//remove springs if necessary
		if(removeSprings){
			removeAllSpringsToNode(wn);
		}
		//Now remove node totally from physics and currentNodes list.
		currentNodes.remove(word);
		physics.removeParticle(wn);
		if(addToHistory){
			history.add(0,new HistoryObject(HistoryObject.ACTION.DELETE_NODE, new DeleteHistory(lastSelected.getWord(),(int)lastSelected.position().x(), (int)lastSelected.position().y(), close )));
		}
		return true;
	}	
	
	/*#### Functions called for each button action below this line ####*/
	
	public void toggleDisplayDebug(boolean selection, boolean addToHistory){
		theScreen.setDisplayDebug(selection);
		theGUI.displayDebug.setSelected(selection);
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG, selection));
		}
	}
	public void toggleDisplayWords(boolean selection, boolean addToHistory){
			theScreen.setDisplayText(selection);
			theGUI.displayWords.setSelected(selection);
			if(addToHistory){
				history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS, selection));
			}
	}
	public void toggleDisplayEdges(boolean selection, boolean addToHistory){
		theScreen.setDisplayEdges(selection);
		theGUI.displayEdges.setSelected(selection);
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES, selection));
		}
	}
	public void toggleDisplayForces(boolean selection, boolean addToHistory){
			theScreen.setDisplayForces(selection);
			theGUI.displayForces.setSelected(selection);
			if(addToHistory){
				history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES, selection));
			}
	}
	public void toggleDisplayNodes(boolean selection, boolean addToHistory){
		theScreen.setDisplayNodes(selection);
		theGUI.displayNodes.setSelected(selection);
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_NODES, selection));
		}
	}
	public void toggleSmoothFont(boolean selection, boolean addToHistory){
		theScreen.setSmoothFont(selection);
		theGUI.smoothFontButton.setSelected(selection);
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_FONT, selection));
		}
	}
	public void toggleSmoothAnimation(boolean selection, boolean addToHistory){
 			theScreen.setSmoothAnimation(selection);
 			theGUI.smoothAnimationButton.setSelected(selection);
			if(addToHistory){
				history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION, selection));
			}
	}
	public void togglePause(boolean selection, boolean addToHistory){
		physics.setPaused(selection);
		theGUI.pauseButton.setSelected(selection);
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.PAUSE_PHYSICS, true));
		}
	}
	public void zoomInPressed(boolean addToHistory){
		theScreen.zoomIn();
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.ZOOM_IN, null));
		}
	}
	public void zoomOutPressed(boolean addToHistory){
		theScreen.zoomOut();
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.ZOOM_OUT, null));
		}
	}
	public void toggleShowIndex(boolean selection, boolean addToHistory){
		sidePane.theFrame.setVisible(selection); 
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.SHOW_INDEX, selection));
		}
	
	}
	public void resetPressed(boolean addToHistory){
		physics.clear();
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		currentNodes.clear();
		if(addToHistory){
			//TODO: addHistory
		}
	}
	public void centerPressed(boolean addToHistory){
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		if(addToHistory){
			//history.add( 0,new HistoryObject(HistoryObject.ACTION.CENTER_PHYSICS, null));
			//TODO: fix the null in the statement above.
		}
	}
	public void deletePressed(boolean addToHistory){
		if(lastSelected == null){
			JOptionPane.showMessageDialog(null,"Cannot delete as no node is selected.");
		}else{
			JOptionPane.showMessageDialog(null,"Deleting word '" + lastSelected.getWord() + "' from the visualisation.");
			removeNodeFromSystem(lastSelected.getWord(),lastSelected.isNodeOpen(), true, true, addToHistory);
			lastSelected = null;
		}
	}
	
	/*Called if add is pressed*/
	public void addPressed(boolean addToHistory){
		//TODO: fix this below!
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
	    	private String tempString = null;
	        public Boolean doInBackground() {
	        	tempString = JOptionPane.showInputDialog("Please type the word to add below:").trim(); 
	        	
	        	if(wordIndex.lookUpWord(tempString, currentCorpus)){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        }
	        public void done() {
	        	Boolean temp = false;
	           	try {
					temp = get();
				} catch (Exception ignore){}
		     	if(temp && tempString != null){
	        		if(!currentNodes.containsKey(tempString)){
	        			theScreen.setFollowPointer(true, tempString);
	        			addingNewNode = true;
	        			newNodeWord = tempString;
	        		}else{
	        			JOptionPane.showMessageDialog(null,"That word already appears in the visualisation.");
	        			WordNode tempNode = currentNodes.get(tempString);
	        			theScreen.center(tempNode);
	        			if(lastSelected != null){
	        				lastSelected.setSelected(false);
	        			}
	        			tempNode.setSelected(true);
	        			updateDetailsInScreen(tempNode);	   
	        		}
	        	}else if(!temp && tempString != null){
	        		JOptionPane.showMessageDialog(null,"That word does not appear in the corpus");
	        	}else{
	        		// the user has cancelled the box..
	        	}  	
	        }
	    }; 
		worker.execute();
	}
	
	/*Called if the adjust tfilter was pressed*/
	public void adjustTFilterPressed(boolean addToHistory){
		//TODO: fix this part below:
		if(addToHistory){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.ADJUST_TFILTER, null));
		}
	}
	
	/*Called if the undo button is pressed*/
	public void undoPressed(boolean addToHistory){
		if(history.size() > 0){
			HistoryObject lastDone =  history.get(0) ;
			System.out.println("Undoing an action " + lastDone.action);
			if(lastDone.action == HistoryObject.ACTION.EXPAND_NODE){
				String wordToClose = currentNodes.get((String)lastDone.details).getWord();
				if(currentNodes.containsKey(wordToClose)){
					closeNode((WordNode) currentNodes.get(wordToClose), false );
				}else{
					System.out.println("Error closing node!");
				}
			}else if(lastDone.action == HistoryObject.ACTION.CLOSE_NODE){
				String wordToOpen = currentNodes.get((String)lastDone.details).getWord();
				if(currentNodes.containsKey(wordToOpen)){
					openNode((WordNode) currentNodes.get(wordToOpen), false);
				}else{
					System.out.println("Error opening node!");
				}
			}else if(lastDone.action == HistoryObject.ACTION.CENTER_PHYSICS){
				//TODO: fix this
			}else if(lastDone.action == HistoryObject.ACTION.ADD_WORD){
				String added = (String) lastDone.details;
				if(currentNodes.containsKey(added)){
					WordNode temp = currentNodes.get(added);
					physics.removeParticle(temp);
					removeAllAttractionsToNode(temp);
					currentNodes.remove(added);
				}
			}else if(lastDone.action == HistoryObject.ACTION.DRAG_CANVAS){
				Point hist = (Point) lastDone.details;
				theScreen.setXOffset(hist.x);
				theScreen.setYOffset(hist.y);
			}else if(lastDone.action == HistoryObject.ACTION.DRAG_NODE){
				ParticleHistory hist = (ParticleHistory) lastDone.details;
				if(currentNodes.containsKey(hist.getWord())){
					WordNode temp = currentNodes.get(hist.getWord());
					temp.position().set(hist.x, hist.y);
				}
			}else if(lastDone.action == HistoryObject.ACTION.DELETE_NODE){
				//TODO: fix this!
				DeleteHistory details = (DeleteHistory) lastDone.details;
				WordNode temp = new WordNode(details.getX(), details.getY(), 1, Color.BLUE,  details.getWord(), 0, 10);
				System.out.println(temp.getWord());
				physics.addParticle(temp);
				currentNodes.put(temp.getWord().trim(), temp);
				//create Node details.getWord() at details.getX() details.getY();
				if(details.isClosed()){
					openNode(temp, false);
				}
			}else if(lastDone.action == HistoryObject.ACTION.ADJUST_TFILTER){
				//TODO: NOT IMPLEMENTED YET (as source is not properly implemented)
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG){
				toggleDisplayDebug((Boolean) lastDone.details, false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS){
				toggleDisplayWords((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES){
				toggleDisplayEdges((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES){
				toggleDisplayForces((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_NODES){
				toggleDisplayNodes((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_FONT){
				toggleSmoothFont((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION){
				toggleSmoothAnimation((Boolean) lastDone.details,false);
			}else if(lastDone.action == HistoryObject.ACTION.PAUSE_PHYSICS){
				togglePause((Boolean) lastDone.details, false);
			}else if(lastDone.action == HistoryObject.ACTION.ZOOM_IN){
				zoomOutPressed(false);
			}else if(lastDone.action == HistoryObject.ACTION.ZOOM_OUT){
				zoomInPressed(false);
			}
			history.remove(lastDone);
			theGUI.menuBar.requestFocus();
		}
	}
	
	/*called if the help button is pressed*/
	public void helpPressed(boolean addToHistory){
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.helpText);
		if(addToHistory){
			//TODO: fix this
		}
	}
	
	/*called when the about button is pressed*/
	public void aboutPressed(boolean addToHistory){
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.aboutText, "About",JOptionPane.INFORMATION_MESSAGE  , theGUI.theLogo);
		if(addToHistory){
			//TODO: fix this
		}
	}
	
	/*Called if the sidepane import button is pressed*/
	public void searchPressed(boolean addToHistory){
		//TODO: FIX THIS PART BELOW
		System.out.println(sidePane.getInputText().trim()   );
    	if(wordIndex.lookUpWord(  sidePane.getInputText().trim()   , currentCorpus   )  == false ){
    		sidePane.setOutputText("That word is not significant in the corpus.");
    	}else{
    		Vector<LinkInformation> results = null;
    		try {
				results = wordIndex.lookupWordNeighbours( sidePane.getInputText().trim(), currentCorpus );
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    		
    		if(results != null){
    			String toSet = "";
    			for(int x =0; x < results.size();x++){
    				toSet += results.get(x).getWord1() + " : " + results.get(x).getWord2() + "  : " +results.get(x).getAffinity() +   "\n";
    			}
    			sidePane.setOutputText(toSet);
    		}else{
    			sidePane.setOutputText("Sorry, there was an error whilst querying the cdata file.");
    		}
    		
    	}
	}

	/*Method called if the import button is pressed*/
	public void importPressed(boolean addToHistory){
	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
    	Boolean userCancelled = false;
    	Corpus temp = null;
    	File[] files = null;
    	//This method automatically gets executed in a background thread

	        public Void doInBackground() {
	    		String[] formats = new String[] { "cdata"};
	    		String description = "cdata files (*.cdata )";
	    		boolean allowFolders = false;
	    		String fileName = "";
	    		JFileChooser chooser = new JFileChooser();
	    		chooser.setMultiSelectionEnabled(true);
	       		chooser.setFileFilter (new ExtendedFileFilter(formats,description,allowFolders));			
	    	    chooser.showOpenDialog(theGUI.theFrame);
	    	    files = chooser.getSelectedFiles();
	    	    for(int y = 0; y < files.length; y++){
		    	    if (files[y] == null){
		    	    	userCancelled = true;
		    	    	return null;
		    	    }
		           	try {
						fileName = files[y].getName();
		        		temp = wordIndex.analyzeXMLDOC(files[y].getPath(), theGUI);
		        		Integer index = theGUI.addCorpusToSlider(files[y].getName());
		        		corpuses.put(index, temp);
		        		if(index == 1){
		        			currentCorpus = corpuses.get(1);
		        		}
					} catch (Exception e) {
							e.printStackTrace();
					}
	    	    }
				return null;
	        }
	        public void done() {	
	        	if(!userCancelled){
	        		theGUI.setProgressBarString("Completed indexing of " +  files.length + " files.");		        	}
	        } 
	    }; 
		worker.execute();
		theGUI.filterSlider.setValue(50);
	}
	
	/*Called if one of the Sliders is moved*/
	public void stateChanged(ChangeEvent e) {
	/*	
	 * TODO::: complete this section
	 *
	 */ 
		
	if(e.getSource() == theGUI.filterSlider){
		theGUI.filterSlider.setEnabled(false);
			if(lastSelected != null){
				updateDetailsInScreen(lastSelected);
			}
			theGUI.filterSlider.setEnabled(false);
			int x = theGUI.filterSlider.getValue();
			if(oldFilterValue > x){
				System.out.println("lowering the slider");
				Vector<Particle> particles = physics.getParticles();
				for(int a = 0 ; a < particles.size(); a++){
					WordNode wn = (WordNode) particles.get(a);
					if(wn.isNodeOpen()){
						Vector<LinkInformation> inverseNeighbours =  currentCorpus.getInverseNeighbours(wn.getWord(), Corpus.LIMIT_TYPE.PERCENTAGE, x); 
					
						for(int d = 0; d < inverseNeighbours.size(); d++){
							String theWord = "";
							if(wn.getWord().equals(inverseNeighbours.get(d).getWord1())){
								theWord = inverseNeighbours.get(d).getWord2().trim();
							}else{
								theWord = inverseNeighbours.get(d).getWord1().trim();
							}
							
							WordNode tempNode = currentNodes.get(theWord);
							if(wn.getNeighbours().contains(tempNode)){
								//contains a thing which must be deleted!
								//TODO: combine this int oa handy little function
								WordLink tempSpring = wn.getSpringTo(tempNode);
								if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() == 1){
									removeAllAttractionsToNode(tempNode);
									wn.getNeighbours().remove(tempNode);
									physics.removeSpring(tempSpring);
									physics.removeParticle(tempNode);
									wn.removeLink(tempSpring);
									currentNodes.remove(tempNode.getWord());
								}else if( tempNode.getNeighbours().size() > 1){
									System.out.println("Removing link between "  + tempNode.getWord() + " and " + wn.getWord() );
									wn.getNeighbours().remove(tempNode);
									tempNode.getNeighbours().remove(wn);
									physics.removeSpring(tempSpring);
									tempNode.removeLink(tempSpring);
									wn.removeLink(tempSpring);
								}
	
							}
							
						}
	
					}
				
				}	
			}else if (oldFilterValue < x){
				System.out.println("increasing the slider");
				// get all of the new neighbours, and go through comparing..
				Vector<Particle> particles = physics.getParticles();
				for(int a = 0 ; a < particles.size(); a++){
					WordNode wn= (WordNode) particles.get(a);
					if(wn.isNodeOpen()){
						Vector<LinkInformation> neighbours =  currentCorpus.getNeighbours(wn.getWord(), Corpus.LIMIT_TYPE.PERCENTAGE, x); 
						for(int b = 0; b < neighbours.size(); b++){
						
							String theWord = "";
							boolean wasWord1 = false;
							if(wn.getWord().equals(neighbours.get(b).getWord1())){
								theWord = neighbours.get(b).getWord2().trim();
								wasWord1 = true;
							}else{
								theWord = neighbours.get(b).getWord1().trim();
								wasWord1 = false;
							}

							if(!wn.getNeighbours().contains(theWord)){
								

								
								/*
								 * TODO:: Make this into a seperate function, along with open node 
								 */
								
								WordNode tempNode = null;
								if(currentNodes.containsKey(theWord)){
									// current node Exists
									tempNode = currentNodes.get(theWord);	
								}else{
									//node doesnt already exist (so have to create a new node first)
									tempNode = new WordNode( wn.position().x() +25 + ((int)(100*Math.random())), wn.position().y() + ((int)(100*Math.random())), 1, Color.BLUE,  theWord, 0, 10);
									if(wasWord1){
										tempNode.setFrequency(neighbours.get(b).frequency1);
									}else{
										tempNode.setFrequency(neighbours.get(b).frequency2);
									}
									physics.addParticle(tempNode);
									currentNodes.put(theWord.trim(), tempNode);
								}
								if(physics.getSpring(tempNode, wn) == null){
									double offset = 0-wordIndex.totalMinAffinity;
									double lineLength = 500 - (((neighbours.get(b).getAffinity() + offset)/(wordIndex.totalMaxAffinity + offset))*400) ;
									double lineWidth = 50 - (lineLength/10);
									WordLink tempSpring = new WordLink(wn,tempNode,(int)lineLength,0.2F,0.4F,Color.BLUE, (int)lineWidth, 1,neighbours.get(b).getTscore());
									physics.addSpring(tempSpring);
									tempNode.addNeighbour(wn);
									wn.addNeighbour(tempNode);
									tempNode.addLink(tempSpring);
									wn.addLink(tempSpring);
									//add a repulsion between every other node and this one
									for(int c = 0; c < physics.getParticles().size(); c++){
										WordNode n = (WordNode) physics.getParticles().get(c);
										if(n != tempNode ){
											WordAttraction tempAttraction = new WordAttraction(tempNode,n, -50000, 15);
											n.addAttraction(tempAttraction);
											tempNode.addAttraction(tempAttraction);
											physics.addAttraction( tempAttraction);
										}	
									}
								}
								
							}
							
						}
					}
				
				}	
				
				/*
				 * get neighbours again, and add any that are missing
				 * */
			}		
			
			theGUI.filterSlider.setEnabled(true);
			oldFilterValue = theGUI.filterSlider.getValue();
		}else
		
		 if(e.getSource() == theGUI.corpusSlider){
			int  x = theGUI.corpusSlider.getValue();
			if(currentCorpus != corpuses.get(x)){
				currentCorpus = corpuses.get(x);
				updateToNewCorpus();
			}			
		}
		
	}
	
	/*Updates the shown map to the currentCorpus*/
	public void updateToNewCorpus(){
		Vector<Particle> particles = physics.getParticles();
		for(int c = 0; c < particles.size(); c++){
			WordNode tempNode = (WordNode) particles.get(c);
			if(currentCorpus.doesWordExist(tempNode.getWord())){
				tempNode.setInCurrentCorpus(true);
			}else{
				tempNode.setInCurrentCorpus(false);
			}
			if(tempNode.isNodeOpen()){
				
	
				
				closeNode(tempNode,false);
				openNode(tempNode, false);
				
				
				
			}
		}
	}

	/*Opens the browser at Google*/
	public void openLinkInGoogle() {
	        if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                try {
	                	String word = null;
	                	
	                	if(lastSelected != null){
	                		word = "q=" + lastSelected.getWord();
	                	}
	            			URI uri = new URI(
	            				    "http", 
	            				    "www.google.com", 
	            				    null,
	            				   word);
	            	
	                        desktop.browse(uri);
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	        } else {
	                // TODO: error handling
	        }
	    }

	/*Opens the browser at Wikipedia*/		
	public void openLinkInWikipedia() {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
               
                
               // http://en.wikipedia.org/w/index.php?title=Special:Search&search=dsfksdfdsf
                try {
                	String word = null;
                	
                	if(lastSelected != null){
                		word = lastSelected.getWord();
                	}
            			URI uri = new URI(
            				    "http", 
            				    "en.wikipedia.org", 
            				    "/wiki/" + word,
            				   null);
            	
                        desktop.browse(uri);
                } catch (Exception e) {
                   e.printStackTrace();
                }
        } else {
                // TODO: error handling
        }
    }
	
	/*Opens the browser at WMatrix*/
	public void openLinkInWMatrix() {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
               
                
               // http://en.wikipedia.org/w/index.php?title=Special:Search&search=dsfksdfdsf
                try {
                	String word = null;
                	
                	if(lastSelected != null){
                		word = lastSelected.getWord();
                	}
           
            			URI uri = new URI(
            				    "http", 
            				    "ucrel.lancs.ac.uk", 
            				    "/wmatrix/",
            				   "q=" + word);
            	
                        desktop.browse(uri);
                } catch (Exception e) {
                   e.printStackTrace();
                }
        } else {
                // TODO: error handling
        }
    }
	
	/*Opens the browser at Dictionary*/
	public void openLinkInDictionary() {
        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
               
                
                try {
                	String word = null;
                	
                	if(lastSelected != null){
                		word = lastSelected.getWord();
                	}
           
            			URI uri = new URI(
            				    "http", 
            				    "dictionary.reference.com", 
            				    "/browse/" + word,
            				   null);
            	
                        desktop.browse(uri);
                } catch (Exception e) {
                   e.printStackTrace();
                }
        } else {
                // TODO: error handling
        }
    }
	
	/*Opens the browser at Thesaurus*/
	public void openLinkInThesaurus() {
	        if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	               
	                
	             
	                try {
	                	String word = null;
	                	
	                	if(lastSelected != null){
	                		word = lastSelected.getWord();
	                	}
	           
	            			URI uri = new URI(
	            				    "http", 
	            				    "thesaurus.com", 
	            				    "/browse/" + word,
	            				    null);
	            	
	                        desktop.browse(uri);
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	        } else {
	                // TODO: error handling
	        }
	    }
			
    /*Unimplemented methods from interfaces below this line*/
	public void keyPressed(KeyEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}	
		

