package edu.gullick.CONE;

public class ParticleHistory {
	WordNode particle = null;
	int x = 0;
	int y = 0;
	public ParticleHistory(WordNode particle, int x, int y) {
		this.particle = particle;
		this.x = x;
		this.y = y;
	}
	public WordNode getParticle() {
		return particle;
	}
	public void setParticle(WordNode particle) {
		this.particle = particle;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
	
}
