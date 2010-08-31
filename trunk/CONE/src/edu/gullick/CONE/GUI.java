package edu.gullick.CONE;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Dictionary;
import java.util.Hashtable;
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
	public JPanel lowerPanel = new JPanel(new GridLayout(3,0));
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
	public JMenu linkMenu = new JMenu("Look up a word in..");
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
	
	public JMenuItem	linkGoogleButton = new JMenuItem("Google.com");
	public JMenuItem	linkWikipediaButton = new JMenuItem("Wikipedia.org");
	public JMenuItem 	linkWMatrixButton = new JMenuItem("WMatrix");
	public JMenuItem	linkThesaurusButton = new JMenuItem("Thesaurus.com");
	public JMenuItem 	linkDictionaryButton = new JMenuItem("Dictionary.com");
	
	public JRadioButton displayNodes = new  JRadioButton("Display Nodes");
	public JRadioButton displayEdges = new  JRadioButton("Display Edges");
	public JRadioButton displayWords = new  JRadioButton("Display Words");
	public JRadioButton displayForces = new  JRadioButton("Display Forces");
	public JRadioButton displayDebug = new  JRadioButton("Display Debug");
	public JMenuItem pauseButton = new  JRadioButtonMenuItem("Pause Physics");
	public JRadioButton smoothFontButton = new  JRadioButton("Smooth Font");
	public JRadioButton smoothAnimationButton = new  JRadioButton("Smooth Animation");
	public JRadioButton showIndex = new  JRadioButton("Show Index");
	public JMenuItem zoomInButton = new  JMenuItem("Zoom In");
	public JMenuItem zoomOutButton = new  JMenuItem("Zoom Out");

	public JTabbedPane  tabbedPane = new JTabbedPane();
	

	JPanel settingsPanel =  new JPanel(new GridLayout(0,2));
	
	
	/*Strings used in the help menu choices*/
	public String aboutText = "<html><b>CONE</b>: <b>CO</b>llocation <b>N</b>etwork <b>E</b>xplorer <i>v0.5</i>. <br/>Written by David Gullick at Lancaster University UK  (2010). <br/>Thanks to:<br/>Francois Taiani, <br/>Paul Rayson, <br/>John Mariani </html>";
	public String helpText = "<html>CONE is a graphical approach to exploring large bodies of text using collocation...<html>";

	public JSlider filterSlider = new JSlider(JSlider.HORIZONTAL,0,100,50);
	public JSlider corpusSlider = new JSlider(JSlider.HORIZONTAL,0,0,0);
	
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
		settingsPanel.add(displayNodes);
		//displayNodes.setAccelerator(KeyStroke.getKeyStroke('n'));

	
		displayEdges.setSelected(true);
		displayEdges.addItemListener(theController);
		settingsPanel.add(displayEdges);
		//displayEdges.setAccelerator(KeyStroke.getKeyStroke('e'));
		
		displayForces.setSelected(false);
		displayForces.addItemListener(theController);
		settingsPanel.add(displayForces);
		//displayDebug.setAccelerator(KeyStroke.getKeyStroke(''));
		
		displayWords.setSelected(true);
		displayWords.addItemListener(theController);
		settingsPanel.add(displayWords);
		//displayWords.setAccelerator(KeyStroke.getKeyStroke('w'));

		displayDebug.setSelected(false);
		displayDebug.addItemListener(theController);
		settingsPanel.add(displayDebug);
		//displayDebug.setAccelerator(KeyStroke.getKeyStroke('d'));
		
		smoothFontButton.setSelected(false);
		smoothFontButton.addItemListener(theController);
		settingsPanel.add(smoothFontButton);
		//smoothFontButton.setAccelerator(KeyStroke.getKeyStroke('f'));
		
		smoothAnimationButton.setSelected(false);
		smoothAnimationButton.addItemListener(theController);
		settingsPanel.add(smoothAnimationButton);
		//smoothAnimationButton.setAccelerator(KeyStroke.getKeyStroke('a'));
		

		pauseButton.setSelected(false);
		pauseButton.addItemListener(theController);
		toolsMenu.add(pauseButton);
		pauseButton.setAccelerator(KeyStroke.getKeyStroke('p'));

		showIndex.setSelected(false);
		showIndex.addItemListener(theController);
		settingsPanel.add(showIndex);
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
		
		menuBar.add(toolsMenu);
		helpMenu.add(helpButton);
		helpButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		helpMenu.add(aboutButton);
		aboutButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuBar.add(helpMenu);
		
	
		 linkGoogleButton.setForeground(Color.BLUE);
		 linkGoogleButton.setToolTipText("Google");
		 linkGoogleButton.addActionListener(theController);
		 

		 linkWikipediaButton.setForeground(Color.BLUE);
		 linkWikipediaButton.setToolTipText("Wikipeida");
		 linkWikipediaButton.addActionListener(theController);

		
	
		 linkWMatrixButton.setForeground(Color.BLUE);
		 linkWMatrixButton.setToolTipText("WMatrix");
		 linkWMatrixButton.addActionListener(theController);

		 linkThesaurusButton.setForeground(Color.BLUE);
		 linkThesaurusButton.setToolTipText("Thesaurus");
		 linkThesaurusButton.addActionListener(theController);

		 linkDictionaryButton.setForeground(Color.BLUE);
		 linkDictionaryButton.setToolTipText("Dictionary");
		 linkDictionaryButton.addActionListener(theController);

		 
		 
		 
		linkMenu.add(linkGoogleButton);
		linkMenu.add(linkWikipediaButton);
		linkMenu.add(linkWMatrixButton);
		linkMenu.add(linkThesaurusButton);
		linkMenu.add(linkDictionaryButton);
		menuBar.add(linkMenu);

		
		
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
		
		filterSlider.addChangeListener(theController);
		filterSlider.setMajorTickSpacing(10);
		filterSlider.setMinorTickSpacing(5);
		filterSlider.setPaintTicks(true);
		filterSlider.setPaintLabels(true);
	
		corpusSlider.addChangeListener(theController);
		corpusSlider.setMajorTickSpacing(1);
		corpusSlider.setPaintTicks(true);
		corpusSlider.setPaintLabels(true);
		corpusSlider.setEnabled(false);
		corpusSlider.setSnapToTicks(true);
	
		/*Adding everything to the GUI*/
		mainPanel.add(theScreen,  BorderLayout.CENTER);
		theFrame.setJMenuBar(menuBar);
		
		
		/*Final frame configurations*/
		theFrame.setDefaultCloseOperation(3);
		tabbedPane.addTab("Visualisation", null, mainPanel, "Visualisation");
		tabbedPane.addTab("Settings", null, settingsPanel, "Settings");
		theFrame.setContentPane(tabbedPane);
		theFrame.setSize(width,height);
	
		/*Setting up the progress bar*/
		lowerPanel.add(filterSlider);
		lowerPanel.add(corpusSlider);
		lowerPanel.add(progBar);
		mainPanel.add(lowerPanel,  BorderLayout.SOUTH);
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
	
