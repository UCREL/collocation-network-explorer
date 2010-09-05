package edu.gullick.CONE;

import java.util.Vector;
import edu.gullick.physics2D.Attraction;
import edu.gullick.physics2D.Particle;
import edu.gullick.physics2D.ParticleSystem;
import edu.gullick.physics2D.Spring;

/**
 * The Class Physics - extension of the ParticleSystem class, that allows pausing.
 * @extends edu.gullick.physics2D.ParticleSystem
 */
public class Physics extends ParticleSystem {
	
	/** Boolean representing the paused status of the engine. */
	private boolean paused = false;

	
	/**
	 * Instantiates a new physics.
	 */
	public Physics() {
		super(0F, 0.7F);
		setup();
	}

	/**
	 * Setup.
	 */
	public void setup() {
		//set the physics engine to use the faster integrator
		setIntegrator(ParticleSystem.RUNGE_KUTTA);
	}

	
	/**
	 * Step - updates all elements of the phyics by one step, unless it is paused..
	 */
	public void step() {

		if (!paused) {
			preStep();
			tick(1F);
			postStep();
		}

	}

	/**
	 * Post step.
	 */
	public void postStep() {

	}

	/**
	 * Pre step.
	 */
	public void preStep() {

	}

	/**
	 * Gets the particles.
	 *
	 * @return the particles
	 */
	public Vector<Particle> getParticles() {
		return particles;
	}

	/**
	 * Gets the springs.
	 *
	 * @return the springs
	 */
	public Vector<Spring> getSprings() {
		return springs;
	}

	/**
	 * Gets the attractions.
	 *
	 * @return the attractions
	 */
	public Vector<Attraction> getAttractions() {
		return attractions;
	}

	/**
	 * Adds the particle.
	 *
	 * @param p the p
	 */
	public void addParticle(WordNode p) {
		particles.add(p);
	}

	/**
	 * Adds the attraction.
	 *
	 * @param a the a
	 */
	public void addAttraction(WordAttraction a) {
		attractions.add(a);
	}

	/**
	 * Adds the spring.
	 *
	 * @param s the s
	 */
	public void addSpring(WordLink s) {
		springs.add(s);
	}

	/**
	 * Checks if is paused.
	 *
	 * @return true, if is paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Sets the paused.
	 *
	 * @param paused the new paused
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
