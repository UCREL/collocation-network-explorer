/*
 * May 29, 2005
 */
package edu.gullick.physics2D;

import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class ParticleSystem.
 */
public class ParticleSystem {
	
	/** The Constant RUNGE_KUTTA. */
	public static final int RUNGE_KUTTA = 0;
	
	/** The Constant MODIFIED_EULER. */
	public static final int MODIFIED_EULER = 1;
	
	/** The Constant EULER. */
	public static final int EULER = 2;

	/** The Constant DEFAULT_GRAVITY. */
	protected static final float DEFAULT_GRAVITY = 0;
	
	/** The Constant DEFAULT_DRAG. */
	protected static final float DEFAULT_DRAG = 0.001f;

	/** The particles. */
	public Vector<Particle> particles;
	
	/** The springs. */
	public Vector<Spring> springs;
	
	/** The attractions. */
	public Vector<Attraction> attractions;
	
	/** The custom forces. */
	public Vector<Force> customForces = new Vector<Force>();

	/** The integrator. */
	Integrator integrator;

	/** The gravity. */
	Vector2D gravity;
	
	/** The drag. */
	float drag;

	/** The has dead particles. */
	boolean hasDeadParticles = false;

	/**
	 * Sets the integrator.
	 *
	 * @param integrator the new integrator
	 */
	public final void setIntegrator(int integrator) {
		switch (integrator) {
		case RUNGE_KUTTA:
			this.integrator = new RungeKuttaIntegrator(this);
			break;
		case MODIFIED_EULER:
			this.integrator = new ModifiedEulerIntegrator(this);
			break;
		case EULER:
			this.integrator = new EulerIntegrator(this);
			break;
		}
	}

	/**
	 * Sets the gravity.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public final void setGravity(float x, float y, float z) {
		gravity.set(x, y);
	}

	// default down gravity
	/**
	 * Sets the gravity.
	 *
	 * @param g the new gravity
	 */
	public final void setGravity(float g) {
		gravity.set(0, g);
	}

	/**
	 * Sets the drag.
	 *
	 * @param d the new drag
	 */
	public final void setDrag(float d) {
		drag = d;
	}

	/**
	 * Tick.
	 */
	public final void tick() {
		tick(1);
	}

	/**
	 * Tick.
	 *
	 * @param t the t
	 */
	public final void tick(float t) {
		integrator.step(t);
	}

	/**
	 * Make particle.
	 *
	 * @param mass the mass
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the particle
	 */
	public final Particle makeParticle(float mass, float x, float y, float z) {
		Particle p = new Particle(mass);
		p.position().set(x, y);
		particles.add(p);
		return p;
	}

	/**
	 * Make particle.
	 *
	 * @return the particle
	 */
	public final Particle makeParticle() {
		return makeParticle(1.0f, 0f, 0f, 0f);
	}

	/**
	 * Make spring.
	 *
	 * @param a the a
	 * @param b the b
	 * @param ks the ks
	 * @param d the d
	 * @param r the r
	 * @return the spring
	 */
	public final Spring makeSpring(Particle a, Particle b, float ks, float d,
			float r) {
		Spring s = new Spring(a, b, ks, d, r);
		springs.add(s);
		return s;
	}

	/**
	 * Make attraction.
	 *
	 * @param a the a
	 * @param b the b
	 * @param k the k
	 * @param minDistance the min distance
	 * @return the attraction
	 */
	public final Attraction makeAttraction(Particle a, Particle b, float k,
			float minDistance) {
		Attraction m = new Attraction(a, b, k, minDistance);
		attractions.add(m);
		return m;
	}

	/**
	 * Clear.
	 */
	public final void clear() {
		particles.clear();
		springs.clear();
		attractions.clear();
	}

	/**
	 * Instantiates a new particle system.
	 *
	 * @param g the g
	 * @param somedrag the somedrag
	 */
	public ParticleSystem(float g, float somedrag) {
		integrator = new RungeKuttaIntegrator(this);
		particles = new Vector<Particle>();
		springs = new Vector<Spring>();
		attractions = new Vector<Attraction>();
		gravity = new Vector2D(0, g);
		drag = somedrag;
	}

	/**
	 * Instantiates a new particle system.
	 *
	 * @param gx the gx
	 * @param gy the gy
	 * @param gz the gz
	 * @param somedrag the somedrag
	 */
	public ParticleSystem(float gx, float gy, float gz, float somedrag) {
		integrator = new RungeKuttaIntegrator(this);
		particles = new Vector<Particle>();
		springs = new Vector<Spring>();
		attractions = new Vector<Attraction>();
		gravity = new Vector2D(gx, gy);
		drag = somedrag;
	}

