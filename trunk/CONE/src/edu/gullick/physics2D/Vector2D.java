/*
 * 
 */
package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class Vector2D.
 */
public class Vector2D {
	
	/** The x. */
	float x;
	
	/** The y. */
	float y;

	/**
	 * Instantiates a new vector2 d.
	 *
	 * @param X the x
	 * @param Y the y
	 */
	public Vector2D(float X, float Y) {
		x = X;
		y = Y;
	}

	/**
	 * Instantiates a new vector2 d.
	 */
	public Vector2D() {
		x = 0;
		y = 0;
	}

	/**
	 * Instantiates a new vector2 d.
	 *
	 * @param p the p
	 */
	public Vector2D(Vector2D p) {
		x = p.x;
		y = p.y;
		;
	}

	/**
	 * Y.
	 *
	 * @return the float
	 */
	public final float y() {
		return y;
	}

	/**
	 * X.
	 *
	 * @return the float
	 */
	public final float x() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param X the new x
	 */
	public final void setX(float X) {
		x = X;
	}

	/**
	 * Sets the y.
	 *
	 * @param Y the new y
	 */
	public final void setY(float Y) {
		y = Y;
	}

	/**
	 * Sets the.
	 *
	 * @param X the x
	 * @param Y the y
	 */
	public final void set(float X, float Y) {
		x = X;
		y = Y;
	}

	/**
	 * Sets the.
	 *
	 * @param p the p
	 */
	public final void set(Vector2D p) {
		x = p.x;
		y = p.y;
	}

	/**
	 * Adds the.
	 *
	 * @param p the p
	 */
	public final void add(Vector2D p) {
		x += p.x;
		y += p.y;
	}

	/**
	 * Subtract.
	 *
	 * @param p the p
	 */
	public final void subtract(Vector2D p) {
		x -= p.x;
		y -= p.y;
	}

	/**
	 * Adds the.
	 *
	 * @param a the a
	 * @param b the b
	 */
	public final void add(float a, float b) {
		x += a;
		y += b;
	}

	/**
	 * Subtract.
	 *
	 * @param a the a
	 * @param b the b
	 */
	public final void subtract(float a, float b) {
		x -= a;
		y -= b;
	}

	/**
	 * Multiply by.
	 *
	 * @param f the f
	 * @return the vector2 d
	 */
	public final Vector2D multiplyBy(float f) {
		x *= f;
		y *= f;
		return this;
	}

	/**
	 * Distance to.
	 *
	 * @param p the p
	 * @return the float
	 */
	public final float distanceTo(Vector2D p) {
		return (float) Math.sqrt(distanceSquaredTo(p));
	}

	/**
	 * Distance squared to.
	 *
	 * @param p the p
	 * @return the float
	 */
	public final float distanceSquaredTo(Vector2D p) {
		float dx = x - p.x;
		float dy = y - p.y;
		return dx * dx + dy * dy;
	}

	/**
	 * Distance to.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the float
	 */
	public final float distanceTo(float x, float y) {
		float dx = this.x - x;
		float dy = this.y - y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Dot.
	 *
	 * @param p the p
	 * @return the float
	 */
	public final float dot(Vector2D p) {
		return x * p.x + y * p.y;
	}

	/**
	 * Length.
	 *
	 * @return the float
	 */
	public final float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * Length squared.
	 *
	 * @return the float
	 */
	public final float lengthSquared() {
		return x * x + y * y;
	}

	/**
	 * Clear.
	 */
	public final void clear() {
		x = 0;
		y = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return new String("(" + x + ", " + y + ")");
	}

	// public final Vector2D cross( Vector2D p )
	// {
	// return new Vector2D( x = this.y*p.z - this.z*p.y,
	// y = this.x*p.z - this.z*p.x,
	// z = this.x*p.y - this.y*p.x );
	// }

	/**
	 * Checks if is zero.
	 *
	 * @return true, if is zero
	 */
	public boolean isZero() {
		return x == 0 && y == 0;
	}
}
