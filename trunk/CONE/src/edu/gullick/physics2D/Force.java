/*
 * May 29, 2005
 */

package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Interface Force.
 *
 * @author jeffrey traer bernstein
 */
public interface Force {
	
	/**
	 * Turn on.
	 */
	public void turnOn();

	/**
	 * Turn off.
	 */
	public void turnOff();

	/**
	 * Checks if is on.
	 *
	 * @return true, if is on
	 */
	public boolean isOn();

	/**
	 * Checks if is off.
	 *
	 * @return true, if is off
	 */
	public boolean isOff();

	/**
	 * Apply.
	 */
	public void apply();
}
