/*
 * 
 */
package edu.gullick.CONE;

import edu.gullick.physics2D.Attraction;
import edu.gullick.physics2D.Particle;

// TODO: Auto-generated Javadoc
/**
 * The Class WordAttraction simple extension of the Attraction class
 * @extends edu.gullick.physics2D.Attraction
 */
public class WordAttraction extends Attraction {

	/**
	 * Instantiates a new word attraction.
	 * @param particleA Particle A
	 * @param particleB Particle B
	 * @param strength float Strength
	 * @param minDistance float minimum Distance
	 */
	public WordAttraction(Particle particleA, Particle particleB, float strength, float minDistance) {
		super(particleA,particleB,strength,minDistance);
	}

}
