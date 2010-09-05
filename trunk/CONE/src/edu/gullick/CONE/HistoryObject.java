package edu.gullick.CONE;
/**
 * The Class HistoryObject - a generic container for history objects - used for the undo feature.
 */
public class HistoryObject {

	/**
	 * The Enum ACTION - determines the type of action that needs to be undone.
	 */
	public static enum ACTION {
		
		/** The CENTE r_ physics. */
		CENTER_PHYSICS, 
		 /** The EXPAN d_ node. */
		 EXPAND_NODE, 
		 /** The CLOS e_ node. */
		 CLOSE_NODE, 
		 /** The AD d_ word. */
		 ADD_WORD, 
		 /** The DRA g_ canvas. */
		 DRAG_CANVAS, 
		 /** The DRA g_ node. */
		 DRAG_NODE, 
		 /** The DELET e_ node. */
		 DELETE_NODE, 
		 /** The ADJUS t_ tfilter. */
		 ADJUST_TFILTER, 
		 /** The TOGGL e_ displa y_ debug. */
		 TOGGLE_DISPLAY_DEBUG, 
		 /** The TOGGL e_ displa y_ words. */
		 TOGGLE_DISPLAY_WORDS, 
		 /** The TOGGL e_ displa y_ edges. */
		 TOGGLE_DISPLAY_EDGES, 
		 /** The TOGGL e_ displa y_ forces. */
		 TOGGLE_DISPLAY_FORCES, 
		 /** The TOGGL e_ displa y_ nodes. */
		 TOGGLE_DISPLAY_NODES, 
		 /** The TOGGL e_ smoot h_ font. */
		 TOGGLE_SMOOTH_FONT, 
		 /** The TOGGL e_ smoot h_ animation. */
		 TOGGLE_SMOOTH_ANIMATION, 
		 /** The PAUS e_ physics. */
		 PAUSE_PHYSICS, 
		 /** The SHO w_ index. */
		 SHOW_INDEX, 
		 /** The ZOO m_ in. */
		 ZOOM_IN, 
		 /** The ZOO m_ out. */
		 ZOOM_OUT
	};

	/** The details of the onject (can be an object, a description ,etc). */
	public Object details = null;
	
	/** The action - type of history event. */
	public HistoryObject.ACTION action;

	
	
	/**
	 * Instantiates a new history object.
	 *
	 * @param action the action
	 * @param details the details
	 */
	public HistoryObject(HistoryObject.ACTION action, Object details) {
		this.action = action;
		this.details = details;
	}

}
