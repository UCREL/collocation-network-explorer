package edu.gullick.CONE;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class SidePane{

	/*
	 * GLOBAL VARIABLES
	 */
	
	
	// the JFrame that everything is drawn on
	public JFrame theFrame = new JFrame("CONE: Index");
	
	/*The JPanel that all of the components are contained within*/
	public JPanel mainPanel = new JPanel(new BorderLayout());
	
	/*the Controller - very very loose MVC*/
	public Controller theController = null;
	
	
	/* Word Index for looking up words*/
	public WordIndex theWordIndex = null;
	
	public JTextField inputField = new JTextField(20);
	
	public JTextArea outputField = new JTextArea(20,20);
	
	public JButton searchButton = new JButton("Go!");

	

	
	
	
	
	public SidePane( Controller theDriver){
	
		/*Setting up global variables*/
		this.theController = theDriver;

		
		/*Final frame configurations*/
		theFrame.setDefaultCloseOperation(3);
		theFrame.setContentPane(mainPanel);
		mainPanel.add(inputField,BorderLayout.NORTH);
		mainPanel.add(searchButton,BorderLayout.SOUTH);
		mainPanel.add(new JScrollPane(outputField));
	
		/*Setting up the progress bar*/
		
		searchButton.addActionListener(theDriver);

		/*Starting the screen, and making the frame visible*/
		theFrame.setVisible(true);
		theFrame.pack();
	}
	
	
	public String getInputText(){
		return inputField.getText();
	}
	
	public void setOutputText(String s){
		outputField.setText(s);
	}
	
	
}