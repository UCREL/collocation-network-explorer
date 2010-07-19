package edu.gullick.CONE;


import java.util.Vector;

import edu.gullick.physics2D.Attraction;
import edu.gullick.physics2D.Particle;
import edu.gullick.physics2D.ParticleSystem;
import edu.gullick.physics2D.Spring;

public class Physics extends ParticleSystem {
	private boolean paused = false;
	
	public Physics(){
		super(0F,0.7F);
		setup();	
	}
		
	
	public void setup(){
		setIntegrator( ParticleSystem.MODIFIED_EULER );
		
	}
	
	
			
	public void step() {
		
		if(!paused){
			preStep();
			tick(1F);
			postStep();
		}
		
		
	}
	
	
	public void postStep() {
		
	}

	public void preStep() {
	
	}
	
	public Vector<Particle> getParticles(){
		return particles;
	}
	
	public Vector<Spring> getSprings(){
		return springs;
	}
	
	public Vector<Attraction> getAttractions(){
		return attractions;
	}
	
	public void addParticle(WordNode p){
		particles.add(p);
	}
	

	public void addAttraction(WordAttraction a) {
		attractions.add(a);
	}


	public void addSpring(WordLink s) {
		springs.add(s);
	}


	public boolean isPaused() {
		return paused;
	}


	public void setPaused(boolean paused) {
		this.paused = paused;
	}


	
}
