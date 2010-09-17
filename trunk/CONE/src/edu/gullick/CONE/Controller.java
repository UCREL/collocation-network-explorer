/*
 * 
 */
package edu.gullick.CONE;

import java.awt.Desktop;
import java.awt.Frame;
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
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLDocument.Iterator;

import edu.gullick.CONE.Corpus.LIMIT_TYPE;
import edu.gullick.physics2D.Particle;


/**
 * The Class Controller - the main class that controls everything that happens in the application.
 */
public class Controller extends JApplet implements ActionListener,
		ItemListener, MouseListener, ChangeListener, MouseMotionListener,
		WindowListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The initial WIDTH. */
	private final int WIDTH = 800;
	
	/** The initiail HEIGHT. */
	private final int HEIGHT = 600;
	
	/** The physics. */
	private Physics physics = null;
	
	/** The word index. */
	private WordIndex wordIndex = null;
	
	/** The screen. */
	private Screen theScreen = null;
	
	/** The gui. */
	private GUI theGUI = null;
	
	/** The side pane. */
	private SidePane sidePane = null;
	
	/** The being dragged. */
	private WordNode beingDragged = null;
	
	/** The dragging. */
	private boolean dragging = false;
	
	/** The particle drag start x. */
	private int particleDragStartX = 0;
	
	/** The particle drag start y. */
	private int particleDragStartY = 0;
	
	/** The locked. */
	private boolean locked = false;
	
	/** The drag start x. */
	private int dragStartX = 0;
	
	/** The drag start y. */
	private int dragStartY = 0;
	
	/** The drag start offset x. */
	private int dragStartOffsetX = 0;
	
	/** The drag start offset y. */
	private int dragStartOffsetY = 0;
	
	/** The adding new node. */
	private boolean addingNewNode = false;
	
	/** The new node word. */
	private String newNodeWord = "";
	
	/** The current nodes. */
	private LinkedHashMap<String, WordNode> currentNodes = new LinkedHashMap<String, WordNode>();
	
	/** The history. */
	private Vector<HistoryObject> history = new Vector<HistoryObject>();
	
	/** The last selected. */
	private WordNode lastSelected = null;
	
	/** The old filter value. */
	private int oldFilterValue = 50;
	
	/** the last value of the corpusSlider*/
	private int oldCorpusValue = 0;

	/** The corpuses. */
	private LinkedHashMap<Integer, Corpus> corpuses = new LinkedHashMap<Integer, Corpus>();
	
	/**last value of thefulterSlider */
	private int lastSliderVal = 0;
	
	/*boolean to decide whether execution is currently in the undo method (and if so, to not re-add something to history)**/
	boolean currentlyInUndoMethod = false;

	/*Current values for the corpuses and various percentages.*/
	private Corpus corpusA = null;
	private Corpus corpusB = null;
	private int percentageA = 0;
	private int percentageB = 0;
	
	
	/**
	 * The main method - when the application is launched as a stand alone application rather than an applet
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		new Controller();
	}

	
	

	/**
	 * Instantiates a new controller. Creates each of the necessary objects, and adds
	 * listeners etc. Also sets up a timer to update the physics 15 times per second.
	 */
	public Controller() {
		 SplashWindow splash = new SplashWindow();
		    splash.showSplash();
		physics = new Physics();
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					physics.step();
				} catch (Exception e) {
				}
			}
		}, 0, 67);
		wordIndex = new WordIndex();
		sidePane = new SidePane(this);
		theScreen = new Screen(physics);
		theGUI = new GUI(this, theScreen,wordIndex, WIDTH, HEIGHT);
		theScreen.addMouseListener(this);
		theScreen.addMouseMotionListener(this);
		theGUI.updateLabels(null);
	}

	
	
	/**
	 * listens for button presses in the GUI and calls other methods
	 * appropriately
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == theGUI.importButton) {
			importPressed();
		} else if (e.getSource() == theGUI.resetButton) {
			resetPressed();
		} else if (e.getSource() == theGUI.centerButton) {
			centerPressed(true);
		} else if (e.getSource() == theGUI.deleteWordButton) {
			deletePressed(true);
		} else if (e.getSource() == theGUI.addButton) {
			addPressed(true);
		} else if (e.getSource() == theGUI.aboutButton) {
			aboutPressed();
		} else if (e.getSource() == theGUI.undoButton) {
			undoPressed();
		} else if (e.getSource() == theGUI.helpButton) {
			helpPressed();
		} else if (e.getSource() == theGUI.zoomInButton) {
			zoomInPressed();
		} else if (e.getSource() == theGUI.zoomOutButton) {
			zoomOutPressed();
		} else if (e.getSource() == theGUI.linkGoogleButton) {
			openLinkInGoogle();
		} else if (e.getSource() == theGUI.linkWikipediaButton) {
			openLinkInWikipedia();
		} else if (e.getSource() == theGUI.linkWMatrixButton) {
			openLinkInWMatrix();
		} else if (e.getSource() == theGUI.linkThesaurusButton) {
			openLinkInThesaurus();
		} else if (e.getSource() == theGUI.linkDictionaryButton) {
			openLinkInDictionary();
		} else if (e.getSource() == theGUI.locateButton) {
			locatePressed();
		}else if (e.getSource() == sidePane.searchButton) {
			searchPressed();
		}
		//The following ensures that the frame gets focus after a click - so that shortcuts still work after this method!
		theGUI.menuBar.requestFocus();
	}

	
	
	/**
	 * Listens for button presses (toggle switches) in the GUI and calls the
	 * methods appropriately
	 * 
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
		if (source == theGUI.displayNodes) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayNodes(false);
			} else {
				toggleDisplayNodes(true);
			}
		} else if (source == theGUI.displayEdges) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayEdges(false);
			} else {
				toggleDisplayEdges(true);
			}
		} else if (source == theGUI.displayWords) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayWords(false);
			} else {
				toggleDisplayWords(true);
			}
		} else if (source == theGUI.displayDebug) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayDebug(false);
			} else {
				toggleDisplayDebug(true);
			}
		} else if (source == theGUI.displayForces) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayForces(false);
			} else {
				toggleDisplayForces(true);
			}
		} else if (source == theGUI.smoothFontButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleSmoothFont(false);
			} else {
				toggleSmoothFont(true);
			}
		} else if (source == theGUI.smoothAnimationButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleSmoothAnimation(false);
			} else {
				toggleSmoothAnimation(true);
			}
		} else if (source == theGUI.pauseButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				togglePause(false);
			} else {
				togglePause(true);
			}
		} else if (source == theGUI.showIndex) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleShowIndex(false);
			} else {
				toggleShowIndex(true);
			}
		}
		//the following ensures the menu still gets focus after the method calls, so that shortcuts still work
		theGUI.menuBar.requestFocus();
	}

	
	/**
	 * Method called if the mouse was released
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
		//if dragging a node
		if (beingDragged != null) {
			dragging = false;
			if (!beingDragged.isNodeOpen()) {
				beingDragged.makeFree();
			}
			if (beingDragged.position().x() != particleDragStartX
					|| beingDragged.position().y() != particleDragStartY) {
				history.add(
						0,
						new HistoryObject(HistoryObject.ACTION.DRAG_NODE,
								new ParticleHistory(beingDragged.getWord(),
										(int) (particleDragStartX / theScreen
												.getZoomLevel()),
										(int) (particleDragStartY / theScreen
												.getZoomLevel()))));
			}
			beingDragged = null;
		} else if (locked) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.DRAG_CANVAS,
					new Point(dragStartOffsetX, dragStartOffsetY)));
			dragging = false;
			locked = false;
		}
		theGUI.menuBar.requestFocus();
	}

	/** Method called if the user has dragged the mouse
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			beingDragged.position().setX(
					(float) ((e.getX() / theScreen.getZoomLevel()) - theScreen
							.getXoffset()));
			beingDragged.position().setY(
					(float) ((e.getY() / theScreen.getZoomLevel()) - theScreen
							.getYoffset()));
		} else if (locked) {
			theScreen.setXoffset((int) ((e.getX() - dragStartX) / theScreen
					.getZoomLevel()));
			theScreen.setYoffset((int) ((e.getY() - dragStartY) / theScreen
					.getZoomLevel()));
		}
	}

	/** Method called if the mouse is pressed 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 1) {
			if (addingNewNode) {
				addNodeToSystem(
						newNodeWord,
						(int) ((mouseEvent.getX() / theScreen.getZoomLevel()) - theScreen
								.getXoffset()),
						(int) ((mouseEvent.getY() / theScreen.getZoomLevel()) - theScreen
								.getYoffset()), true);
				addingNewNode = false;
				theScreen.setFollowPointer(false, "");
			} else {
				WordNode wn = null;
				int xpos = (int) (mouseEvent.getX() - (theScreen.getXoffset() * theScreen
						.getZoomLevel()));
				int ypos = (int) (mouseEvent.getY() - (theScreen.getYoffset() * theScreen
						.getZoomLevel()));

				for (int a = 0; a < physics.getParticles().size(); a++) {
					if (theScreen.mouseOver((WordNode) physics.getParticles()
							.get(a))) {
						wn = (WordNode) physics.getParticles().get(a);
					}
				}
				if (wn != null) {
					wn.makeFixed();
					if (lastSelected != null) {
						lastSelected.setSelected(false);
					}
					wn.setSelected(true);
					updateDetailsInScreen(wn);

					lastSelected = wn;

					theGUI.updateLabels(wn);
					particleDragStartX = xpos;
					particleDragStartY = ypos;
					beingDragged = wn;
					dragging = true;
				} else {
					locked = true;
					dragStartX = xpos;
					dragStartY = ypos;
					dragStartOffsetX = theScreen.getXoffset();
					dragStartOffsetY = theScreen.getYoffset();
					if(lastSelected != null){
						lastSelected.setSelected(false);
					}
					lastSelected = null;
					updateDetailsInScreen(null);
					
				}
			}
		} else if (mouseEvent.getClickCount() == 2) {
			for (int a = 0; a < physics.getParticles().size(); a++) {
				if (theScreen.mouseOver((WordNode) physics.getParticles()
						.get(a))) {
					WordNode wn = (WordNode) physics.getParticles().get(a);
					if (wn.isNodeOpen()) {
						closeNode(wn, true);
					} else {
						openNode(wn, true);
					}
					break;
				}
			}
		}
	}

	/** 
	 * Removes all of the attractions to a particular particle in the system
	 *
	 * @param wn the WordNode
	 */
	public void removeAllAttractionsToNode(WordNode wn) {
	
		for(WordNode temp : currentNodes.values()){
			WordAttraction x = temp.getAttractions().get(wn.getWord());
			temp.removeAttraction(wn.getWord());
			wn.removeAttraction(temp.getWord());
			physics.removeAttraction(x);
		}
			

	}


	/**
	 * Given a WordNode, this function will set its frequency given the current corpuses.
	 * @param wn th node to update
	 */
	public void updateNodeStatus(WordNode wn){
		wn.setFrequency(wordIndex.lookupCombinedFrequency(wn.getWord(), corpusA, percentageA, corpusB, percentageB));
	}
	
	/**
	 * Will update all of the links to a node given a new list.
	 * @param wn WordNode to update
	 * @param neighbours HashMap of links to neighours that the node needs to have
	 * @param willDelete whether the node will be deleted after this method call.
	 */
	public void updateNodeLinks(WordNode wn, LinkedHashMap<String ,LinkInformation> neighbours, boolean willDelete){
		LinkedHashMap<String ,WordLink> currentLinks = wn.getLinks();
		Vector<String> toRemove = new Vector<String>();
		Vector<String> toAdd = new Vector<String>();
		Vector<String> toChange = new Vector<String>();
		
		//first have to build a list to add, and remove.
		
		
		// if it is not in the current corpus, remove all of the 'open' links it has to others.
		if(wn.getFrequency() <= 0){
		
			for(Map.Entry<String ,WordLink> entry : currentLinks.entrySet()){
				toRemove.add( entry.getKey());
			}
		
		}else{
			
			//build up the list to add
			for(Map.Entry<String ,LinkInformation> entry : neighbours.entrySet()){
				String s = entry.getKey();
				LinkInformation li = entry.getValue();
				// if the new word would have a frequency of 0 or less, dont add it.
			
				// &&	wordIndex.lookupCombinedFrequency(s, corpusA, percentageA, corpusB, percentageB
				
				
					if(!currentLinks.containsKey(s) && li.getAffinity() > 0 && wordIndex.lookupCombinedFrequency(s, corpusA, percentageA, corpusB, percentageB) > 0){
						toAdd.add(s);
					}else if (currentLinks.containsKey(s) && (currentLinks.get(s).getEndRepresentingWord(s).getFrequency() <= 0 | li.getAffinity() <= 0)){
						//TODO: update this so it checks and is therefore more efficient
						toRemove.add(s);
					}else{
						toChange.add(s);
					}
		
			
			}
			
			
			//build up the list to remove
			for(Map.Entry<String ,WordLink> entry : currentLinks.entrySet()){
				String s = entry.getKey();
				if(!neighbours.containsKey(s) ){
					toRemove.add(s);
				}
			}
			
		}
		
		
		
		//First remove those that are not needed.
		for(String s : toRemove){
			WordNode otherNode = currentLinks.get(s).getEndRepresentingWord(s);
			if(!willDelete){
				if(otherNode.isNodeOpen() && otherNode.getFrequency() > 0){
					//not removing links as the other node is open + has multiple links
				}else if(otherNode.isNodeOpen() && otherNode.getFrequency() <= 0){
					removeLink(currentLinks.get(s),false);
				}else{
					removeLink(currentLinks.get(s),true);
				}
			}else{
				removeLink(currentLinks.get(s),false);
			}
		}
		
		//Add those that are needed
		for(String s : toAdd){
			LinkInformation tempInfo = neighbours.get(s);
			
			int xPosition =(int) ((wn.position().x()  - 50) + (Math.random()*100));
			int yPosition =(int) ((wn.position().y()  - 50) + (Math.random()*100));
			
			WordNode added = addNodeToSystem(s, xPosition, yPosition, false);
			//now must create a spring between the two
			double offset = 0 - wordIndex.totalMinAffinity;
			double lineLength = 500 - (((tempInfo.getAffinity() + offset) / (wordIndex.totalMaxAffinity + offset)) * 400);
			double lineWidth = (50 - (lineLength / 10)) * 2;
			WordLink tempSpring = new WordLink(wn, added, (int) lineLength, 0.2F, 0.4F, (int) lineWidth);
			wn.addLink(tempSpring);
			added.addLink(tempSpring);
			wn.addNeighbour(added);
			added.addNeighbour(wn);
			physics.addSpring(tempSpring);
		}
		//change the values in the change list
		for(String s : toChange){
			LinkInformation tempInfo = neighbours.get(s);
			//now must create a spring between the two
			double offset = 0 - wordIndex.totalMinAffinity;
			double lineLength = 500 - (((tempInfo.getAffinity() + offset) / (wordIndex.totalMaxAffinity + offset)) * 400);
			double lineWidth = (50 - (lineLength / 10)) * 2;
			WordLink theSpring = wn.getLinks().get(s);
			if(theSpring != null){
				theSpring.setRestLength((float) lineLength);
				theSpring.setThickness((int) lineWidth);
			}
		}
		
		
	}
	
	
	
	
	/**
	 * method called to open a given node
	 *
	 * @param wn the WordNode
	 * @param addToHistory the add to history
	 */
	public void openNode(WordNode wn, boolean addToHistory) {
		try {
			wn.makeFixed();
			wn.setNodeOpen(true);
			Corpus tempCorpusA = corpusA;
			Corpus tempCorpusB = corpusB;
			if(corpusA == null){
				tempCorpusA = new Corpus("");
			}
			if(corpusB == null){
				tempCorpusB = new Corpus("");
			}
			LinkedHashMap<String ,LinkInformation> neighbours = wordIndex.lookupCombinedWordNeighbours(wn.getWord(), tempCorpusA, percentageA, tempCorpusB, percentageB, Corpus.LIMIT_TYPE.NUMBER,  oldFilterValue);
			updateNodeLinks(wn, neighbours, false);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.EXPAND_NODE,
					wn.getWord()));
		}
	}


	/**
	 * Close node with extra parameters.
	 *
	 * @param wn the wn
	 * @param addToHistory the add to history
	 * @param removeNodes the remove nodes
	 */
	public void closeNode(WordNode wn, boolean addToHistory) {
		try {
			LinkedHashMap<String ,LinkInformation> neighbours = new LinkedHashMap<String, LinkInformation>();
			updateNodeLinks(wn, neighbours, false);
			wn.setNodeOpen(false);
			wn.makeFree();
		}catch(Exception e){
			e.printStackTrace();
		}
		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.CLOSE_NODE,
			wn.getWord()));
		}
	}

	/**
	 *  Is called if the user closes the sidePane window 
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		sidePane.theFrame.setVisible(false);
		theGUI.showIndex.setSelected(false);
	}



	
	/**
	 * Update details in screen.
	 *
	 * @param wn the WordNode
	 */
	public void updateDetailsInScreen(WordNode wn) {
		String wordInfo = "";
		
		if(wn == null){
			theScreen.setWordInfo(wordInfo);
			return;
		}
		
		int totalNumber = -1;
		LinkedHashMap<String, LinkInformation> neighbours = null;
		try {
			neighbours = wordIndex.lookupCombinedWordNeighbours(wn.getWord(), corpusA, percentageA, corpusB, percentageB, Corpus.LIMIT_TYPE.NUMBER,  oldFilterValue);
			totalNumber = wordIndex.lookupCombinedWordNeighbours(wn.getWord(), corpusA, percentageA, corpusB, percentageB, Corpus.LIMIT_TYPE.NUMBER,  100).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (neighbours != null) {
			wordInfo += "-------------------\n";
			wordInfo += wn.getWord() + "( " + neighbours.size() + "/" + totalNumber + " links ) \n";
			wordInfo += "-------------------\n";
			wordInfo += "Top " + Math.min(10, neighbours.size()) + ":\n";
			int limit = Math.min(10, neighbours.size());
			int count = 0;
			for(Map.Entry<String,LinkInformation> entry : neighbours.entrySet()){
				if(count <= limit){
				LinkInformation tempInfo = entry.getValue();
				wordInfo += tempInfo.getTheOtherWord(wn.getWord()) + "\t" + tempInfo.affinity	+ "\n";
				count++;
				}else{
					break;
				}
			}
		} else {
			wordInfo = "Error looking up word '" + wn.getWord() + "'";
		}
		theScreen.setWordInfo(wordInfo);
	}



	/**
	 * 	this function adds a node to the system, returning true if succesful, and
 	 * 	false otherwise
	 *
	 * @param word the word
	 * @param x the x
	 * @param y the y
	 * @param addToHistory the add to history
	 * @return the word node
	 */
	public WordNode addNodeToSystem(String word, int x, int y, boolean addToHistory) {
		if (!currentNodes.containsKey(word)) {
			WordNode temp = new WordNode(x, y, 1,  word, 0);
			int position = wordIndex.lookUpWordGlobally(word, corpuses);
			Double freq = wordIndex.lookupCombinedFrequency(word, corpusA, percentageA, corpusB, percentageB);
			
			
			//if lookup returned -1, it does not exist in any loaded corpus
			if(position == -1){
				return null;
			}
			
	
			//if the frequency is zero, it needs to have a line drawn through it, so set the variable.
			temp.setFrequency(freq);
	

			//adding attractions between this node and every other node.
			for (WordNode n : currentNodes.values()) {
				if (n != temp) {
					WordAttraction tempAttraction = new WordAttraction(temp, n,	-50000, 15);
					n.addAttraction(tempAttraction);
					temp.addAttraction(tempAttraction);
					physics.addAttraction(tempAttraction);
				}
			}

			physics.addParticle(temp);
			addToCurrentNodes(word.trim(), temp);
			if (addToHistory) {
				history.add(0, new HistoryObject(HistoryObject.ACTION.ADD_WORD,
						temp.getWord()));
			}
			return temp;
		}else{
			return currentNodes.get(word);
		}
	}

	
	private synchronized void addToCurrentNodes(String word, WordNode x){
		currentNodes.put(word, x);
	}
	

	/**
	 * this function removes the node from the system given a word
	 *
	 * @param word the word
	 * @param close the close
	 * @param removeSprings the remove springs
	 * @param removeAttractions the remove attractions
	 * @param addToHistory the add to history
	 * @return true, if successful
	 */
	public boolean removeNodeFromSystem(String word, boolean close,	boolean removeSprings, boolean removeAttractions, boolean addToHistory) {
		WordNode wn = null;
		if (currentNodes.containsKey(word)) {
			wn = currentNodes.get(word);
		} else {
			return false;
		}
		
		// close the node if necessary
		if (close && wn.isNodeOpen()) {
			closeNode(wn, false);
		}
		// remove attractions if necesary
		if (removeAttractions) {
			removeAllAttractionsToNode(wn);
		}
		// remove springs if necessary
		if (removeSprings) {
			updateNodeLinks(wn, new LinkedHashMap<String ,LinkInformation>(), true);
		}
		// Now remove node totally from physics and currentNodes list.
		currentNodes.remove(word);
		physics.removeParticle(wn);
		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.DELETE_NODE,
					new DeleteHistory(lastSelected.getWord(),
							(int) lastSelected.position().x(),
							(int) lastSelected.position().y(), close)));
		}
		return true;
	}

	/* #### Functions called for each button action below this line #### */

	/**
	 * Toggle display debug.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayDebug(boolean selection) {
		theScreen.setDisplayDebug(selection);
		theGUI.displayDebug.setSelected(selection);

	}

	/**
	 * Toggle display words.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayWords(boolean selection) {
		theScreen.setDisplayText(selection);
		theGUI.displayWords.setSelected(selection);

	}

	/**
	 * Toggle display edges.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayEdges(boolean selection) {
		theScreen.setDisplayEdges(selection);
		theGUI.displayEdges.setSelected(selection);
	}

	/**
	 * Toggle display forces.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayForces(boolean selection) {
		theScreen.setDisplayForces(selection);
		theGUI.displayForces.setSelected(selection);
	}

	/**
	 * Toggle display nodes.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayNodes(boolean selection) {
		theScreen.setDisplayNodes(selection);
		theGUI.displayNodes.setSelected(selection);
	}

	/**
	 * Toggle smooth font.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleSmoothFont(boolean selection) {
		theScreen.setSmoothFont(selection);
		theGUI.smoothFontButton.setSelected(selection);
	}

	/**
	 * Toggle smooth animation.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleSmoothAnimation(boolean selection) {
		theScreen.setSmoothAnimation(selection);
		theGUI.smoothAnimationButton.setSelected(selection);

	}

	/**
	 * Toggle pause.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void togglePause(boolean selection) {
		physics.setPaused(selection);
		theGUI.pauseButton.setSelected(selection);

	}

	/**
	 * Zoom in pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void zoomInPressed() {
		theScreen.zoomIn();
	}

	/**
	 * Zoom out pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void zoomOutPressed() {
		theScreen.zoomOut();
	}

	/**
	 * Toggle show index.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleShowIndex(boolean selection) {
		sidePane.theFrame.setVisible(selection);
	}

	/**
	 * Reset pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void resetPressed() {
		physics.clear();
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		currentNodes.clear();
		corpuses.clear();
		corpusA = null;
		corpusB = null;
		percentageA = 0;
		percentageB = 0;
		beingDragged = null;
		dragging = false;
		particleDragStartX = 0;
		particleDragStartY = 0;
		locked = false;
		dragStartX = 0;
		dragStartY = 0;
		dragStartOffsetX = 0;
		dragStartOffsetY = 0;
		addingNewNode = false;
		newNodeWord = "";
		lastSelected = null;
		oldFilterValue = 50;
		lastSliderVal = 0;
		theGUI.reset();
		history.clear();
		updateDetailsInScreen(null);

	}

	/**
	 * is called if the center button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void centerPressed(boolean addToHistory) {
		ScreenSet ss = new ScreenSet(theScreen.getXoffset(), theScreen.getYoffset(), theScreen.getZoomLevel());
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		if (addToHistory) {
			 history.add( 0,new
			 HistoryObject(HistoryObject.ACTION.CENTER_PHYSICS, ss ));
		}
	}

	/**
	 * is called if the delete button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void deletePressed(boolean addToHistory) {
		if (lastSelected == null) {
			JOptionPane.showMessageDialog(null,
					"Cannot delete as no node is selected.");
		} else {
			JOptionPane.showMessageDialog(null, "Deleting word '"
					+ lastSelected.getWord() + "' from the visualisation.");
			removeNodeFromSystem(lastSelected.getWord(),
					lastSelected.isNodeOpen(), true, true, addToHistory);
			lastSelected = null;
		}
	}


	
	/**
	 * called if the add button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void addPressed(boolean addToHistory) {
		SwingWorker<Vector<String>, Void> worker = new SwingWorker<Vector<String>, Void>() {
			String failedWords = "";
			
			public Vector<String> doInBackground() {
				String tempString = JOptionPane.showInputDialog(
						"Please type the words to add below, seperated by commas:").trim();
				
				StringTokenizer st = new StringTokenizer(tempString, ",");
				Vector<String> workedWords = new Vector<String>();
				
				
				try{
					while (st.hasMoreTokens()) {
						String s = st.nextToken().trim();
						if (wordIndex.lookUpWordGlobally(s, corpuses) != -1) {
								workedWords.add(s);
						 } else {
								failedWords += s + ", ";
					     }
					}	
					return workedWords;
				}catch(Exception e){
					e.printStackTrace();
					return workedWords;
				}
			}

			public void done() {
				Vector<String> temp = null;
				try {
					temp = get();
				} catch (NullPointerException ne) {
					// do nothing, Null pointer means the user cancelled.
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				if(temp == null | temp.size() == 0){
					//the user pressed cancel
				}else if(corpuses.size() == 0 ){
					JOptionPane.showMessageDialog(null,"No data is currently loaded. Please import atleast one '.cdata' file to continue.");
					importPressed();
				}else{
					for(int a = 0; a < temp.size(); a++){
						String s = temp.get(a).trim();
						if (currentNodes.containsKey(s)) {
							failedWords += s + ",\n";
							temp.remove(a);
							a--;
						}
					}
					
					if(temp.size() == 1){
						theScreen.setFollowPointer(true, temp.get(0));
						addingNewNode = true;
						newNodeWord = temp.get(0);
					}else{
						Double sqrt = Math.sqrt(temp.size());
						int size = (int) (sqrt + (1 - sqrt%1));		//size is the size of a square layout
						int centerX = theScreen.getCenterX();
						int centerY = theScreen.getCenterY();
						int width = theScreen.getPhysicsWidth();
						int height = theScreen.getPhysicsHeight();
						int widthSeperator = (width)/(size + 1);
						int heighSeperator = (height)/(size + 1);
						int leftEdge = centerX - (width/2) +  (widthSeperator/2);
						int topEdge = centerY - (height/2) + (heighSeperator/2);
						

						for(int z = 0 ; z < temp.size(); z++){
							int xPosition = leftEdge + ((z%size)*widthSeperator);
							int yPosition = topEdge + ((z/size)*heighSeperator);
							addNodeToSystem(temp.get(z), xPosition  ,yPosition  , true);
						}
						
						
					}
					
					
					if(!failedWords.equals("")){
						failedWords = failedWords.substring(0,failedWords.lastIndexOf(','));
						JOptionPane.showMessageDialog(null,"The following words were not added:\n" + failedWords + ".\nEither they already are in the vizualisation, or they dont collocate significantly.");
					}
				}
			}
		};
		worker.execute();
	}




	/**
	 * Called if the undo button is pressed
	 *
	 * @param addToHistory the add to history
	 */
	public void undoPressed() {
		currentlyInUndoMethod = true;
		if (history.size() > 0) {
			HistoryObject lastDone = history.get(0);
			System.out.println("Undoing an action " + lastDone.action);
			if (lastDone.action == HistoryObject.ACTION.EXPAND_NODE) {
				String wordToClose = currentNodes
						.get((String) lastDone.details).getWord();
				if (currentNodes.containsKey(wordToClose)) {
					closeNode((WordNode) currentNodes.get(wordToClose), false);
				}
			} else if (lastDone.action == HistoryObject.ACTION.CLOSE_NODE) {
				String wordToOpen = currentNodes.get((String) lastDone.details)
						.getWord();
				if (currentNodes.containsKey(wordToOpen)) {
					openNode((WordNode) currentNodes.get(wordToOpen), false);
				}
			} else if (lastDone.action == HistoryObject.ACTION.CENTER_PHYSICS) {
				ScreenSet ss = (ScreenSet) lastDone.details;
				theScreen.setXoffset(ss.getXOffset());
				theScreen.setYoffset(ss.getYOffset());
				theScreen.setZoomLevel(ss.getZoomLevel());
			} else if (lastDone.action == HistoryObject.ACTION.ADD_WORD) {
				String added = (String) lastDone.details;
				//if the last thing to be one was add a word, rmeove it again
				if (currentNodes.containsKey(added)) {
					WordNode temp = currentNodes.get(added);
					physics.removeParticle(temp);
					removeAllAttractionsToNode(temp);
					currentNodes.remove(added);
				}
			} else if (lastDone.action == HistoryObject.ACTION.DRAG_CANVAS) {
				Point hist = (Point) lastDone.details;
				theScreen.setXOffset(hist.x);
				theScreen.setYOffset(hist.y);
			} else if (lastDone.action == HistoryObject.ACTION.DRAG_NODE) {
				ParticleHistory hist = (ParticleHistory) lastDone.details;
				if (currentNodes.containsKey(hist.getWord())) {
					WordNode temp = currentNodes.get(hist.getWord());
					temp.position().set(hist.x, hist.y);
				}
			} else if (lastDone.action == HistoryObject.ACTION.DELETE_NODE) {
				// TODO: fix this!
				DeleteHistory details = (DeleteHistory) lastDone.details;
				WordNode temp = addNodeToSystem(details.getWord(),details.getX(), details.getY(), false);
				//if the node was close to delete it, re-open it
				if (details.isClosed()) {
					openNode(temp, false);
				}
			}else if(lastDone.action == HistoryObject.ACTION.CHANGE_CORPUS_SLIDER){
				theGUI.corpusSlider.setValue((Integer) lastDone.details);
				System.out.println("Gotta undo a corpus thing!");
			}else if(lastDone.action == HistoryObject.ACTION.CHANGE_FILTER_SLIDER){
				theGUI.filterSlider.setValue((Integer) lastDone.details);
			}else if(lastDone.action == HistoryObject.ACTION.CENTER_ON_NODE){
				ScreenSet ss = (ScreenSet) lastDone.details;
				theScreen.setXoffset(ss.getXOffset());
				theScreen.setYoffset(ss.getYOffset());
				theScreen.setZoomLevel(ss.getZoomLevel());
			}
			history.remove(lastDone);
			theGUI.menuBar.requestFocus();
		}
		currentlyInUndoMethod = false;
	}


	/**
	 * called if the help button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void helpPressed() {
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.helpText);
	}

	
	/**
	 * called when the about button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void aboutPressed() {
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.aboutText,
				"About", JOptionPane.INFORMATION_MESSAGE, theGUI.theLogo);
	}


	/**
	 * Called if the sidepane import button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void searchPressed() {
		String s = sidePane.getInputText().trim();
		if (wordIndex.lookUpWordGlobally(s, corpuses) == -1) {
			sidePane.setOutputText("That word is not significant in the corpus.");
		} else {
			LinkedHashMap<String, LinkInformation> results = null;
			try {
				results = wordIndex.lookupCombinedWordNeighbours(s, corpusA, percentageA, corpusB, percentageB, Corpus.LIMIT_TYPE.NUMBER, lastSliderVal);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (results != null) {
				String toSet = "--" + s + " --";
				for (LinkInformation li : results.values()) {
					toSet += li.getTheOtherWord(s) + ":" + li.getAffinity();
					//TODO: add frequency info here.
				}
				sidePane.setOutputText(toSet);
			} else {
				sidePane.setOutputText("Sorry, there was an error whilst querying the cdata file.");
			}

		}
	}

	
	
	public void locatePressed() {
		
		
		
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			public String doInBackground() {
				String tempString = JOptionPane.showInputDialog(
						"Please type the word to locate below:").trim();
				return tempString;
			}

			public void done() {
				String temp = null;
				try {
					temp = get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(temp == null){
					return;
				}else if(!currentNodes.containsKey(temp)){
					JOptionPane.showMessageDialog(null,"Sorry, that word is not currently in the visualization.");
				}else{
					WordNode tempNode = currentNodes.get(temp);
					JOptionPane.showMessageDialog(null,"Found the word '" + temp + "', press OK to center on it.");
					history.add(0, new HistoryObject(HistoryObject.ACTION.CENTER_ON_NODE,new ScreenSet(theScreen.getXoffset(), theScreen.getYoffset(), theScreen.getZoomLevel())));
					theScreen.center(tempNode);
				}
				
			}
		};
		worker.execute();

	}
	
	
	
	/**
	 * Method called if the import button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void importPressed() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			Boolean userCancelled = false;
			Corpus temp = null;
			File[] files = null;

			// This method automatically gets executed in a background thread

			public Void doInBackground() {
				String[] formats = new String[] { "cdata" };
				String description = "cdata files (*.cdata )";
				boolean allowFolders = false;
				String fileName = "";
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(true);
				chooser.setFileFilter(new ExtendedFileFilter(formats,
						description, allowFolders));
				chooser.showOpenDialog(theGUI.theFrame);
				files = chooser.getSelectedFiles();
				for (int y = 0; y < files.length; y++) {
					if (files[y] == null) {
						userCancelled = true;
						return null;
					}
					try {
						fileName = files[y].getName();
						temp = wordIndex.analyzeXMLDOC(files[y].getPath(),
								theGUI);
						Integer index = theGUI.addCorpusToSlider(files[y]
								.getName());
						corpuses.put(index, temp);
						if (index == 0) {
							corpusA = temp;
							corpusB = null;
							percentageA = 100;
							percentageB = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			public void done() {
				if (!userCancelled) {
					theGUI.setProgressBarString("Completed indexing of "
							+ files.length + " files.");
				}
			}
		};
		worker.execute();
		theGUI.filterSlider.setValue(50);
	}


	/**
	 * called if one of the Sliders is moved
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == theGUI.filterSlider) {
			if(!currentlyInUndoMethod){
				history.add(0,new HistoryObject(HistoryObject.ACTION.CHANGE_FILTER_SLIDER, new Integer(oldFilterValue)));
			}
			theGUI.filterSlider.setEnabled(false);
			oldFilterValue = theGUI.filterSlider.getValue();
			if(lastSelected != null ){
				updateDetailsInScreen(lastSelected);
			}
			theGUI.filterSlider.setEnabled(true);
		} else if (e.getSource() == theGUI.corpusSlider) {
			theGUI.corpusSlider.setEnabled(false);
			int x = theGUI.corpusSlider.getValue();
			if(!currentlyInUndoMethod){
				history.add(0,new HistoryObject(HistoryObject.ACTION.CHANGE_CORPUS_SLIDER, new Integer(oldCorpusValue)));
			
			}
			oldCorpusValue = x;
				int offset = 0;	
				int indexA = 0;
				int indexB = 0;
				if(x == theGUI.corpusSlider.getMaximum()){
					offset = 0;	
					indexA = x;
					indexB = x - 10;
					percentageA =  100 ;
					percentageB =  0;
				}else{
					offset = x%10;	
					indexA = x - offset;
					indexB = x + (10 - offset);
					percentageA =  100 - (10*offset);
					percentageB =  0 + (10*offset) ;
				}
				corpusA = corpuses.get(new Integer(indexA));
				corpusB = corpuses.get(new Integer(indexB));
				
		}
		
		for(WordNode wn : currentNodes.values()){
			updateNodeStatus(wn);
		}
		
		
	//TODO: fix this!	
		Object[] sArray =  currentNodes.keySet().toArray();
		
		for(int x = 0; x < sArray.length; x++){
			String s = (String) sArray[x];
			WordNode wn = currentNodes.get(s);
			if(wn != null && wn.isNodeOpen()){
				updateNodeLinks(wn,wordIndex.lookupCombinedWordNeighbours(wn.getWord(), corpusA, percentageA, corpusB, percentageB, Corpus.LIMIT_TYPE.NUMBER,  oldFilterValue), false);
			}
			
		}
		theGUI.corpusSlider.setEnabled(true);
	}

	
	
	public void removeLink(WordLink link, boolean deleteNodes){
		WordNode A = (WordNode) link.getOneEnd();
		WordNode B = (WordNode) link.getTheOtherEnd();
		
		A.getNeighbours().remove(B);
		B.getNeighbours().remove(A);
		// remove spring connecting them
		physics.removeSpring(link);
		A.removeLink(link);
		B.removeLink(link);
		
		if(deleteNodes){
			if(!A.isNodeOpen() && A.getLinks().size() == 0){
				removeAllAttractionsToNode(A);
				currentNodes.remove(A.getWord());
				physics.removeParticle(A);
			}
			
			if(!B.isNodeOpen() && B.getLinks().size() == 0){
				removeAllAttractionsToNode(B);
				currentNodes.remove(B.getWord());
				physics.removeParticle(B);
			}
		}
	}
	
	

	/**
	 *  Opens the browser at Google .
	 */
	public void openLinkInGoogle() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				String word = null;

				if (lastSelected != null) {
					word = "q=" + lastSelected.getWord();
				}
				URI uri = new URI("http", "www.google.com", null, word);

				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error handling
		}
	}

	
	/**
	 * Opens the browser at Wikipedia
	 */
	public void openLinkInWikipedia() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			// http://en.wikipedia.org/w/index.php?title=Special:Search&search=dsfksdfdsf
			try {
				String word = null;

				if (lastSelected != null) {
					word = lastSelected.getWord();
				}
				URI uri = new URI("http", "en.wikipedia.org", "/wiki/" + word,
						null);

				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error handling
		}
	}


	/**
	 * Opens the browser at WMatrix.
	 */
	public void openLinkInWMatrix() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			// http://en.wikipedia.org/w/index.php?title=Special:Search&search=dsfksdfdsf
			try {
				String word = null;

				if (lastSelected != null) {
					word = lastSelected.getWord();
				}

				URI uri = new URI("http", "ucrel.lancs.ac.uk", "/wmatrix/",
						"q=" + word);

				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error handling
		}
	}


	/**
	 * Opens the browser at Dictionary.
	 */
	public void openLinkInDictionary() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			try {
				String word = null;

				if (lastSelected != null) {
					word = lastSelected.getWord();
				}

				URI uri = new URI("http", "dictionary.reference.com",
						"/browse/" + word, null);

				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error handling
		}
	}


	/**
	 * Opens the browser at Thesaurus.
	 */
	public void openLinkInThesaurus() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			try {
				String word = null;

				if (lastSelected != null) {
					word = lastSelected.getWord();
				}

				URI uri = new URI("http", "thesaurus.com", "/browse/" + word,
						null);

				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO: error handling
		}
	}

	
	
	
	
	
	
	/* ********************************************************************** 
	 * Unimplemented methods from interfaces below this line 
	 * **********************************************************************/
	
	/**
	 * Key pressed.
	 *
	 * @param e the e
	 */
	public void keyPressed(KeyEvent e) {
	}

	/* 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/* 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	/* 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/* 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
	}
}  