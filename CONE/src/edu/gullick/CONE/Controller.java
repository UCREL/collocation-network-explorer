/*
 * 
 */
package edu.gullick.CONE;

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
import java.net.URI;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.gullick.physics2D.Particle;


/**
 * The Class Controller.
 */
public class Controller extends JApplet implements ActionListener,
		ItemListener, MouseListener, ChangeListener, MouseMotionListener,
		WindowListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The WIDTH. */
	private final int WIDTH = 800;
	
	/** The HEIGHT. */
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
	private String newNodeWord = "undefined";
	
	/** The current nodes. */
	private HashMap<String, WordNode> currentNodes = new HashMap<String, WordNode>();
	
	/** The history. */
	private Vector<HistoryObject> history = new Vector<HistoryObject>();
	
	/** The last selected. */
	private WordNode lastSelected = null;
	
	/** The current corpus. */
	private Corpus currentCorpus = null;
	
	/** The old filter value. */
	private int oldFilterValue = 50;

	/** The corpuses. */
	private HashMap<Integer, Corpus> corpuses = new HashMap<Integer, Corpus>();

	
	
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
			importPressed(false);
		} else if (e.getSource() == theGUI.resetButton) {
			resetPressed(false);
		} else if (e.getSource() == theGUI.centerButton) {
			centerPressed(true);
		} else if (e.getSource() == theGUI.deleteWordButton) {
			deletePressed(true);
		} else if (e.getSource() == theGUI.addButton) {
			addPressed(true);
		} else if (e.getSource() == theGUI.tFilterButton) {
			adjustTFilterPressed(true);
		} else if (e.getSource() == theGUI.aboutButton) {
			aboutPressed(false);
		} else if (e.getSource() == theGUI.undoButton) {
			undoPressed(false);
		} else if (e.getSource() == theGUI.helpButton) {
			helpPressed(false);
		} else if (e.getSource() == theGUI.zoomInButton) {
			zoomInPressed(true);
		} else if (e.getSource() == theGUI.zoomOutButton) {
			zoomOutPressed(true);
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
		} else if (e.getSource() == sidePane.searchButton) {
			searchPressed(false);
		}
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
				toggleDisplayNodes(false, true);
			} else {
				toggleDisplayNodes(true, true);
			}
		} else if (source == theGUI.displayEdges) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayEdges(false, true);
			} else {
				toggleDisplayEdges(true, true);
			}
		} else if (source == theGUI.displayWords) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayWords(false, true);
			} else {
				toggleDisplayWords(true, true);
			}
		} else if (source == theGUI.displayDebug) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayDebug(false, true);
			} else {
				toggleDisplayDebug(true, true);
			}
		} else if (source == theGUI.displayForces) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleDisplayForces(false, true);
			} else {
				toggleDisplayForces(true, true);
			}
		} else if (source == theGUI.smoothFontButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleSmoothFont(false, true);
			} else {
				toggleSmoothFont(true, true);
			}
		} else if (source == theGUI.smoothAnimationButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleSmoothAnimation(false, true);
			} else {
				toggleSmoothAnimation(true, true);
			}
		} else if (source == theGUI.pauseButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				togglePause(false, true);
			} else {
				togglePause(true, true);
			}
		} else if (source == theGUI.showIndex) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				toggleShowIndex(false, true);
			} else {
				toggleShowIndex(true, true);
			}
		}
		theGUI.menuBar.requestFocus();
	}

	
	/**
	 * Method called if the mouse was released
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
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
		while (wn.getAttractions().size() > 0) {
			WordAttraction temp = wn.getAttractions().get(0);
			((WordNode) temp.getTheOtherEnd()).removeAttraction(temp);
			((WordNode) temp.getOneEnd()).removeAttraction(temp);
			physics.removeAttraction(temp);
		}

	}


	/**
	 * method called to open a given node
	 *
	 * @param wn the WordNode
	 * @param addToHistory the add to history
	 */
	public void openNode(WordNode wn, boolean addToHistory) {
		System.out.println("Opening: " + wn.getWord());
		try {
			wn.makeFixed();
			wn.setNodeOpen(true);
			Vector<LinkInformation> neighbours = currentCorpus.getNeighbours(
					wn.getWord(), Corpus.LIMIT_TYPE.PERCENTAGE,
					theGUI.filterSlider.getValue());
			for (int x = 0; x < neighbours.size(); x++) {
				LinkInformation tempInfo = neighbours.get(x);
				String word = "";
				boolean wasWord1 = false;
				if (wn.getWord().equals(tempInfo.getWord1())) {
					word = tempInfo.getWord2().trim();
					wasWord1 = false;
				} else {
					word = tempInfo.getWord1().trim();
					wasWord1 = true;
				}
				if (!wn.getWord().equals(word)) {
					WordNode tempNode = null;
					if (currentNodes.containsKey(word)) {
						// current node Exists
						tempNode = currentNodes.get(word);
					} else {
						// node doesnt already exist (so have to create a new
						// node first)
						tempNode = new WordNode(wn.position().x() + 25
								+ ((int) (100 * Math.random())), wn.position()
								.y() + ((int) (100 * Math.random())), 1,
								 word, 0);
						if (wasWord1) {
							tempNode.setFrequency(tempInfo.frequency1);
						} else {
							tempNode.setFrequency(tempInfo.frequency2);
						}
						physics.addParticle(tempNode);
						currentNodes.put(word.trim(), tempNode);
					}
					if (physics.getSpring(tempNode, wn) == null) {
						double offset = 0 - wordIndex.totalMinAffinity;
						double lineLength = 500 - (((tempInfo.getAffinity() + offset) / (wordIndex.totalMaxAffinity + offset)) * 400);
						double lineWidth = 50 - (lineLength / 10);
						WordLink tempSpring = new WordLink(wn, tempNode,(int) lineLength, 0.2F, 0.4F,(int) lineWidth, 1);
						physics.addSpring(tempSpring);
						tempNode.addNeighbour(wn);
						wn.addNeighbour(tempNode);
						tempNode.addLink(tempSpring);
						wn.addLink(tempSpring);
						// add a repulsion between every other node and this one
						for (int a = 0; a < physics.getParticles().size(); a++) {
							WordNode n = (WordNode) physics.getParticles().get(
									a);

							if (n != tempNode) {
								WordAttraction tempAttraction = new WordAttraction(
										tempNode, n, -50000, 15);
								n.addAttraction(tempAttraction);
								tempNode.addAttraction(tempAttraction);
								physics.addAttraction(tempAttraction);
							}
						}
					}

				}

			}
		} catch (Exception e) {
		}

		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.EXPAND_NODE,
					wn.getWord()));
		}
	}

	/**
	 * Close a node.
	 *
	 * @param wn the wn
	 * @param addToHistory the add to history
	 */
	public void closeNode(WordNode wn, boolean addToHistory) {
		closeNode(wn, addToHistory, true);
	}

	/**
	 * Close node with extra parameters.
	 *
	 * @param wn the wn
	 * @param addToHistory the add to history
	 * @param removeNodes the remove nodes
	 */
	public void closeNode(WordNode wn, boolean addToHistory, boolean removeNodes) {
		System.out.println("Closing: " + wn.getWord());
		Vector<WordLink> links = wn.getLinks();

		for (int x = 0; x < links.size();) {
			boolean didDelete = false;
			WordLink tempSpring = links.get(x);
			WordNode tempNode = null;
			if (tempSpring.getOneEnd() == wn) {
				tempNode = (WordNode) tempSpring.getTheOtherEnd();
			} else {
				tempNode = (WordNode) tempSpring.getOneEnd();
			}

			if (!tempNode.isNodeOpen() && tempNode.getNeighbours().size() == 1) {
				// other node is a closed node, and only connected to this one.
				// Remove it

				if (removeNodes) {
					removeAllAttractionsToNode(tempNode);
				}
				wn.getNeighbours().remove(tempNode);
				physics.removeSpring(tempSpring);
				if (removeNodes) {
					physics.removeParticle(tempNode);
				}

				wn.removeLink(tempSpring);
				if (removeNodes) {
					currentNodes.remove(tempNode.getWord());
				}
				didDelete = true;
			} else if (!tempNode.isNodeOpen()
					&& tempNode.getNeighbours().size() > 1) {
				// remove references to each other
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				// remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
				didDelete = true;
			} else if (tempNode.isNodeOpen() && !tempNode.isInCurrentCorpus()) {
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				// remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
				didDelete = true;
			}

			if (!didDelete) {
				x++;
			}
		}
		wn.setNodeOpen(false);
		wn.makeFree();

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
	 * Removes the all springs to node.
	 *
	 * @param wn the WordNode
	 */
	public void removeAllSpringsToNode(WordNode wn) {
		while (wn.getLinks().size() > 0) {
			WordLink tempLink = wn.getLinks().get(0);

			((WordNode) tempLink.getTheOtherEnd()).getLinks().remove(tempLink);

			((WordNode) tempLink.getOneEnd()).getLinks().remove(tempLink);

			physics.removeSpring(tempLink);
		}
	}

	
	/**
	 * Update details in screen.
	 *
	 * @param wn the WordNode
	 */
	public void updateDetailsInScreen(WordNode wn) {
		Vector<LinkInformation> neighbours = null;
		String wordInfo = "";
		try {
			neighbours = currentCorpus.getNeighbours(wn.getWord(),
					Corpus.LIMIT_TYPE.PERCENTAGE,
					theGUI.filterSlider.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (neighbours != null) {
			wordInfo += "-------------------\n";
			wordInfo += wn.getWord() + "( " + neighbours.size() + " links ) \n";
			wordInfo += "-------------------\n";
			wordInfo += "Top " + Math.min(10, neighbours.size()) + ":\n";
			int limit = Math.min(10, neighbours.size());
			for (int x = 0; x < limit; x++) {
				LinkInformation tempInfo = neighbours.get(x);
				if (tempInfo.word1.equals(wn.getWord().trim())) {
					wordInfo += tempInfo.word2 + "\t" + tempInfo.affinity
							+ "\n";
				} else {
					wordInfo += tempInfo.word1 + "\t" + tempInfo.affinity
							+ "\n";
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
	public WordNode addNodeToSystem(String word, int x, int y,
			boolean addToHistory) {
		if (!currentNodes.containsKey(word)
				&& wordIndex.lookUpWord(word, currentCorpus)) {
			WordNode temp = new WordNode(x, y, 1,  word, 0);

			Vector<LinkInformation> wordInfo = currentCorpus.getNeighbours(
					word, Corpus.LIMIT_TYPE.NUMBER, 1);

			if (wordInfo.get(0).word1.equals(word)) {
				temp.setFrequency(wordInfo.get(0).frequency1);
			} else {
				temp.setFrequency(wordInfo.get(0).frequency2);
			}

			for (int a = 0; a < physics.getParticles().size(); a++) {
				WordNode n = (WordNode) physics.getParticles().get(a);
				if (n != temp) {
					WordAttraction tempAttraction = new WordAttraction(temp, n,
							-50000, 15);
					n.addAttraction(tempAttraction);
					temp.addAttraction(tempAttraction);
					physics.addAttraction(tempAttraction);
				}
			}

			physics.addParticle(temp);
			if (addToHistory) {
				history.add(0, new HistoryObject(HistoryObject.ACTION.ADD_WORD,
						temp.getWord()));
			}
			currentNodes.put(word.trim(), temp);
			
			return temp;
		} else if (currentNodes.containsKey(word)) {
			return currentNodes.get(word);
		} else {
			return null;
		}
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
	public boolean removeNodeFromSystem(String word, boolean close,
			boolean removeSprings, boolean removeAttractions,
			boolean addToHistory) {
		WordNode wn = null;
		if (currentNodes.containsKey(word)) {
			wn = currentNodes.get(word);
		} else {
			return false;
		}
		// close the node if necessary
		if (close) {
			closeNode(wn, false);
		}
		// remove attractions if necesary
		if (removeAttractions) {
			removeAllAttractionsToNode(wn);
		}
		// remove springs if necessary
		if (removeSprings) {
			removeAllSpringsToNode(wn);
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
	public void toggleDisplayDebug(boolean selection, boolean addToHistory) {
		theScreen.setDisplayDebug(selection);
		theGUI.displayDebug.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG, selection));
		}
	}

	/**
	 * Toggle display words.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayWords(boolean selection, boolean addToHistory) {
		theScreen.setDisplayText(selection);
		theGUI.displayWords.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS, selection));
		}
	}

	/**
	 * Toggle display edges.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayEdges(boolean selection, boolean addToHistory) {
		theScreen.setDisplayEdges(selection);
		theGUI.displayEdges.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES, selection));
		}
	}

	/**
	 * Toggle display forces.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayForces(boolean selection, boolean addToHistory) {
		theScreen.setDisplayForces(selection);
		theGUI.displayForces.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES, selection));
		}
	}

	/**
	 * Toggle display nodes.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleDisplayNodes(boolean selection, boolean addToHistory) {
		theScreen.setDisplayNodes(selection);
		theGUI.displayNodes.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_DISPLAY_NODES, selection));
		}
	}

	/**
	 * Toggle smooth font.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleSmoothFont(boolean selection, boolean addToHistory) {
		theScreen.setSmoothFont(selection);
		theGUI.smoothFontButton.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_SMOOTH_FONT, selection));
		}
	}

	/**
	 * Toggle smooth animation.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleSmoothAnimation(boolean selection, boolean addToHistory) {
		theScreen.setSmoothAnimation(selection);
		theGUI.smoothAnimationButton.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION, selection));
		}
	}

	/**
	 * Toggle pause.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void togglePause(boolean selection, boolean addToHistory) {
		physics.setPaused(selection);
		theGUI.pauseButton.setSelected(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.PAUSE_PHYSICS, true));
		}
	}

	/**
	 * Zoom in pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void zoomInPressed(boolean addToHistory) {
		theScreen.zoomIn();
		if (addToHistory) {
			history.add(0,
					new HistoryObject(HistoryObject.ACTION.ZOOM_IN, null));
		}
	}

	/**
	 * Zoom out pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void zoomOutPressed(boolean addToHistory) {
		theScreen.zoomOut();
		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.ZOOM_OUT,
					null));
		}
	}

	/**
	 * Toggle show index.
	 *
	 * @param selection the selection
	 * @param addToHistory the add to history
	 */
	public void toggleShowIndex(boolean selection, boolean addToHistory) {
		sidePane.theFrame.setVisible(selection);
		if (addToHistory) {
			history.add(0, new HistoryObject(HistoryObject.ACTION.SHOW_INDEX,
					selection));
		}

	}

	/**
	 * Reset pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void resetPressed(boolean addToHistory) {
		physics.clear();
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		currentNodes.clear();
		if (addToHistory) {
			// TODO: addHistory
		}
	}

	/**
	 * is called if the center button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void centerPressed(boolean addToHistory) {
		theScreen.setXOffset(0);
		theScreen.setYOffset(0);
		theScreen.resetZoom();
		if (addToHistory) {
			// history.add( 0,new
			// HistoryObject(HistoryObject.ACTION.CENTER_PHYSICS, null));
			// TODO: fix the null in the statement above.
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
		// TODO: fix this below!
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
			private String tempString = null;

			public Boolean doInBackground() {
				tempString = JOptionPane.showInputDialog(
						"Please type the word to add below:").trim();

				if (wordIndex.lookUpWord(tempString, currentCorpus)) {
					return true;
				} else {
					return false;
				}
			}

			public void done() {
				Boolean temp = false;
				try {
					temp = get();
				} catch (Exception ignore) {
				}
				if (temp && tempString != null) {
					if (!currentNodes.containsKey(tempString)) {
						theScreen.setFollowPointer(true, tempString);
						addingNewNode = true;
						newNodeWord = tempString;
					} else {
						JOptionPane
								.showMessageDialog(null,
										"That word already appears in the visualisation.");
						WordNode tempNode = currentNodes.get(tempString);
						theScreen.center(tempNode);
						if (lastSelected != null) {
							lastSelected.setSelected(false);
						}
						tempNode.setSelected(true);
						updateDetailsInScreen(tempNode);
					}
				} else if (!temp && tempString != null) {
					JOptionPane.showMessageDialog(null,
							"That word does not appear in the corpus");
				} else {
					// the user has cancelled the box..
				}
			}
		};
		worker.execute();
	}


	/**
	 * Called if the adjust tfilter was pressed
	 * TODO: remove as no longer need this button!
	 *
	 * @param addToHistory the add to history
	 */
	public void adjustTFilterPressed(boolean addToHistory) {
		// TODO: REMOVE THIS AS IT IS NO ONGER NEEDED
		if (addToHistory) {
			history.add(0, new HistoryObject(
					HistoryObject.ACTION.ADJUST_TFILTER, null));
		}
	}


	/**
	 * Called if the undo button is pressed
	 *
	 * @param addToHistory the add to history
	 */
	public void undoPressed(boolean addToHistory) {
		if (history.size() > 0) {
			HistoryObject lastDone = history.get(0);
			System.out.println("Undoing an action " + lastDone.action);
			if (lastDone.action == HistoryObject.ACTION.EXPAND_NODE) {
				String wordToClose = currentNodes
						.get((String) lastDone.details).getWord();
				if (currentNodes.containsKey(wordToClose)) {
					closeNode((WordNode) currentNodes.get(wordToClose), false);
				} else {
					System.out.println("Error closing node!");
				}
			} else if (lastDone.action == HistoryObject.ACTION.CLOSE_NODE) {
				String wordToOpen = currentNodes.get((String) lastDone.details)
						.getWord();
				if (currentNodes.containsKey(wordToOpen)) {
					openNode((WordNode) currentNodes.get(wordToOpen), false);
				} else {
					System.out.println("Error opening node!");
				}
			} else if (lastDone.action == HistoryObject.ACTION.CENTER_PHYSICS) {
				// TODO: fix this
			} else if (lastDone.action == HistoryObject.ACTION.ADD_WORD) {
				String added = (String) lastDone.details;
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
				WordNode temp = new WordNode(details.getX(), details.getY(), 1,
						 details.getWord(), 0);
				System.out.println(temp.getWord());
				physics.addParticle(temp);
				currentNodes.put(temp.getWord().trim(), temp);
				// create Node details.getWord() at details.getX()
				// details.getY();
				if (details.isClosed()) {
					openNode(temp, false);
				}
			} else if (lastDone.action == HistoryObject.ACTION.ADJUST_TFILTER) {
				// TODO: NOT IMPLEMENTED YET (as source is not properly
				// implemented)
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG) {
				toggleDisplayDebug((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS) {
				toggleDisplayWords((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES) {
				toggleDisplayEdges((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES) {
				toggleDisplayForces((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_NODES) {
				toggleDisplayNodes((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_FONT) {
				toggleSmoothFont((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION) {
				toggleSmoothAnimation((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.PAUSE_PHYSICS) {
				togglePause((Boolean) lastDone.details, false);
			} else if (lastDone.action == HistoryObject.ACTION.ZOOM_IN) {
				zoomOutPressed(false);
			} else if (lastDone.action == HistoryObject.ACTION.ZOOM_OUT) {
				zoomInPressed(false);
			}
			history.remove(lastDone);
			theGUI.menuBar.requestFocus();
		}
	}


	/**
	 * called if the help button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void helpPressed(boolean addToHistory) {
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.helpText);
		if (addToHistory) {
			// TODO: fix this
		}
	}

	
	/**
	 * called when the about button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void aboutPressed(boolean addToHistory) {
		JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.aboutText,
				"About", JOptionPane.INFORMATION_MESSAGE, theGUI.theLogo);
		if (addToHistory) {
			// TODO: fix this
		}
	}


	/**
	 * Called if the sidepane import button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void searchPressed(boolean addToHistory) {
		// TODO: FIX THIS PART BELOW
		System.out.println(sidePane.getInputText().trim());
		if (wordIndex.lookUpWord(sidePane.getInputText().trim(), currentCorpus) == false) {
			sidePane.setOutputText("That word is not significant in the corpus.");
		} else {
			Vector<LinkInformation> results = null;
			try {
				results = wordIndex.lookupWordNeighbours(sidePane
						.getInputText().trim(), currentCorpus);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if (results != null) {
				String toSet = "";
				for (int x = 0; x < results.size(); x++) {
					toSet += results.get(x).getWord1() + " : "
							+ results.get(x).getWord2() + "  : "
							+ results.get(x).getAffinity() + "\n";
				}
				sidePane.setOutputText(toSet);
			} else {
				sidePane.setOutputText("Sorry, there was an error whilst querying the cdata file.");
			}

		}
	}

	/**
	 * Method called if the import button is pressed.
	 *
	 * @param addToHistory the add to history
	 */
	public void importPressed(boolean addToHistory) {
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
						if (index == 1) {
							currentCorpus = corpuses.get(1);
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
		/*
		 * TODO::: complete this section
		 */

		if (e.getSource() == theGUI.filterSlider) {
			theGUI.filterSlider.setEnabled(false);
			if (lastSelected != null) {
				updateDetailsInScreen(lastSelected);
			}
			theGUI.filterSlider.setEnabled(false);
			int x = theGUI.filterSlider.getValue();
			if (oldFilterValue > x) {
				System.out.println("lowering the slider");
				Vector<Particle> particles = physics.getParticles();
				for (int a = 0; a < particles.size(); a++) {
					WordNode wn = (WordNode) particles.get(a);
					if (wn.isNodeOpen()) {
						Vector<LinkInformation> inverseNeighbours = currentCorpus
								.getInverseNeighbours(wn.getWord(),
										Corpus.LIMIT_TYPE.PERCENTAGE, x);

						for (int d = 0; d < inverseNeighbours.size(); d++) {
							String theWord = "";
							if (wn.getWord().equals(
									inverseNeighbours.get(d).getWord1())) {
								theWord = inverseNeighbours.get(d).getWord2()
										.trim();
							} else {
								theWord = inverseNeighbours.get(d).getWord1()
										.trim();
							}

							WordNode tempNode = currentNodes.get(theWord);
							if (wn.getNeighbours().contains(tempNode)) {
								// contains a thing which must be deleted!
								// TODO: combine this int oa handy little
								// function
								WordLink tempSpring = wn.getSpringTo(tempNode);
								if (!tempNode.isNodeOpen()
										&& tempNode.getNeighbours().size() == 1) {
									removeAllAttractionsToNode(tempNode);
									wn.getNeighbours().remove(tempNode);
									physics.removeSpring(tempSpring);
									physics.removeParticle(tempNode);
									wn.removeLink(tempSpring);
									currentNodes.remove(tempNode.getWord());
								} else if (tempNode.getNeighbours().size() > 1) {
									System.out.println("Removing link between "
											+ tempNode.getWord() + " and "
											+ wn.getWord());
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
			} else if (oldFilterValue < x) {
				System.out.println("increasing the slider");
				// get all of the new neighbours, and go through comparing..
				Vector<Particle> particles = physics.getParticles();
				for (int a = 0; a < particles.size(); a++) {
					WordNode wn = (WordNode) particles.get(a);
					if (wn.isNodeOpen()) {
						Vector<LinkInformation> neighbours = currentCorpus
								.getNeighbours(wn.getWord(),
										Corpus.LIMIT_TYPE.PERCENTAGE, x);
						for (int b = 0; b < neighbours.size(); b++) {

							String theWord = "";
							boolean wasWord1 = false;
							if (wn.getWord().equals(
									neighbours.get(b).getWord1())) {
								theWord = neighbours.get(b).getWord2().trim();
								wasWord1 = true;
							} else {
								theWord = neighbours.get(b).getWord1().trim();
								wasWord1 = false;
							}

							if (!wn.getNeighbours().contains(theWord)) {

								/*
								 * TODO:: Make this into a seperate function,
								 * along with open node
								 */

								WordNode tempNode = null;
								if (currentNodes.containsKey(theWord)) {
									// current node Exists
									tempNode = currentNodes.get(theWord);
								} else {
									// node doesnt already exist (so have to
									// create a new node first)
									tempNode = new WordNode(wn.position().x()
											+ 25
											+ ((int) (100 * Math.random())), wn
											.position().y()
											+ ((int) (100 * Math.random())), 1,
											 theWord, 0);
									if (wasWord1) {
										tempNode.setFrequency(neighbours.get(b).frequency1);
									} else {
										tempNode.setFrequency(neighbours.get(b).frequency2);
									}
									physics.addParticle(tempNode);
									currentNodes.put(theWord.trim(), tempNode);
								}
								if (physics.getSpring(tempNode, wn) == null) {
									double offset = 0 - wordIndex.totalMinAffinity;
									double lineLength = 500 - (((neighbours
											.get(b).getAffinity() + offset) / (wordIndex.totalMaxAffinity + offset)) * 400);
									double lineWidth = 50 - (lineLength / 10);
									WordLink tempSpring = new WordLink(wn,tempNode, (int) lineLength, 0.2F, 0.4F, (int) lineWidth,
											1);
									physics.addSpring(tempSpring);
									tempNode.addNeighbour(wn);
									wn.addNeighbour(tempNode);
									tempNode.addLink(tempSpring);
									wn.addLink(tempSpring);
									// add a repulsion between every other node
									// and this one
									for (int c = 0; c < physics.getParticles()
											.size(); c++) {
										WordNode n = (WordNode) physics
												.getParticles().get(c);
										if (n != tempNode) {
											WordAttraction tempAttraction = new WordAttraction(
													tempNode, n, -50000, 15);
											n.addAttraction(tempAttraction);
											tempNode.addAttraction(tempAttraction);
											physics.addAttraction(tempAttraction);
										}
									}
								}

							}

						}
					}

				}

				/*
				 * get neighbours again, and add any that are missing
				 */
			}

			theGUI.filterSlider.setEnabled(true);
			oldFilterValue = theGUI.filterSlider.getValue();
		} else

		if (e.getSource() == theGUI.corpusSlider) {
			int x = theGUI.corpusSlider.getValue();
			if (currentCorpus != corpuses.get(x)) {
				currentCorpus = corpuses.get(x);
				updateToNewCorpus();
			}
		}

	}

	
	
	/**
	 * Updates the shown map to the currentCorpus
	 */
	public void updateToNewCorpus() {
		Vector<Particle> particles = physics.getParticles();
		for (int c = 0; c < particles.size(); c++) {
			WordNode tempNode = (WordNode) particles.get(c);
			if (currentCorpus.doesWordExist(tempNode.getWord())) {
				tempNode.setInCurrentCorpus(true);
			} else {
				tempNode.setInCurrentCorpus(false);
			}
			if (tempNode.isNodeOpen()) {

				closeNode(tempNode, false);
				openNode(tempNode, false);

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
