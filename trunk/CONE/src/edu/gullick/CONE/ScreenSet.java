package edu.gullick.CONE;

public class ScreenSet {
	public int XOffset = 0;
	public int YOffset = 0;
	public double zoomLevel = 0;
	
	public ScreenSet(int xOffset, int yOffset, double zoomLevel) {
		XOffset = xOffset;
		YOffset = yOffset;
		this.zoomLevel = zoomLevel;
	}

	public int getXOffset() {
		return XOffset;
	}

	public void setXOffset(int xOffset) {
		XOffset = xOffset;
	}

	public int getYOffset() {
		return YOffset;
	}

	public void setYOffset(int yOffset) {
		YOffset = yOffset;
	}

	public double getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	
	
}
