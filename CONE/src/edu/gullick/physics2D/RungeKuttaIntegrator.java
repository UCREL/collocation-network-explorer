/*
 * TODO: sort out hacky way of WordNode sorting!
 */
package edu.gullick.physics2D;

import java.util.*;

import edu.gullick.CONE.WordNode;

// TODO: Auto-generated Javadoc
/**
 * The Class RungeKuttaIntegrator.
 */
public class RungeKuttaIntegrator implements Integrator {
	
	/** The original positions. */
	ArrayList<Vector2D> originalPositions;
	
	/** The original velocities. */
	ArrayList<Vector2D> originalVelocities;
	
	/** The k1 forces. */
	ArrayList<Vector2D> k1Forces;
	
	/** The k1 velocities. */
	ArrayList<Vector2D> k1Velocities;
	
	/** The k2 forces. */
	ArrayList<Vector2D> k2Forces;
	
	/** The k2 velocities. */
	ArrayList<Vector2D> k2Velocities;
	
	/** The k3 forces. */
	ArrayList<Vector2D> k3Forces;
	
	/** The k3 velocities. */
	ArrayList<Vector2D> k3Velocities;
	
	/** The k4 forces. */
	ArrayList<Vector2D> k4Forces;
	
	/** The k4 velocities. */
	ArrayList<Vector2D> k4Velocities;

	/** The s. */
	ParticleSystem s;

	/**
	 * Instantiates a new runge kutta integrator.
	 *
	 * @param s the s
	 */
	public RungeKuttaIntegrator(ParticleSystem s) {
		this.s = s;

		originalPositions = new ArrayList<Vector2D>();
		originalVelocities = new ArrayList<Vector2D>();
		k1Forces = new ArrayList<Vector2D>();
		k1Velocities = new ArrayList<Vector2D>();
		k2Forces = new ArrayList<Vector2D>();
		k2Velocities = new ArrayList<Vector2D>();
		k3Forces = new ArrayList<Vector2D>();
		k3Velocities = new ArrayList<Vector2D>();
		k4Forces = new ArrayList<Vector2D>();
		k4Velocities = new ArrayList<Vector2D>();
	}

	/**
	 * Allocate particles.
	 */
	final void allocateParticles() {
		while (s.particles.size() > originalPositions.size()) {
			originalPositions.add(new Vector2D());
			originalVelocities.add(new Vector2D());
			k1Forces.add(new Vector2D());
			k1Velocities.add(new Vector2D());
			k2Forces.add(new Vector2D());
			k2Velocities.add(new Vector2D());
			k3Forces.add(new Vector2D());
			k3Velocities.add(new Vector2D());
			k4Forces.add(new Vector2D());
			k4Velocities.add(new Vector2D());
		}
	}

	/* (non-Javadoc)
	 * @see edu.gullick.physics2D.Integrator#step(float)
	 */
	public final void step(float deltaT) {
		allocateParticles();
		// ///////////////////////////////////////////////////////
		// save original position and velocities

		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.getParticle(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				(originalPositions.get(i)).set(p.position);
				(originalVelocities.get(i)).set(p.velocity);
			}

			p.force.clear(); // and clear the forces
		}

		// //////////////////////////////////////////////////////
		// get all the k1 values

		s.applyForces();

