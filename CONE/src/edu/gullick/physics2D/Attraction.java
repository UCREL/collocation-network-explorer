// attract positive repel negative

package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class Attraction.
 */
public class Attraction implements Force {
	
	/** The a. */
	Particle a;
	
	/** The b. */
	Particle b;
	
	/** The k. */
	float k;
	
	/** The on. */
	boolean on;
	
	/** The distance min. */
	float distanceMin;
	
	/** The distance min squared. */
	float distanceMinSquared;

	/**
	 * Instantiates a new attraction.
	 *
	 * @param a the a
	 * @param b the b
	 * @param k the k
	 * @param distanceMin the distance min
	 */
	public Attraction(Particle a, Particle b, float k, float distanceMin) {
		this.a = a;
		this.b = b;
		this.k = k;
		on = true;
		this.distanceMin = distanceMin;
		this.distanceMinSquared = distanceMin * distanceMin;
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

	/**
	 * Gets the minimum distance.
	 *
	 * @return the minimum distance
	 */
	public final float getMinimumDistance() {
		return distanceMin;
	}

	/**
	 * Sets the minimum distance.
	 *
	 * @param d the new minimum distance
	 */
	public final void setMinimumDistance(float d) {
		distanceMin = d;
		distanceMinSquared = d * d;
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

	/**
	 * Sets the strength.
	 *
	 * @param k the new strength
	 */
	public final void setStrength(float k) {
		this.k = k;
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

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Force#apply()
	 */
	public void apply() {
		if (on && (a.isFree() || b.isFree())) {
			float a2bX = a.position().x() - b.position().x();
			float a2bY = a.position().y() - b.position().y();

			float a2bDistanceSquared = a2bX * a2bX + a2bY * a2bY;

			if (a2bDistanceSquared < distanceMinSquared)
				a2bDistanceSquared = distanceMinSquared;

			float force = k / a.mass / b.mass / a2bDistanceSquared;
			// TODO: above / changed from * -- look into this
			float length = (float) Math.sqrt(a2bDistanceSquared);

			// make unit vector

			a2bX /= length;
			a2bY /= length;

			// multiply by force

			a2bX *= force;
			a2bY *= force;

			// apply

			if (a.isFree())
				a.force().add(-a2bX, -a2bY);
			if (b.isFree())
				b.force().add(a2bX, a2bY);
		}
	}

	/**
	 * Gets the strength.
	 *
	 * @return the strength
	 */
	public final float getStrength() {
		return k;
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
}
