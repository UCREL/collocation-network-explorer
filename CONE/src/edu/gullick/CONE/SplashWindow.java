package edu.gullick.CONE;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * The Class SplashWindow - the splashscreen that gets drawn as teh application loads up.
 * Currently it draws the image located at /data/Splash.jpg.
 */
public class SplashWindow extends JWindow {

  /**
   * Instantiates a new splash window.
   */
  public SplashWindow() {

  }


  /**
   * Show splash - stays up untill user clicks, or closeSplash() is called.
   */
  public void showSplash() {
    JPanel content = (JPanel)getContentPane();
    content.setBackground(Color.white);

    // Set the window's bounds, centering the window
    ImageIcon im = new ImageIcon(getClass().getResource("/data/Splash.jpg"));
 	int width = 450;
	int height =115;
    
	if(im == null){
    	im = new ImageIcon();
   
    }else{
    	 width = im.getIconWidth() + 10;
    	 height =im.getIconHeight() + 10;
    }
    
   
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width-width)/2;
    int y = (screen.height-height)/2;
    setBounds(x,y,width,height);
    setAlwaysOnTop(true);
    // Build the splash screen
  //  JLabel title = new JLabel
    //("CONE: Collocation Network Explorer.", JLabel.CENTER);
    JLabel label = new JLabel(im);
   // JLabel copyrt = new JLabel
     // ("<html>&nbsp;&nbsp;&nbsp;&nbsp;Copyright 2010, David Gullick & Lancaster University.<br/>Thanks to Francois taiani, Paul Rayson, John Mariani and Scott Piao</html>", JLabel.CENTER);
    //copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    content.add(new JLabel(im));
      //content.add(title, BorderLayout.NORTH);    
    //content.add(copyrt, BorderLayout.SOUTH);
    //content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

    
    
    addMouseListener(new MouseAdapter()
    {
        public void mousePressed(MouseEvent e)
        {
            setVisible(false);
           closeSplash();
        }
    });
    
    
    
    
    // Display it
    setVisible(true);
  
  }

  /**
   * Close splash.
   */
  public void closeSplash(){
	  this.setVisible(false);
	  this.dispose();
  }

 
}