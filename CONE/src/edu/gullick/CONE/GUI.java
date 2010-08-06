package edu.gullick.CONE;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;


public class GUI{

	/*
	 * GLOBAL VARIABLES
	 */
	public Vector<HistoryObject> history = new Vector<HistoryObject>();
	
	// the JFrame that everything is drawn on
	public JFrame theFrame = new JFrame("CONE: COllocation Network Explorer v0.5");
	
	/*The JPanel that all of the components are contained within*/
	public JPanel mainPanel = new JPanel(new BorderLayout());

	/*the Processing.core.PApplet that handles the graphics*/
	public Screen theScreen = null;
	
	/*the Controller - very very loose MVC*/
	public Controller theController = null;
	
	/* the Physics system */
	public Physics physics = null;
	
	/* Word Index for looking up words*/
	public WordIndex theWordIndex = null;
	
	public ImageIcon theLogo = null;
	
	/*JMenuBar*/
	public JMenuBar menuBar = new JMenuBar();
	public JMenu fileMenu = new JMenu("File");
	public JMenu editMenu = new JMenu("Edit");
	public JMenu toolsMenu= new JMenu("Tools");
	public JMenu helpMenu = new JMenu("Help");
	public JMenuItem importButton = new JMenuItem("Import Corpus");
	public JMenuItem cdataButton = new JMenuItem("Import .cdata file");
	public JMenuItem prefButton = new JMenuItem("Preferences");
	public JMenuItem resetButton = new JMenuItem("Reset");
	public JMenuItem centerButton = new JMenuItem("Center");
	public JMenuItem addButton = new JMenuItem("Add a Word");
	public JMenuItem tFilterButton = new JMenuItem("Adjust T-Filter");
	public JMenuItem deleteWordButton = new JMenuItem("Delete Word");
	public JMenuItem aboutButton = new JMenuItem("About");
	public JMenuItem helpButton = new JMenuItem("Help");
	public JMenuItem undoButton = new  JMenuItem("Undo");
	public JMenuItem redoButton = new  JMenuItem("Redo");

	public JMenuItem displayNodes = new  JRadioButtonMenuItem("Display Nodes");
	public JMenuItem displayEdges = new  JRadioButtonMenuItem("Display Edges");
	public JMenuItem displayWords = new  JRadioButtonMenuItem("Display Words");
	public JMenuItem displayForces = new  JRadioButtonMenuItem("Display Forces");
	public JMenuItem displayDebug = new  JRadioButtonMenuItem("Display Debug");
	public JMenuItem pauseButton = new  JRadioButtonMenuItem("Pause Physics");
	public JMenuItem smoothFontButton = new  JRadioButtonMenuItem("Smooth Font");
	public JMenuItem smoothAnimationButton = new  JRadioButtonMenuItem("Smooth Animation");
	public JMenuItem showIndex = new  JRadioButtonMenuItem("Show Index");
	public JMenuItem zoomInButton = new  JMenuItem("Zoom In");
	public JMenuItem zoomOutButton = new  JMenuItem("Zoom Out");
	
	/*Strings used in the help menu choices*/
	public String aboutText = "<html><b>CONE</b>: <b>CO</b>llocation <b>N</b>etwork <b>E</b>xplorer <i>v0.5</i>. <br/>Written by David Gullick at Lancaster University UK  (2010). <br/>Thanks to:<br/>Francois Taiani, <br/>Paul Rayson, <br/>John Mariani </html>";
	public String helpText = "<html>CONE is a graphical approach to exploring large bodies of text using collocation...<html>";


	/*Progress bar seen at the bottom of the page.*/
	public JProgressBar progBar = new JProgressBar();
	/*
	 * The constructor for the GUI class.
	 */
	