public void updateLabels(WordNode wn){
	if(wn == null){
		linkMenu.setEnabled(false);
		deleteWordButton.setEnabled(false);
		linkMenu.setText("Please select a word..");
	}else{
		linkMenu.setEnabled(true);
		deleteWordButton.setEnabled(true);
		linkMenu.setText("Look up '" + wn.getWord() + "' in..");
	}
}

public String makeLabelString(String s, int key){
	String toReturn = "<html><font size='1'>";
	
		if(key%2==0){
			toReturn += "<br/>";
		}
	
		toReturn += s;
	
	
	toReturn +="</font></html>";
	return toReturn;
}

public int addCorpusToSlider(String label){
	
	
	/*If the slider currently has no entries*/
	if(corpusSlider.getMaximum() == 0){
		Dictionary<Integer, Component> labels = new Hashtable<Integer, Component>();
		corpusSlider.setEnabled(true);
		corpusSlider.setMinimum(1);
		corpusSlider.setMaximum(1);
		corpusSlider.setValue(1);
		
		JLabel l = new JLabel( makeLabelString(label, 1));
		labels.put(1, l);
		
		corpusSlider.setLabelTable(labels);
		corpusSlider.setEnabled(false);
		corpusSlider.validate();
		corpusSlider.update(corpusSlider.getGraphics());
		corpusSlider.updateUI();
		return 1;
	}else{
		Dictionary<Integer, Component> labels = corpusSlider.getLabelTable();
		corpusSlider.setEnabled(true);
		int newValue = corpusSlider.getMaximum() + 1;
		
		JLabel l = new JLabel( makeLabelString(label, newValue));
		labels.put(newValue, l);
		corpusSlider.setMaximum(newValue);
		corpusSlider.setLabelTable(labels);
		corpusSlider.validate();
		corpusSlider.update(corpusSlider.getGraphics());
		corpusSlider.updateUI();
		return newValue;
	}
	

}
	
	

	
}
