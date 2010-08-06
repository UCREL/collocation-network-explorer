package edu.gullick.CONE;

public class HistoryObject {

	public static enum ACTION{
		CENTER_PHYSICS,
		EXPAND_NODE, 
		CLOSE_NODE, 
		ADD_WORD, 
		DRAG_CANVAS, 
		DRAG_NODE, 
		DELETE_NODE, 
		ADJUST_TFILTER, 
		TOGGLE_DISPLAY_DEBUG, 
		TOGGLE_DISPLAY_WORDS, 
		TOGGLE_DISPLAY_EDGES, 
		TOGGLE_DISPLAY_FORCES, 
		TOGGLE_DISPLAY_NODES, 
		TOGGLE_SMOOTH_FONT, 
		TOGGLE_SMOOTH_ANIMATION, 
		PAUSE_PHYSICS, 
		SHOW_INDEX, 
		ZOOM_IN, 
		ZOOM_OUT
	};
	
	
	public Object details = null;
	public HistoryObject.ACTION action;
	
	public HistoryObject( HistoryObject.ACTION action, Object details){
		this.action = action;
		this.details = details;
	}
	
	
}
