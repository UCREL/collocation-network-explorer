package edu.gullick.CONE;

import java.awt.Color;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Controller extends JApplet implements ActionListener, ItemListener, MouseListener, MouseMotionListener, WindowListener{
	private static final long serialVersionUID = 1L;
	final int WIDTH = 800;
	final int HEIGHT = 600;
	Physics physics = null;
	WordIndex wordIndex = null;
	Screen theScreen = null;
	GUI theGUI = null;
	Controller theController = null;
	SidePane sidePane = null;
	WordNode beingDragged = null;
	boolean dragging = false;
	int particleDragStartX = 0;
	int particleDragStartY = 0;
	boolean locked = false;
	int dragStartX = 0;
	int dragStartY = 0;
	int dragStartOffsetX = 0;
	int dragStartOffsetY = 0;
	private boolean addingNewNode = false;
	private String newNodeWord = "undefined";
	private HashMap<String, WordNode> currentNodes = new HashMap<String,WordNode>();
	private Vector<HistoryObject> history = new Vector<HistoryObject>();
	private Vector<HistoryObject> toRedoHistory = new Vector<HistoryObject>();
	private boolean isUndoMove = false;
	
	private WordNode lastSelected = null;
	
	
	// normal execution, ran as an application
	public static void main(String[] args){
		new Controller();
	}
	

	/*The constructor method, creates each of the necessary objects, and adds listeners etc*/
	public Controller(){
		theController = this;
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
	}
	

	/*
	 * These are the Action events for the different buttons on the GUI.
	 * Long methods make use of a SwingWorker (essentially another thread)-useful
	 * for not crippling the performance of the GUI.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == theGUI.importButton){
		/*
		 * If the user has clicked on the import button, bring up a file select dialogue.
		 * Load into the WordIndex instance.
		 */
	    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		    	String fileName = "";
		    	Boolean userCancelled = false;
		        //This method automatically gets executed in a background thread

		        public Void doInBackground() {
		    		String[] formats = new String[] { "cdata"};
		    		String description = "cdata files (*.cdata )";
		    		boolean allowFolders = true;
		    		JFileChooser chooser = new JFileChooser();
		       		chooser.setFileFilter (new ExtendedFileFilter(formats,description,allowFolders));			
		    	    chooser.showOpenDialog(theGUI.theFrame);
		    	    File file = chooser.getSelectedFile();
		    	    if (file == null){
		    	    	userCancelled = true;
		    	    	return null;
		    	    }
		           	try {
						fileName = file.getName();
		        		wordIndex.analyzeXMLDOC(file.getPath(), theGUI);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
		        }
		        public void done() {	
		        	if(!userCancelled){
		        		theGUI.setProgressBarString("Completed indexing of " +  fileName + ".");
		        		theGUI.menuBar.requestFocus();
		        	}
		        } 
		    }; 
		worker.execute();
		}else if(e.getSource() == theGUI.resetButton){
			/*
			 * reset the physics  + the display
			 */
			physics.clear();
			theScreen.setXOffset(0);
			theScreen.setYOffset(0);
			theScreen.resetZoom();
			currentNodes.clear();
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.centerButton){
			/*
			 * reset the display values to default
			 */
			theScreen.setXOffset(0);
			theScreen.setYOffset(0);
			theScreen.resetZoom();
			history.add( 0,new HistoryObject(HistoryObject.ACTION.CENTER_PHYSICS, null));
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.deleteWordButton){
			if(lastSelected == null){
				System.out.println("User pressed delete, but no node is selected.");
				JOptionPane.showMessageDialog(null,"Cannot delete as no node is selected.");
			}else{
				JOptionPane.showMessageDialog(null,"Deleting word '" + lastSelected.getWord() + "' from the visualisation.");
				boolean didClose = false;
				if(lastSelected.isNodeOpen()){
					closeNode(lastSelected);
					didClose = true;
				}
				removeAllAttractionsToNode(lastSelected);
				removeAllSpringsToNode(lastSelected);
				currentNodes.remove(lastSelected.getWord());
				physics.removeParticle(lastSelected);
				history.add(new HistoryObject(HistoryObject.ACTION.DELETE_NODE, new DeleteHistory(lastSelected.getWord(),(int)lastSelected.position().x(), (int)lastSelected.position().y(), didClose )));
				lastSelected = null;
			}
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.addButton){
			/*
			 * If the user clicks the "add a word" button.
			 * It opens a dialogue and sets th screen to draw the targeting node.
			 */
			
		    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

		        //This method automatically gets executed in a background thread
		    	private String tempString = null;
		    	
		        public Boolean doInBackground() {
		        	tempString = JOptionPane.showInputDialog("Please type the word to add below:").trim(); 
		        	
		        	if(wordIndex.lookUpWord(tempString)){
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
		        		}
		        	}else if(!temp && tempString != null){
		        		JOptionPane.showMessageDialog(null,"That word does not appear in the corpus");
		        	}else{
		        		// the user has cancelled the box..
		        	}
		        	theGUI.menuBar.requestFocus();
		        }
		    }; 
			worker.execute();
		}else if(e.getSource() == theGUI.tFilterButton){
			history.add( 0,new HistoryObject(HistoryObject.ACTION.ADJUST_TFILTER, null));
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.aboutButton){
			/*
			 * If the user selects the  about button, display about
			 */
			JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.aboutText, "About",JOptionPane.INFORMATION_MESSAGE  , theGUI.theLogo);
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.undoButton){
			
				if(history.size() > 0){
					
				HistoryObject lastDone =  history.get(0) ;
				System.out.println("Undoing " +  lastDone.action);
				if(lastDone.action == HistoryObject.ACTION.EXPAND_NODE){
					if(currentNodes.containsKey((String)lastDone.details)){
						closeNode((WordNode) currentNodes.get((String)lastDone.details));
					}else{
						System.out.println("Error opening node!");
					}
				}else if(lastDone.action == HistoryObject.ACTION.CLOSE_NODE){
					if(currentNodes.containsKey((String)lastDone.details)){
						openNode((WordNode) currentNodes.get((String)lastDone.details));
					}else{
						System.out.println("Error closing node!");
					}
				}else if(lastDone.action == HistoryObject.ACTION.CENTER_PHYSICS){
					
				}else if(lastDone.action == HistoryObject.ACTION.ADD_WORD){
					WordNode added = (WordNode) lastDone.details;
					currentNodes.remove(added.getWord().trim());
					physics.removeParticle(added);
					removeAllAttractionsToNode(added);
				}else if(lastDone.action == HistoryObject.ACTION.DRAG_CANVAS){
					Point hist = (Point) lastDone.details;
					theScreen.setXOffset(hist.x);
					theScreen.setYOffset(hist.y);
				}else if(lastDone.action == HistoryObject.ACTION.DRAG_NODE){
					ParticleHistory hist = (ParticleHistory) lastDone.details;
					hist.particle.position().set(hist.x, hist.y);
				}else if(lastDone.action == HistoryObject.ACTION.DELETE_NODE){
					setUndoMove(true);
					DeleteHistory details = (DeleteHistory) lastDone.details;
					WordNode temp = new WordNode(details.getX(), details.getY(), 1, Color.BLUE,  details.getWord(), 0, 10);
					System.out.println(temp.getWord());
					physics.addParticle(temp);
					currentNodes.put(temp.getWord().trim(), temp);
					//create Node details.getWord() at details.getX() details.getY();
					if(details.isClosed()){
						openNode(temp);
					}
					setUndoMove(false);
					//TODO: NOT FINISHED YET -- attractions + forces?? (as source is not properly implemented)
				}else if(lastDone.action == HistoryObject.ACTION.ADJUST_TFILTER){
					//TODO: NOT IMPLEMENTED YET (as source is not properly implemented)
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setDisplayDebug(changeTo);
					theGUI.displayDebug.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setDisplayText(changeTo);
					theGUI.displayWords.setSelected(changeTo);
					setUndoMove(false);;
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setDisplayEdges(changeTo);
					theGUI.displayEdges.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setDisplayForces(changeTo);
					theGUI.displayForces.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_DISPLAY_NODES){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setDisplayNodes(changeTo);
					theGUI.displayNodes.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_FONT){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setSmoothAnimation(changeTo);
					theGUI.smoothAnimationButton.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					theScreen.setSmoothFont(changeTo);
					theGUI.smoothFontButton.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.PAUSE_PHYSICS){
					setUndoMove(true);
					boolean changeTo = (Boolean) lastDone.details;
					physics.setPaused(changeTo);
					theGUI.pauseButton.setSelected(changeTo);
					setUndoMove(false);
				}else if(lastDone.action == HistoryObject.ACTION.ZOOM_IN){
					theScreen.zoomOut();
				}else if(lastDone.action == HistoryObject.ACTION.ZOOM_OUT){
					theScreen.zoomIn();
				}
				history.remove(lastDone);
			}
			theGUI.menuBar.requestFocus();
		}else if(e.getSource() == theGUI.redoButton){
			System.out.println("The user clicked redo");
		}else if(e.getSource() == theGUI.helpButton){
			JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.helpText);
			theGUI.menuBar.requestFocus();
		}else if (e.getSource() == theGUI.zoomInButton) {
	    	theScreen.zoomIn();
	    	history.add( 0,new HistoryObject(HistoryObject.ACTION.ZOOM_IN, null));
	    	theGUI.menuBar.requestFocus();
	    }else if (e.getSource() == theGUI.zoomOutButton) {
			theScreen.zoomOut();
			history.add( 0,new HistoryObject(HistoryObject.ACTION.ZOOM_OUT, null));
			theGUI.menuBar.requestFocus();
	    } else if(e.getSource() == sidePane.searchButton){
	    	System.out.println(sidePane.getInputText().trim()   );
	    	if(wordIndex.lookUpWord(  sidePane.getInputText().trim()      )  == false ){
	    		sidePane.setOutputText("That word is not significant in the corpus.");
	    	}else{
	    		Vector<LinkInformation> results = null;
	    		try {
					results = wordIndex.lookupWordNeighbours( sidePane.getInputText().trim() );
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

	}

	private void removeAllSpringsToNode(WordNode wn) {
		while(wn.getLinks().size() > 0){
			WordLink tempLink = wn.getLinks().get(0);
		
				((WordNode)tempLink.getTheOtherEnd()).getLinks().remove(tempLink);
			
				((WordNode)tempLink.getOneEnd()).getLinks().remove(tempLink);
			
				physics.removeSpring(tempLink);
		}
		
		
		
	}


	/*
	 * Item state changes for the tick boxes on the menu. Each one toggles a variable in the screen. 
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
	
		if (source == theGUI.displayNodes) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayNodes(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_NODES, true));
			}else{
				theScreen.setDisplayNodes(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_NODES, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.displayEdges) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayEdges(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES, true));
			}else{
				theScreen.setDisplayEdges(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_EDGES, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.displayWords) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayText(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS, true));
			}else{
				theScreen.setDisplayText(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_WORDS, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.displayDebug) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayDebug(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG, true));
			}else{
				theScreen.setDisplayDebug(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_DEBUG, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.displayForces) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayForces(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES, true));
			}else{
				theScreen.setDisplayForces(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_DISPLAY_FORCES, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.smoothFontButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setSmoothFont(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_FONT, true));
			}else{
				theScreen.setSmoothFont(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_FONT, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.smoothAnimationButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setSmoothAnimation(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION, true));
			}else{
				theScreen.setSmoothAnimation(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.TOGGLE_SMOOTH_ANIMATION, false));
			}
			theGUI.menuBar.requestFocus();
	    } else if (source == theGUI.pauseButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				physics.setPaused(false);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.PAUSE_PHYSICS, true));
			}else{
				physics.setPaused(true);
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.PAUSE_PHYSICS, false));
			}
			theGUI.menuBar.requestFocus();
	    }else if (source == theGUI.showIndex) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				//
				sidePane.theFrame.setVisible(false); 
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.SHOW_INDEX, true));
			}else{
				sidePane.theFrame.setVisible(true); 
				if(!isUndoMove())history.add( 0,new HistoryObject(HistoryObject.ACTION.SHOW_INDEX, false));
			}
	
			theGUI.menuBar.requestFocus();
	    }
		
	}

	/*Method called if the mouse was released*/
	public void mouseReleased(MouseEvent arg0) {
		if(beingDragged != null){
			dragging = false;
			beingDragged.makeFree();
			history.add( 0,new HistoryObject(HistoryObject.ACTION.DRAG_NODE, new ParticleHistory(beingDragged,(int)( particleDragStartX/theScreen.getZoomLevel()),(int)(particleDragStartY/theScreen.getZoomLevel())) ));
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


	/*Method called if the key is released*/
	public void keyReleased(KeyEvent arg0){
		if(dragging){
			beingDragged.makeFree();
			beingDragged = null;
			dragging = false;
		}
		locked = false;
		theGUI.menuBar.requestFocus();
}
	
	/*Method called if the mouse is pressed*/
	public void mousePressed(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount()==1){
			if(addingNewNode){
				WordNode temp = new WordNode((int)((mouseEvent.getX() / theScreen.getZoomLevel()) - theScreen.getXoffset()   ), (int)((mouseEvent.getY() / theScreen.getZoomLevel()) - theScreen.getYoffset()   ), 1, Color.BLUE,  newNodeWord, 0, 10);
				physics.addParticle(temp);
				history.add( 0,new HistoryObject(HistoryObject.ACTION.ADD_WORD, temp));
				currentNodes.put(newNodeWord.trim(), temp);
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
					Vector<LinkInformation> neighbours = null;
					String wordInfo = "";
					try {
						neighbours = wordIndex.lookupWordNeighbours(wn.getWord());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					if(neighbours != null){
						wordInfo += "-------------------\n";
						wordInfo += wn.getWord() + "( " + neighbours.size() + " links ) \n";
						wordInfo += "-------------------\n";
						wordInfo += "Top 10:\n";
						
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
					lastSelected = wn;
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
						closeNode(wn);
						history.add( 0,new HistoryObject(HistoryObject.ACTION.CLOSE_NODE, wn.getWord()));
					}else{
						openNode(wn);
						history.add( 0,new HistoryObject(HistoryObject.ACTION.EXPAND_NODE, wn.getWord()));
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
	 
	/*Calld to open a closed Node*/
	/*method called to open a given node*/
	public void openNode(WordNode wn){
		try {
			wn.setMass(2147483647);
			wn.makeFixed();
			Vector<LinkInformation> neighbours = wordIndex.lookupWordNeighbours(wn.getWord());
			for(int x = 0;  x < neighbours.size();x++){
				LinkInformation tempInfo = neighbours.get(x);
				String word = "";
				if(wn.getWord().equals(tempInfo.getWord1())){
					word = tempInfo.getWord2().trim();
				}else{
					word = tempInfo.getWord1().trim();
				}
				if(!wn.getWord().equals(word)){
					WordNode tempNode = null;
					if(currentNodes.containsKey(word)){
						// current node Exists
						tempNode = currentNodes.get(word);	
					}else{
						//node doesnt already exist (so have to create a new node first)
						tempNode = new WordNode( wn.position().x() +25 + ((int)(100*Math.random())), wn.position().y() + ((int)(100*Math.random())), 1, Color.BLUE,  word, 0, 10);
						physics.addParticle(tempNode);
						currentNodes.put(word.trim(), tempNode);
					}
					if(physics.getSpring(tempNode, wn) == null){
						double offset = 0-wordIndex.minAffinity;
						double lineLength = 500 - (((tempInfo.getAffinity() + offset)/(wordIndex.maxAffinity + offset))*400) ;
						double lineWidth = 50 - (lineLength/10);
						WordLink tempSpring = new WordLink(wn,tempNode,(int)lineLength,0.2F,0.4F,Color.BLUE, (int)lineWidth, 1);
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
					wn.setNodeOpen(true);
					wn.makeFree();
				}
			}
		}catch (Exception e) {}
	}
	
	/*Called to close an open Node*/
	/*Method to close a given node*/
	public void closeNode(WordNode wn){
		Vector<WordNode> neighbours = wn.getNeighbours();
		
		for(int x = neighbours.size() - 1; x >=0; x--){
			WordNode tempNode = neighbours.get(x);
			if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() == 1){
				// other node is a closed node, and only connected to this one. Remove it
				WordLink tempSpring = (WordLink) tempNode.getSpringTo(wn);
			 	removeAllAttractionsToNode(tempNode);
				wn.getNeighbours().remove(tempNode);
				physics.removeSpring(tempSpring);
				physics.removeParticle(tempNode);
				wn.removeLink(tempSpring);
				currentNodes.remove(tempNode.getWord());
			}else if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() > 1){
				//remove link, but dont delete node
				WordLink tempSpring = (WordLink) tempNode.getSpringTo(wn);
				//remove references to each other
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				//remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
			}
		}
		wn.setNodeOpen(false);
		wn.setMass(1);
	}

	/*Is called if the user closes the sidePane window*/
	public void windowClosing(WindowEvent e) {
		sidePane.theFrame.setVisible(false);
		theGUI.showIndex.setSelected(false);
	}
	
	public void setUndoMove( boolean isUndoMove ) {
		this.isUndoMove = isUndoMove;
	}

	public boolean isUndoMove() {
		return isUndoMove;
	}
	
	
	
	
	
	/*#####Unimplimented methods from interfaces below this line!!!!####*/
	
	public void keyPressed(KeyEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseMoved(MouseEvent arg0) {}


}	
		

