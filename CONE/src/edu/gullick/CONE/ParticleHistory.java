package edu.gullick.CONE;

public class ParticleHistory {
	String word = "";
	int x = 0;
	int y = 0;
	public ParticleHistory(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
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
