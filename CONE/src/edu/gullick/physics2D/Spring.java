/*
 * May 29, 2005
 */

package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class Spring.
 *
 * @author jeffrey traer bernstein
 */
public class Spring implements Force {
	
	/** The spring constant. */
	float springConstant;
	
	/** The damping. */
	float damping;
	
	/** The rest length. */
	float restLength;
	
	/** The b. */
	Particle a, b;
	
	/** The on. */
	boolean on;

	/**
	 * Instantiates a new spring.
	 *
	 * @param A the a
	 * @param B the b
	 * @param ks the ks
	 * @param d the d
	 * @param r the r
	 */
	public Spring(Particle A, Particle B, float ks, float d, float r) {
		springConstant = ks;
		damping = d;
		restLength = r;
		a = A;
		b = B;
		on = true;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#turnOff()
	 */
	public final void turnOff() {
		on = false;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#turnOn()
	 */
	public final void turnOn() {
		on = true;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#isOn()
	 */
	public final boolean isOn() {
		return on;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#isOff()
	 */
	public final boolean isOff() {
		return !on;
	}

	/**
	 * Gets the one end.
	 *
	 * @return the one end
	 */
	public final Particle getOneEnd() {
		return a;
	}

	/**
	 * Gets the the other end.
	 *
	 * @return the the other end
	 */
	public final Particle getTheOtherEnd() {
		return b;
	}

	/**
	 * Current length.
	 *
	 * @return the float
	 */
	public final float currentLength() {
		return a.position().distanceTo(b.position());
	}

	/**
	 * Rest length.
	 *
	 * @return the float
	 */
	public final float restLength() {
		return restLength;
	}

	/**
	 * Strength.
	 *
	 * @return the float
	 */
	public final float strength() {
		return springConstant;
	}

	/**
	 * Sets the strength.
	 *
	 * @param ks the new strength
	 */
	public final void setStrength(float ks) {
		springConstant = ks;
	}

	/**
	 * Damping.
	 *
	 * @return the float
	 */
	public final float damping() {
		return damping;
	}

	/**
	 * Sets the damping.
	 *
	 * @param d the new damping
	 */
	public final void setDamping(float d) {
		damping = d;
	}

	/**
	 * Sets the rest length.
	 *
	 * @param l the new rest length
	 */
	public final void setRestLength(float l) {
		restLength = l;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#apply()
	 */
	public final void apply() {
		if (on && (a.isFree() || b.isFree())) {
			float a2bX = a.position().x - b.position().x;
			float a2bY = a.position().y - b.position().y;

			float a2bDistance = (float) Math.sqrt(a2bX * a2bX + a2bY * a2bY);

			if (a2bDistance == 0) {
				a2bX = 0;
				a2bY = 0;

			} else {
				a2bX /= a2bDistance;
				a2bY /= a2bDistance;

			}

			// spring force is proportional to how much it stretched

			float springForce = -(a2bDistance - restLength) * springConstant;

			// want velocity along line b/w a & b, damping force is proportional
			// to this

			float Va2bX = a.velocity().x - b.velocity().x;
			float Va2bY = a.velocity().y - b.velocity().y;

			float dampingForce = -damping * (a2bX * Va2bX + a2bY * Va2bY);

			// forceB is same as forceA in opposite direction

			float r = springForce + dampingForce;

			a2bX *= r;
			a2bY *= r;

			if (a.isFree())
				a.force().add(a2bX, a2bY);
			if (b.isFree())
				b.force().add(-a2bX, -a2bY);
		}
	}

	/**
	 * Sets the a.
	 *
	 * @param p the new a
	 */
	protected void setA(Particle p) {
		a = p;
	}

	/**
	 * Sets the b.
	 *
	 * @param p the new b
	 */
	protected void setB(Particle p) {
		b = p;
	}
}