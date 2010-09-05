
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


/**
 * The Class GUI - Handles all of the GUI that the user sees, but has all action/change listeners set as the Controller.
 * In an MVC model, this would by considered the view component.
 */
public class GUI {

	/*
	 * GLOBAL VARIABLES
	 */
	
	/** the JFrame that everything is drawn on */
	public JFrame theFrame = new JFrame("CONE: COllocation Network Explorer v1.0");

	/** The JPanel that all of the components are contained within */
	public JPanel mainPanel = new JPanel(new BorderLayout());
	
	/** The lower panel that contains sliders. */
	public JPanel lowerPanel = new JPanel(new GridLayout(3, 0));
	
	/** the Processing.core.PApplet that handles the graphics */
	public Screen theScreen = null;

	/** The controller. */
	public Controller theController = null;

	/** The word index. */
	public WordIndex theWordIndex = null;

	/** The logo. */
	public ImageIcon theLogo = null;

	/** The menu bar. */
	public JMenuBar menuBar = new JMenuBar();
	
	/** The file menu. */
	public JMenu fileMenu = new JMenu("File");
	
	/** The edit menu. */
	public JMenu editMenu = new JMenu("Edit");
	
	/** The tools menu. */
	public JMenu toolsMenu = new JMenu("Tools");
	
	/** The help menu. */
	public JMenu helpMenu = new JMenu("Help");
	
	/** The link menu. */
	public JMenu linkMenu = new JMenu("Look up a word in..");
	
	/** The import button. */
	public JMenuItem importButton = new JMenuItem("Import Corpus");
	
	/** The cdata button. */
	public JMenuItem cdataButton = new JMenuItem("Import .cdata file");
	
	/** The pref button. */
	public JMenuItem prefButton = new JMenuItem("Preferences");
	
	/** The reset button. */
	public JMenuItem resetButton = new JMenuItem("Reset");
	
	/** The center button. */
	public JMenuItem centerButton = new JMenuItem("Center");
	
	/** The add button. */
	public JMenuItem addButton = new JMenuItem("Add a Word");
	
	/** The t filter button. */
	public JMenuItem tFilterButton = new JMenuItem("Adjust T-Filter");
	
	/** The delete word button. */
	public JMenuItem deleteWordButton = new JMenuItem("Delete Word");
	
	/** The about button. */
	public JMenuItem aboutButton = new JMenuItem("About");
	
	/** The help button. */
	public JMenuItem helpButton = new JMenuItem("Help");
	
	/** The undo button. */
	public JMenuItem undoButton = new JMenuItem("Undo");

	/** The link google button. */
	public JMenuItem linkGoogleButton = new JMenuItem("Google.com");
	
	/** The link wikipedia button. */
	public JMenuItem linkWikipediaButton = new JMenuItem("Wikipedia.org");
	
	/** The link w matrix button. */
	public JMenuItem linkWMatrixButton = new JMenuItem("WMatrix");
	
	/** The link thesaurus button. */
	public JMenuItem linkThesaurusButton = new JMenuItem("Thesaurus.com");
	
	/** The link dictionary button. */
	public JMenuItem linkDictionaryButton = new JMenuItem("Dictionary.com");

	/** The display nodes. */
	public JRadioButton displayNodes = new JRadioButton("Display Nodes");
	
	/** The display edges. */
	public JRadioButton displayEdges = new JRadioButton("Display Edges");
	
	/** The display words. */
	public JRadioButton displayWords = new JRadioButton("Display Words");
	
	/** The display forces. */
	public JRadioButton displayForces = new JRadioButton("Display Forces");
	
	/** The display debug. */
	public JRadioButton displayDebug = new JRadioButton("Display Debug");
	
	/** The pause button. */
	public JMenuItem pauseButton = new JRadioButtonMenuItem("Pause Physics");
	
	/** The smooth font button. */
	public JRadioButton smoothFontButton = new JRadioButton("Smooth Font");
	
	/** The smooth animation button. */
	public JRadioButton smoothAnimationButton = new JRadioButton(
			"Smooth Animation");
	
	/** The show index. */
	public JRadioButton showIndex = new JRadioButton("Show Index");
	
	/** The zoom in button. */
	public JMenuItem zoomInButton = new JMenuItem("Zoom In");
	
	/** The zoom out button. */
	public JMenuItem zoomOutButton = new JMenuItem("Zoom Out");

