package edu.gullick.CONE;

public class LinkInformation {
	public String word1 = "";
	public String word2 = "";
	public double affinity = 0.0F;
	public double tscore = 0.0F;
	public String frequency = "";
	
	public LinkInformation(String word1,String  word2, double affinity, double tscore, String frequency){
		this. word1 = word1;
		this.word2 = word2;
		this.affinity = affinity;
		this.tscore = tscore;
		this.frequency = frequency;
	}

	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}

	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}

	public double getAffinity() {
		return affinity;
	}

	public void setAffinity(double affinity) {
		this.affinity = affinity;
	}

	public double getTscore() {
		return tscore;
	}

	public void setTscore(double tscore) {
		this.tscore = tscore;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	
}
