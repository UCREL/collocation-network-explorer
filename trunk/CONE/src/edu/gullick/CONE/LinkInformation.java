package edu.gullick.CONE;

public class LinkInformation {
	public String word1 = "";
	public String word2 = "";
	public double affinity = 0.0D;
	public double tscore = 0.0D;
	public double frequency1 = 0.0D;
	public double frequency2 = 0.0D;
	
	public LinkInformation(String word1,String  word2, double affinity, double tscore, Double frequency1, Double frequency2){
		this. word1 = word1;
		this.word2 = word2;
		this.affinity = affinity;
		this.tscore = tscore;
		this.frequency1 = frequency1;
		this.frequency2 = frequency2;
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

	public double getFrequency1() {
		return frequency1;
	}

	public void setFrequency1(double frequency1) {
		this.frequency1 = frequency1;
	}

	public double getFrequency2() {
		return frequency2;
	}

	public void setFrequency2(double frequency2) {
		this.frequency2 = frequency2;
	}
}
