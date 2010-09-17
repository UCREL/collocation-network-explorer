/*
 * 
 */
package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class EulerIntegrator.
 */
public class EulerIntegrator implements Integrator {
	
	/** The s. */
	ParticleSystem s;

	/**
	 * Instantiates a new euler integrator.
	 *
	 * @param s the s
	 */
	public EulerIntegrator(ParticleSystem s) {
		this.s = s;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Integrator#step(float)
	 */
	public void step(float t) {
		s.clearForces();
		s.applyForces();

		for (int i = 0; i < s.numberOfParticles(); i++) {
			Particle p = s.getParticle(i);
			if (p.isFree()) {
				p.velocity().add(p.force().x() / (p.mass() * t),
						p.force().y() / (p.mass() * t));
				p.position().add(p.velocity().x() / t, p.velocity().y() / t);
			}
		}
	}

}
