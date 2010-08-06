package edu.gullick.CONE;

public class DeleteHistory {
	
	private String word = "";
	private int x = 0;
	private int y = 0;
	private boolean closed = false;
	
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public DeleteHistory(String word, int x, int y, boolean closed) {
		this.word = word;
		this.x = x;
		this.y = y;
		this.closed = closed;
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
