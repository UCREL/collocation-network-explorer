package edu.gullick.CONE;
/**
 * The Class ScreenSet. Is used to record all of the settings of the screen at some time - mostly used for the undo feature.
 */

public class ScreenSet {
	
	/** The X offset. */
	public int XOffset = 0;
	
	/** The Y offset. */
	public int YOffset = 0;
	
	/** The zoom level. */
	public double zoomLevel = 0;

	/**
	 * Instantiates a new screen set.
	 *
	 * @param xOffset the x offset
	 * @param yOffset the y offset
	 * @param zoomLevel the zoom level
	 */
	public ScreenSet(int xOffset, int yOffset, double zoomLevel) {
		XOffset = xOffset;
		YOffset = yOffset;
		this.zoomLevel = zoomLevel;
	}
	
	
	/* ***************************************
	 * GETTERS AND SETTERS PAST HERE 
	 ****************************************/

	/**
	 * Gets the x offset.
	 *
	 * @return the x offset
	 */
	public int getXOffset() {
		return XOffset;
	}

	/**
	 * Sets the x offset.
	 *
	 * @param xOffset the new x offset
	 */
	public void setXOffset(int xOffset) {
		XOffset = xOffset;
	}

	/**
	 * Gets the y offset.
	 *
	 * @return the y offset
	 */
	public int getYOffset() {
		return YOffset;
	}

	/**
	 * Sets the y offset.
	 *
	 * @param yOffset the new y offset
	 */
	public void setYOffset(int yOffset) {
		YOffset = yOffset;
	}

	/**
	 * Gets the zoom level.
	 *
	 * @return the zoom level
	 */
	public double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * Sets the zoom level.
	 *
	 * @param zoomLevel the new zoom level
	 */
	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

}