		// save the intermediate forces
		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				(k1Forces.get(i)).set(p.force);
				(k1Velocities.get(i)).set(p.velocity);
			}

			p.force.clear();
		}

		// //////////////////////////////////////////////////////////////
		// get k2 values

		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Vector2D originalPosition = originalPositions.get(i);
				Vector2D k1Velocity = k1Velocities.get(i);

				p.position.x = originalPosition.x + k1Velocity.x * 0.5f
						* deltaT;
				p.position.y = originalPosition.y + k1Velocity.y * 0.5f
						* deltaT;

				Vector2D originalVelocity = originalVelocities.get(i);
				Vector2D k1Force = k1Forces.get(i);

				p.velocity.x = originalVelocity.x + k1Force.x * 0.5f * deltaT
						/ p.mass;
				p.velocity.y = originalVelocity.y + k1Force.y * 0.5f * deltaT
						/ p.mass;

			}
		}

		s.applyForces();

		// save the intermediate forces
		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				(k2Forces.get(i)).set(p.force);
				(k2Velocities.get(i)).set(p.velocity);
			}

			p.force.clear(); // and clear the forces now that we are done with
								// them
		}

		// ///////////////////////////////////////////////////
		// get k3 values

		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Vector2D originalPosition = originalPositions.get(i);
				Vector2D k2Velocity = k2Velocities.get(i);

				p.position.x = originalPosition.x + k2Velocity.x * 0.5f
						* deltaT;
				p.position.y = originalPosition.y + k2Velocity.y * 0.5f
						* deltaT;

				Vector2D originalVelocity = originalVelocities.get(i);
				Vector2D k2Force = k2Forces.get(i);

				p.velocity.x = originalVelocity.x + k2Force.x * 0.5f * deltaT
						/ p.mass;
				p.velocity.y = originalVelocity.y + k2Force.y * 0.5f * deltaT
						/ p.mass;

			}
		}

		s.applyForces();

		// save the intermediate forces
		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				(k3Forces.get(i)).set(p.force);
				(k3Velocities.get(i)).set(p.velocity);
			}

			p.force.clear(); // and clear the forces now that we are done with
								// them
		}

		// ////////////////////////////////////////////////
		// get k4 values

		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
						continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				Vector2D originalPosition = originalPositions.get(i);
				Vector2D k3Velocity = k3Velocities.get(i);

				p.position.x = originalPosition.x + k3Velocity.x * deltaT;
				p.position.y = originalPosition.y + k3Velocity.y * deltaT;

				Vector2D originalVelocity = originalVelocities.get(i);
				Vector2D k3Force = k3Forces.get(i);

				p.velocity.x = originalVelocity.x + k3Force.x * deltaT / p.mass;
				p.velocity.y = originalVelocity.y + k3Force.y * deltaT / p.mass;

			}
		}

		s.applyForces();

		// save the intermediate forces
		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			if (p.isFree()) {
				(k4Forces.get(i)).set(p.force);
				(k4Velocities.get(i)).set(p.velocity);
			}
		}

		// ///////////////////////////////////////////////////////////
		// put them all together and what do you get?

		for (int i = 0; i < s.particles.size(); ++i) {
			Particle p = s.particles.get(i);
			p.age += deltaT;
			if (p.isFree()) {
				try{
					if(((WordNode)p).getLinks().size() == 0){
							continue;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				// update position

				Vector2D originalPosition = originalPositions.get(i);
				Vector2D k1Velocity = k1Velocities.get(i);
				Vector2D k2Velocity = k2Velocities.get(i);
				Vector2D k3Velocity = k3Velocities.get(i);
				Vector2D k4Velocity = k4Velocities.get(i);

				p.position.x = originalPosition.x
						+ deltaT
						/ 6.0f
						* (k1Velocity.x + 2.0f * k2Velocity.x + 2.0f
								* k3Velocity.x + k4Velocity.x);
				p.position.y = originalPosition.y
						+ deltaT
						/ 6.0f
						* (k1Velocity.y + 2.0f * k2Velocity.y + 2.0f
								* k3Velocity.y + k4Velocity.y);

				// update velocity

				Vector2D originalVelocity = originalVelocities.get(i);
				Vector2D k1Force = k1Forces.get(i);
				Vector2D k2Force = k2Forces.get(i);
				Vector2D k3Force = k3Forces.get(i);
				Vector2D k4Force = k4Forces.get(i);

				p.velocity.x = originalVelocity.x
						+ deltaT
						/ (6.0f * p.mass)
						* (k1Force.x + 2.0f * k2Force.x + 2.0f * k3Force.x + k4Force.x);
				p.velocity.y = originalVelocity.y
						+ deltaT
						/ (6.0f * p.mass)
						* (k1Force.y + 2.0f * k2Force.y + 2.0f * k3Force.y + k4Force.y);

			}
		}
	}
}