	public GUI( Controller theDriver,Screen theScreen,Physics physics, WordIndex theWordIndex, int width, int height){
	
		/*Setting up global variables*/
		this.theScreen = theScreen;
		this.theController = theDriver;
		this.physics = physics;
		this.theWordIndex = theWordIndex;
		
		theLogo = createImageIcon("/data/logo.png","the logo");
		
		/*Setting up the menu*/
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		fileMenu.add(importButton);
		importButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));	
		//fileMenu.add(cdataButton);	
		//fileMenu.add(prefButton);

		menuBar.add(fileMenu);
		
		toolsMenu.add(resetButton);
		resetButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		toolsMenu.add(centerButton);
		centerButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		toolsMenu.add(addButton);
		addButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		toolsMenu.add(tFilterButton);
		tFilterButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		toolsMenu.add(deleteWordButton);
		deleteWordButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
	
	
		displayNodes.setSelected(false);
		displayNodes.addItemListener(theController);
		toolsMenu.add(displayNodes);
		displayNodes.setAccelerator(KeyStroke.getKeyStroke('n'));

	
		displayEdges.setSelected(true);
		displayEdges.addItemListener(theController);
		toolsMenu.add(displayEdges);
		displayEdges.setAccelerator(KeyStroke.getKeyStroke('e'));
		
		displayForces.setSelected(false);
		displayForces.addItemListener(theController);
		toolsMenu.add(displayForces);
		//displayDebug.setAccelerator(KeyStroke.getKeyStroke(''));
		
		displayWords.setSelected(true);
		displayWords.addItemListener(theController);
		toolsMenu.add(displayWords);
		displayWords.setAccelerator(KeyStroke.getKeyStroke('w'));

		displayDebug.setSelected(false);
		displayDebug.addItemListener(theController);
		toolsMenu.add(displayDebug);
		displayDebug.setAccelerator(KeyStroke.getKeyStroke('d'));
		
		smoothFontButton.setSelected(false);
		smoothFontButton.addItemListener(theController);
		toolsMenu.add(smoothFontButton);
		smoothFontButton.setAccelerator(KeyStroke.getKeyStroke('f'));
		
		smoothAnimationButton.setSelected(false);
		smoothAnimationButton.addItemListener(theController);
		toolsMenu.add(smoothAnimationButton);
		smoothAnimationButton.setAccelerator(KeyStroke.getKeyStroke('a'));
		

		pauseButton.setSelected(false);
		pauseButton.addItemListener(theController);
		toolsMenu.add(pauseButton);
		pauseButton.setAccelerator(KeyStroke.getKeyStroke('p'));

		showIndex.setSelected(false);
		showIndex.addItemListener(theController);
		toolsMenu.add(showIndex);
		//showIndex.setAccelerator(KeyStroke.getKeyStroke('p'));

		zoomInButton.addActionListener(theController);
		toolsMenu.add(zoomInButton);
		zoomInButton.setAccelerator(KeyStroke.getKeyStroke('+'));
		
	
		zoomOutButton.addActionListener(theController);
		toolsMenu.add(zoomOutButton);
		zoomOutButton.setAccelerator(KeyStroke.getKeyStroke('-'));
		
		
		menuBar.add(editMenu);
		undoButton.addActionListener(theController);
		editMenu.add(undoButton);
		undoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		redoButton.addActionListener(theController);
		editMenu.add(redoButton);
		redoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		redoButton.setEnabled(false);
		
		menuBar.add(toolsMenu);
		helpMenu.add(helpButton);
		helpButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		helpMenu.add(aboutButton);
		aboutButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuBar.add(helpMenu);
		
		
		/*Adding action Listeners*/
		importButton.addActionListener(theController);
		cdataButton.addActionListener(theController);
		prefButton.addActionListener(theController);
		resetButton.addActionListener(theController);
		centerButton.addActionListener(theController);
		addButton.addActionListener(theController);
		tFilterButton.addActionListener(theController);
		aboutButton.addActionListener(theController);
		helpButton.addActionListener(theController);
		deleteWordButton.addActionListener(theController);
		
		
		
		/*Adding everything to the GUI*/
		mainPanel.add(theScreen,  BorderLayout.CENTER);
		theFrame.setJMenuBar(menuBar);
		
		/*Final frame configurations*/
		theFrame.setDefaultCloseOperation(3);
		theFrame.setContentPane(mainPanel);
		theFrame.setSize(width,height);
	
		/*Setting up the progress bar*/
		mainPanel.add(progBar,  BorderLayout.SOUTH);
		progBar.setStringPainted(true);
		setProgressBarString("No data loaded.");
		theFrame.setIconImage(theLogo.getImage());
		
		/*Starting the screen, and making the frame visable*/
		theScreen.init();	
		theFrame.setVisible(true);
	}
	
	
	/*Re-sets the progress bar*/
	public void initProgressbar(int lower, int higher){
		progBar.setMinimum(lower);
		progBar.setMaximum(higher);
		progBar.setValue(lower);
		progBar.setString("" + lower);
	}
	
	/*Sets the progress bar level*/
	public void setProgressBarLevel(int level){
		progBar.setValue(level);
	}
	
	/*Sets the note on the progress bar*/
	public void setProgressBarString(String s){
		progBar.setString(s);
	}
	
	protected ImageIcon createImageIcon(String path,
            String description) {
java.net.URL imgURL = getClass().getResource(path);

if (imgURL != null) {
return new ImageIcon(imgURL, description);
} else {
System.err.println("Couldn't find file: " + path);
return null;
}
}
	

	
	

	
}
