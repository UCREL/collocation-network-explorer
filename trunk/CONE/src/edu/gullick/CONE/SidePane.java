package edu.gullick.CONE;

import javax.swing.*;
import java.awt.BorderLayout;


/**
 * The Class SidePane - a small JFrame based window for looking up information without using a visualisation.
 * Currently not particularly useful.
 */
public class SidePane {

	/*
	 * GLOBAL VARIABLES
	 */

	// the JFrame that everything is drawn on
	/** The frame. */
	public JFrame theFrame = new JFrame("CONE: Index");

	/* The JPanel that all of the components are contained within */
	/** The main panel. */
	public JPanel mainPanel = new JPanel(new BorderLayout());

	/* the Controller - very very loose MVC */
	/** The controller - very loose MVC. */
	public Controller theController = null;

	/* Word Index for looking up words */
	/** The word index. */
	public WordIndex theWordIndex = null;

	/** The input field. */
	public JTextField inputField = new JTextField(20);

	/** The output field. */
	public JTextArea outputField = new JTextArea(20, 20);

	/** The search button. */
	public JButton searchButton = new JButton("Go!");

	
	
	
	
	/**
	 * Instantiates a new side pane. Creates all of the components and adds them to the frame.
	 *
	 * @param theDriver the driver
	 */
	public SidePane(Controller theDriver) {

		/* Setting up global variables */
		this.theController = theDriver;

		/* Final frame configurations */
		theFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		theFrame.setContentPane(mainPanel);
		mainPanel.add(inputField, BorderLayout.NORTH);
		mainPanel.add(searchButton, BorderLayout.SOUTH);
		mainPanel.add(new JScrollPane(outputField));

		
		/* setting up the button listener */
		searchButton.addActionListener(theDriver);
		theFrame.addWindowListener(theDriver);
		theFrame.pack();
	}

	
	/* **************************************************
	 * GETTERS AND SETTERS PAST HERE
	 ***************************************************/
	
	/**
	 * Gets the input text.
	 *
	 * @return the input text
	 */
	public String getInputText() {
		return inputField.getText();
	}

	/**
	 * Sets the output text.
	 *
	 * @param s the new output text
	 */
	public void setOutputText(String s) {
		outputField.setText(s);
	}

}