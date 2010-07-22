package edu.gullick.CONE;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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




public class Controller extends JApplet implements ActionListener, ItemListener, KeyListener, MouseListener, MouseMotionListener, WindowListener{
	 

	private static final long serialVersionUID = 1L;
	
	final int WIDTH = 800;
	final int HEIGHT = 600;
	final int NUMBEROFNODES = 20;
	
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
	
	private boolean addingNewNode = false;
	private String newNodeWord = "undefined";
	
	private HashMap<String, WordNode> currentNodes = new HashMap<String,WordNode>();
		
	
	// normal execution, ran as an application
	public static void main(String[] args){
		new Controller();
	}
	


    
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
		theScreen.addKeyListener(this);
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
		        		theGUI.theFrame.requestFocus();
		        	}
		        } 
		    }; 
		worker.execute();
		}else if(e.getSource() == theGUI.cdataButton){
			/*
			 * TODO:
			 * If user clicks on the cdata button (currently not implemented)
			 */
		}else if(e.getSource() == theGUI.prefButton){
			/*
			 * TODO:
			 * If user clicks on the preferences button (currently not implemented)
			 */
		}else if(e.getSource() == theGUI.resetButton){
			/*
			 * reset the physics  + the display
			 */
			physics.clear();
			theScreen.setXOffset(0);
			theScreen.setYOffset(0);
			theScreen.resetZoom();
			currentNodes.clear();
			theGUI.theFrame.requestFocus();
		}else if(e.getSource() == theGUI.centerButton){
			/*
			 * reset the display values to default
			 */
			theScreen.setXOffset(0);
			theScreen.setYOffset(0);
			theScreen.resetZoom();
			theGUI.theFrame.requestFocus();
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
		        		theScreen.setFollowPointer(true, tempString);
		        		addingNewNode = true;
		        		newNodeWord = tempString;
		        	}else if(!temp && tempString != null){
		        		JOptionPane.showMessageDialog(null,"That word does not appear in the corpus");
		        	}else{
		        		// the user has cancelled the box..
		        	}
		        	theGUI.theFrame.requestFocus();
		        }
		    }; 
			worker.execute();
		}else if(e.getSource() == theGUI.tFilterButton){
			/*
			 * TODO:
			 * If the user clicks the adjust filter button.
			 */
			theGUI.theFrame.requestFocus();
		}else if(e.getSource() == theGUI.aboutButton){
			/*
			 * If the user selects the  about button, display about
			 */
			
			
			JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.aboutText, "About",JOptionPane.INFORMATION_MESSAGE  , theGUI.theLogo);
			theGUI.theFrame.requestFocus();
		}else if(e.getSource() == theGUI.helpButton){
			/*
			 * If the user selects the  help button, display help
			 */
			JOptionPane.showMessageDialog(theGUI.theFrame, theGUI.helpText);
			theGUI.theFrame.requestFocus();
		}else if (e.getSource() == theGUI.zoomInButton) {
	    	theScreen.zoomIn();
	    	theGUI.theFrame.requestFocus();
	    }else if (e.getSource() == theGUI.zoomOutButton) {
			theScreen.zoomOut();
			theGUI.theFrame.requestFocus();
	    } else if(e.getSource() == sidePane.searchButton){
	    	System.out.println(sidePane.getInputText().trim()   );
	    	if(wordIndex.lookUpWord(  sidePane.getInputText().trim()      )  == false ){
	    		sidePane.setOutputText("That word is not significant in the corpus.");
	    	}else{
	    		Vector<LinkInformation> results = null;
	    		try {
					results = wordIndex.lookupWordNeighbours( sidePane.getInputText().trim() );
				} catch (Exception e1) {
					// TODO Auto-generated catch block
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
			}else{
				theScreen.setDisplayNodes(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.displayEdges) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayEdges(false);
			}else{
				theScreen.setDisplayEdges(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.displayWords) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayText(false);
			}else{
				theScreen.setDisplayText(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.displayDebug) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayDebug(false);
			}else{
				theScreen.setDisplayDebug(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.displayForces) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setDisplayForces(false);
			}else{
				theScreen.setDisplayForces(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.smoothFontButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setSmoothFont(false);
			}else{
				theScreen.setSmoothFont(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.smoothAnimationButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				theScreen.setSmoothAnimation(false);
			}else{
				theScreen.setSmoothAnimation(true);
			}
			theGUI.theFrame.requestFocus();
	    } else if (source == theGUI.pauseButton) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				physics.setPaused(false);
			}else{
				physics.setPaused(true);
			}
			theGUI.theFrame.requestFocus();
	    }else if (source == theGUI.showIndex) {
			if (e.getStateChange() == ItemEvent.DESELECTED){
				//
				sidePane.theFrame.setVisible(false); 
			}else{
				sidePane.theFrame.setVisible(true); 
			}
			theGUI.theFrame.requestFocus();
	    }
		
	}




	/*Key events - zooms in when the user types + and zooms out on -*/
	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == '-'){
			 theScreen.zoomOut();
		}else if(e.getKeyChar() == '+'){
			theScreen.zoomIn();
		}
	}
	
	
	
	
	/*
	 * Methods called when user interacts with the screen via mouseclicks/movement 
	 */
	
	
	
	
	
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		dragging = false;
		if(beingDragged != null){
			beingDragged.makeFree();
			beingDragged = null;
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragging){
			 beingDragged.position().setX((float) ((e.getX() /theScreen.getZoomLevel()) - theScreen.getXoffset()));
			 beingDragged.position().setY( (float)((e.getY() /theScreen.getZoomLevel()) - theScreen.getYoffset()));
		}else if(locked) {
			theScreen.setXoffset((int) ((e.getX()-dragStartX) / theScreen.getZoomLevel())); 
			theScreen.setYoffset((int) ((e.getY()-dragStartY)/ theScreen.getZoomLevel())); 
		}
	}

	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	public void keyReleased(KeyEvent arg0){
		if(dragging){
			beingDragged.makeFree();
			beingDragged = null;
			dragging = false;
		}
		locked = false;
	}
	
	
	
	
	
	/*
	 * Unimplimented methods from Interfaces
	 */
	public void keyPressed(KeyEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent mouseEvent) {
		
		if (mouseEvent.getClickCount()==1){
			if(addingNewNode){
				//TODO add a new Node to physics
				WordNode temp = new WordNode((int)((mouseEvent.getX() / theScreen.getZoomLevel()) - theScreen.getXoffset()   ), (int)((mouseEvent.getY() / theScreen.getZoomLevel()) - theScreen.getYoffset()   ), 1, Color.BLUE,  newNodeWord, 0, 10);
				physics.addParticle(temp);
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
					particleDragStartX =  xpos; 
					particleDragStartY =  ypos; 
					beingDragged = wn;
					dragging = true;
				 }else{
					locked = true; 
				 	dragStartX =  xpos; 
				 	dragStartY =  ypos; 
				 }
			}
		}else if(mouseEvent.getClickCount()==2){
		
			for(int a = 0;  a < physics.getParticles().size(); a++){
				
				
			
			//TODO some wierd bug going on here...?
				if(theScreen.mouseOver((WordNode)physics.getParticles().get(a))){
					WordNode wn = (WordNode) physics.getParticles().get(a);
					if(wn.isNodeOpen()){
						closeNode(wn);
					}else{
						openNode(wn);
					}
					break;
					
				}
			}
			
		}
		
	}
		
				
	 
	
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
	
	public void removeAllAttractionsToNode(WordNode wn){
		for(int x = 0;x < wn.getAttractions().size();x++){
			physics.removeAttraction(wn.getAttractions().get(x));
		}
	}
	
	public void closeNode(WordNode wn){
		boolean toDeleteNode = true;
		Vector<WordNode> neighbours = wn.getNeighbours();

		for(int x = neighbours.size() - 1; x >=0; x--){
			WordNode tempNode = neighbours.get(x);
//TODO: make more efficient
			if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() == 1){
				// other node is a closed node, and only connected to this one. Remove it
				WordLink tempSpring = (WordLink) physics.getSpring(tempNode, wn);
				removeAllAttractionsToNode(tempNode);
				wn.getNeighbours().remove(tempNode);
				physics.removeSpring(tempSpring);
				physics.removeParticle(tempNode);
				currentNodes.remove(tempNode.getWord());
			}else if(!tempNode.isNodeOpen() && tempNode.getNeighbours().size() > 1){
		
				//remove link, but dont delete node
				WordLink tempSpring = (WordLink) physics.getSpring(tempNode, wn);
				
				//remove references to each other
				wn.getNeighbours().remove(tempNode);
				tempNode.getNeighbours().remove(wn);
				
				//remove spring connecting them
				physics.removeSpring(tempSpring);
				tempNode.removeLink(tempSpring);
				wn.removeLink(tempSpring);
				
				
			}else if(tempNode.isNodeOpen()){
				toDeleteNode = false;
			}
			
		}
		
		if(toDeleteNode){
			physics.removeParticle(wn);
			currentNodes.remove(wn.getWord());
			removeAllAttractionsToNode(wn);
		}
		
		
		wn.setNodeOpen(false);
		wn.setMass(1);
	}




	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		sidePane.theFrame.setVisible(false);
		theGUI.showIndex.setSelected(false);
		
	}




	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	
		
	}




	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}
}	
		

