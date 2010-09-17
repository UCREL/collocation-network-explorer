/*
 * 
 */
package edu.gullick.physics2D;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifiedEulerIntegrator.
 */
public class ModifiedEulerIntegrator implements Integrator {
	
	/** The s. */
	ParticleSystem s;

	/**
	 * Instantiates a new modified euler integrator.
	 *
	 * @param s the s
	 */
	public ModifiedEulerIntegrator(ParticleSystem s) {
		this.s = s;
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Integrator#step(float)
	 */
	public void step(float t) {
		s.clearForces();
		s.applyForces();

		float halftt = 0.5f * t * t;

		for (int i = 0; i < s.numberOfParticles(); i++) {
			Particle p = s.getParticle(i);
			if (p.isFree()) {
				float ax = p.force().x() / p.mass();
				float ay = p.force().y() / p.mass();

				p.position().add(p.velocity().x() / t, p.velocity().y() / t);
				p.position().add(ax * halftt, ay * halftt);
				p.velocity().add(ax / t, ay / t);
			}
		}
	}

}