	/** The tabbed pane. */
	public JTabbedPane tabbedPane = new JTabbedPane();

	/** The settings panel. */
	JPanel settingsPanel = new JPanel(new GridLayout(0, 2));

	/* Strings used in the help menu choices */
	/** The about text. */
	public String aboutText = "<html><b>CONE</b>: <b>CO</b>llocation <b>N</b>etwork <b>E</b>xplorer <i>v0.5</i>. <br/>Written by David Gullick at Lancaster University UK  (2010). <br/>Thanks to:<br/>Francois Taiani, <br/>Paul Rayson, <br/>John Mariani </html>";

	/** The help text. */
	public String helpText = "<html>CONE is a graphical approach to exploring large bodies of text using collocation...<html>";

	/** The filter slider. */
	public JSlider filterSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
	
	/** The corpus slider. */
	public JSlider corpusSlider = new JSlider(JSlider.HORIZONTAL, 0, 0, 0);

	/* Progress bar seen at the bottom of the page. */
	/** The prog bar. */
	public JProgressBar progBar = new JProgressBar();

	
	
	
	/*
	 * The constructor for the GUI class.
	 */

	/**
	 * Instantiates a new gUI.
	 *
	 * @param theDriver the the driver
	 * @param theScreen the the screen
	 * @param theWordIndex the the word index
	 * @param width the width
	 * @param height the height
	 */
	public GUI(Controller theDriver, Screen theScreen,
			WordIndex theWordIndex, int width, int height) {

		/* Setting up global variables */
		this.theScreen = theScreen;
		this.theController = theDriver;
		this.theWordIndex = theWordIndex;

		theLogo = createImageIcon("/data/logo.png", "the logo");

		/* Setting up the menu + shortcuts
		 * TODO: sort out the shortcuts for the other tab
		 */
		
		fileMenu.setMnemonic(KeyEvent.VK_F);
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		fileMenu.add(importButton);
		importButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.CTRL_MASK));

		menuBar.add(fileMenu);

		toolsMenu.add(resetButton);
		resetButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		toolsMenu.add(centerButton);
		centerButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		toolsMenu.add(addButton);
		addButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));
		toolsMenu.add(tFilterButton);
		tFilterButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				ActionEvent.CTRL_MASK));
		toolsMenu.add(deleteWordButton);
		deleteWordButton.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0));

		displayNodes.setSelected(false);
		displayNodes.addItemListener(theController);
		settingsPanel.add(displayNodes);
		// displayNodes.setAccelerator(KeyStroke.getKeyStroke('n'));

		displayEdges.setSelected(true);
		displayEdges.addItemListener(theController);
		settingsPanel.add(displayEdges);
		// displayEdges.setAccelerator(KeyStroke.getKeyStroke('e'));

		displayForces.setSelected(false);
		displayForces.addItemListener(theController);
		settingsPanel.add(displayForces);
		// displayDebug.setAccelerator(KeyStroke.getKeyStroke(''));

		displayWords.setSelected(true);
		displayWords.addItemListener(theController);
		settingsPanel.add(displayWords);
		// displayWords.setAccelerator(KeyStroke.getKeyStroke('w'));

		displayDebug.setSelected(false);
		displayDebug.addItemListener(theController);
		settingsPanel.add(displayDebug);
		// displayDebug.setAccelerator(KeyStroke.getKeyStroke('d'));

		smoothFontButton.setSelected(false);
		smoothFontButton.addItemListener(theController);
		settingsPanel.add(smoothFontButton);
		// smoothFontButton.setAccelerator(KeyStroke.getKeyStroke('f'));

		smoothAnimationButton.setSelected(false);
		smoothAnimationButton.addItemListener(theController);
		settingsPanel.add(smoothAnimationButton);
		// smoothAnimationButton.setAccelerator(KeyStroke.getKeyStroke('a'));

		pauseButton.setSelected(false);
		pauseButton.addItemListener(theController);
		toolsMenu.add(pauseButton);
		pauseButton.setAccelerator(KeyStroke.getKeyStroke('p'));

		showIndex.setSelected(false);
		showIndex.addItemListener(theController);
		settingsPanel.add(showIndex);
		// showIndex.setAccelerator(KeyStroke.getKeyStroke('p'));

		zoomInButton.addActionListener(theController);
		toolsMenu.add(zoomInButton);
		zoomInButton.setAccelerator(KeyStroke.getKeyStroke('+'));

		zoomOutButton.addActionListener(theController);
		toolsMenu.add(zoomOutButton);
		zoomOutButton.setAccelerator(KeyStroke.getKeyStroke('-'));

		menuBar.add(editMenu);
		undoButton.addActionListener(theController);
		editMenu.add(undoButton);
		undoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				ActionEvent.CTRL_MASK));

		menuBar.add(toolsMenu);
		helpMenu.add(helpButton);
		helpButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.ALT_MASK));
		helpMenu.add(aboutButton);
		aboutButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
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

		/* Adding action Listeners */
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

		/* Adding everything to the GUI */
		mainPanel.add(theScreen, BorderLayout.CENTER);
		theFrame.setJMenuBar(menuBar);

		/* Final frame configurations */
		theFrame.setDefaultCloseOperation(3);
		tabbedPane.addTab("Visualisation", null, mainPanel, "Visualisation");
		tabbedPane.addTab("Settings", null, settingsPanel, "Settings");
		theFrame.setContentPane(tabbedPane);
		theFrame.setSize(width, height);

		/* Setting up the progress bar */
		lowerPanel.add(filterSlider);
		lowerPanel.add(corpusSlider);
		lowerPanel.add(progBar);
		mainPanel.add(lowerPanel, BorderLayout.SOUTH);
		progBar.setStringPainted(true);
		setProgressBarString("No data loaded.");
		theFrame.setIconImage(theLogo.getImage());

		/* Starting the screen, and making the frame visable */
		theScreen.init();
		theFrame.setVisible(true);
	}

	
	
	
	
	
	/**
	 * Inits the progressbar to new values.
	 *
	 * @param lower the lower limit + new value
	 * @param higher the higher limit
	 */
	public void initProgressbar(int lower, int higher) {
		progBar.setMinimum(lower);
		progBar.setMaximum(higher);
		progBar.setValue(lower);
		progBar.setString("" + lower);
	}

	
	/**
	 * Sets the progress bar level.
	 *
	 * @param level the new progress bar level
	 */
	public void setProgressBarLevel(int level) {
		progBar.setValue(level);
	}
	


	/**
	 * Sets the progress bar string.
	 *
	 * @param s the new progress bar string
	 */
	public void setProgressBarString(String s) {
		progBar.setString(s);
	}

	
	/**
	 * Creates the image icon.
	 *
	 * @param path the path
	 * @param description the description
	 * @return the image icon
	 */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);

		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Update labels.
	 *
	 * @param wn the WordNode to generate labels from
	 */
	public void updateLabels(WordNode wn) {
		if (wn == null) {
			linkMenu.setEnabled(false);
			deleteWordButton.setEnabled(false);
			linkMenu.setText("Please select a word..");
		} else {
			linkMenu.setEnabled(true);
			deleteWordButton.setEnabled(true);
			linkMenu.setText("Look up '" + wn.getWord() + "' in..");
		}
	}

	/**
	 * Make label string.
	 *
	 * @param s the original String
	 * @param key the key (location along the JSlider)
	 * @return the label string 
	 */
	public String makeLabelString(String s, int key) {
		String toReturn = "";

		if (key % 2 == 0) {
			toReturn += "\n";
		}

		toReturn += s;

		return toReturn;
	}

	
	
	/**
	 * Adds the corpus to slider.
	 *
	 * @param label :the label for the slider
	 * @return the int :the position on the slider
	 */
	public int addCorpusToSlider(String label) {

		/* If the slider currently has no entries */
		if (corpusSlider.getMaximum() == 0) {
			Dictionary<Integer, Component> labels = new Hashtable<Integer, Component>();
			corpusSlider.setEnabled(true);
			corpusSlider.setMinimum(1);
			corpusSlider.setMaximum(1);
			corpusSlider.setValue(1);

			JLabel l = new JLabel(makeLabelString(label, 1));
			labels.put(1, l);

			corpusSlider.setLabelTable(labels);
			corpusSlider.setEnabled(false);
			corpusSlider.validate();
			corpusSlider.update(corpusSlider.getGraphics());
			corpusSlider.updateUI();
			return 1;
		} else {

			Dictionary<Integer, Component> labels = corpusSlider.getLabelTable();
			corpusSlider.setEnabled(true);
			int newValue = corpusSlider.getMaximum() + 1;

			JLabel l = new JLabel(makeLabelString(label, newValue));
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
