/*
 * 
 */
package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class Particle.
 */
public class Particle {
	
	/** The position. */
	protected Vector2D position;
	
	/** The velocity. */
	protected Vector2D velocity;
	
	/** The force. */
	protected Vector2D force;
	
	/** The mass. */
	protected float mass;
	
	/** The age. */
	protected float age;
	
	/** The dead. */
	protected boolean dead;

	/** The fixed. */
	boolean fixed;

	/**
	 * Instantiates a new particle.
	 *
	 * @param m the m
	 */
	public Particle(float m) {
		position = new Vector2D();
		velocity = new Vector2D();
		force = new Vector2D();
		mass = m;
		fixed = false;
		age = 0;
		dead = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#distanceTo(traer.physics.Particle)
	 */
	/**
	 * Distance to.
	 *
	 * @param p the p
	 * @return the float
	 */
	public final float distanceTo(Particle p) {
		return this.position().distanceTo(p.position());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#makeFixed()
	 */
	/**
	 * Make fixed.
	 */
	public final void makeFixed() {
		fixed = true;
		velocity.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#isFixed()
	 */
	/**
	 * Checks if is fixed.
	 *
	 * @return true, if is fixed
	 */
	public final boolean isFixed() {
		return fixed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#isFree()
	 */
	/**
	 * Checks if is free.
	 *
	 * @return true, if is free
	 */
	public final boolean isFree() {
		return !fixed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#makeFree()
	 */
	/**
	 * Make free.
	 */
	public final void makeFree() {
		fixed = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#position()
	 */
	/**
	 * Position.
	 *
	 * @return the vector2 d
	 */
	public final Vector2D position() {
		return position;
	}

	/**
	 * Velocity.
	 *
	 * @return the vector2 d
	 */
	public final Vector2D velocity() {
		return velocity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#mass()
	 */
	/**
	 * Mass.
	 *
	 * @return the float
	 */
	public final float mass() {
		return mass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#setMass(float)
	 */
	/**
	 * Sets the mass.
	 *
	 * @param m the new mass
	 */
	public final void setMass(float m) {
		mass = m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#force()
	 */
	/**
	 * Force.
	 *
	 * @return the vector2 d
	 */
	public final Vector2D force() {
		return force;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see traer.physics.AbstractParticle#age()
	 */
	/**
	 * Age.
	 *
	 * @return the float
	 */
	public final float age() {
		return age;
	}

	/**
	 * Reset.
	 */
	protected void reset() {
		age = 0;
		dead = false;
		position.clear();
		velocity.clear();
		force.clear();
		mass = 1f;
	}

}
