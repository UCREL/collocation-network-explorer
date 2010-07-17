/*
 * May 29, 2005
 */

package edu.gullick.physics2D;

/**
 * @author jeffrey traer bernstein
 *
 */
public interface Force
{
	public void turnOn();
	public void turnOff();
	public boolean isOn();
	public boolean isOff();
	public void apply();
}