	/**
	 * Instantiates a new particle system.
	 */
	public ParticleSystem() {
		integrator = new RungeKuttaIntegrator(this);
		particles = new Vector<Particle>();
		springs = new Vector<Spring>();
		attractions = new Vector<Attraction>();
		gravity = new Vector2D(0, ParticleSystem.DEFAULT_GRAVITY);
		drag = ParticleSystem.DEFAULT_DRAG;
	}

	/**
	 * Apply forces.
	 */
	protected final void applyForces() {
		if (!gravity.isZero()) {
			for (int i = 0; i < particles.size(); ++i) {
				Particle p = particles.get(i);
				p.force.add(gravity);
			}
		}

		for (int i = 0; i < particles.size(); ++i) {
			Particle p = (particles.get(i));
			p.force.add(p.velocity.x() * -drag, p.velocity.y() * -drag);
		}

		for (int i = 0; i < springs.size(); i++) {
			Spring f = springs.get(i);
			f.apply();
		}

		for (int i = 0; i < attractions.size(); i++) {
			Attraction f = attractions.get(i);
			f.apply();
		}

		for (int i = 0; i < customForces.size(); i++) {
			Force f = customForces.get(i);
			f.apply();
		}
	}

	/**
	 * Clear forces.
	 */
	protected final void clearForces() {
		Iterator<Particle> i = particles.iterator();
		while (i.hasNext()) {
			Particle p = (Particle) i.next();
			p.force.clear();
		}
	}

	/**
	 * Number of particles.
	 *
	 * @return the int
	 */
	public final int numberOfParticles() {
		return particles.size();
	}

	/**
	 * Number of springs.
	 *
	 * @return the int
	 */
	public final int numberOfSprings() {
		return springs.size();
	}

	/**
	 * Number of attractions.
	 *
	 * @return the int
	 */
	public final int numberOfAttractions() {
		return attractions.size();
	}

	/**
	 * Gets the particle.
	 *
	 * @param i the i
	 * @return the particle
	 */
	public final Particle getParticle(int i) {
		return particles.get(i);
	}

	/**
	 * Gets the spring.
	 *
	 * @param i the i
	 * @return the spring
	 */
	public final Spring getSpring(int i) {
		return springs.get(i);
	}

	/**
	 * Gets the attraction.
	 *
	 * @param i the i
	 * @return the attraction
	 */
	public final Attraction getAttraction(int i) {
		return attractions.get(i);
	}

	/**
	 * Adds the custom force.
	 *
	 * @param f the f
	 */
	public final void addCustomForce(Force f) {
		customForces.add(f);
	}

	/**
	 * Number of custom forces.
	 *
	 * @return the int
	 */
	public final int numberOfCustomForces() {
		return customForces.size();
	}

	/**
	 * Gets the custom force.
	 *
	 * @param i the i
	 * @return the custom force
	 */
	public final Force getCustomForce(int i) {
		return customForces.get(i);
	}

	/**
	 * Removes the custom force.
	 *
	 * @param i the i
	 * @return the force
	 */
	public final Force removeCustomForce(int i) {
		return customForces.remove(i);
	}

	/**
	 * Removes the particle.
	 *
	 * @param p the p
	 */
	public final void removeParticle(Particle p) {
		particles.remove(p);
	}

	/**
	 * Removes the spring.
	 *
	 * @param i the i
	 * @return the spring
	 */
	public final Spring removeSpring(int i) {
		return springs.remove(i);
	}

	/**
	 * Removes the attraction.
	 *
	 * @param i the i
	 * @return the attraction
	 */
	public final Attraction removeAttraction(int i) {
		return attractions.remove(i);
	}

	/**
	 * Removes the attraction.
	 *
	 * @param s the s
	 */
	public final void removeAttraction(Attraction s) {
		attractions.remove(s);
	}

	/**
	 * Removes the spring.
	 *
	 * @param a the a
	 */
	public final void removeSpring(Spring a) {
		springs.remove(a);
	}

	/**
	 * Removes the custom force.
	 *
	 * @param f the f
	 */
	public final void removeCustomForce(Force f) {
		customForces.remove(f);
	}

	/**
	 * Gets the spring.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the spring
	 */
	public final Spring getSpring(Particle a, Particle b) {
		for (int x = 0; x < springs.size(); x++) {
			Spring temp = springs.get(x);
			Particle oneEnd = temp.getOneEnd();
			Particle otherEnd = temp.getTheOtherEnd();
			if ((a == oneEnd && b == otherEnd) | (b == oneEnd && a == otherEnd)) {
				return temp;
			}
		}
		return null;

	}

}
