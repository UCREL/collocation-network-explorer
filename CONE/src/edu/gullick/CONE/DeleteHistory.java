package edu.gullick.CONE;

/**
 * The Class DeleteHistory - specific history object that is used after a node has been deleted from the system.
 */
public class DeleteHistory {

	/** The word that was deleted. */
	private String word = "";
	
	/** The x position of the node. */
	private int x = 0;
	
	/** The y position of the node. */
	private int y = 0;
	
	/** Whether or not the node was closed before deleting */
	private boolean closed = false;


	/**
	 * Instantiates a new delete history Object.
	 *
	 * @param word the word
	 * @param x the x position
	 * @param y the y postion
	 * @param closed whether or not the node was closed prior to deleting
	 */
	public DeleteHistory(String word, int x, int y, boolean closed) {
		this.word = word;
		this.x = x;
		this.y = y;
		this.closed = closed;
	}

	
	/* ***********************************
	 * GETTERS & SETTERS AFTER THIS!
	 ************************************/
	
	
	/**
	 * Checks if is closed.
	 *
	 * @return true, if is closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * Sets the closed.
	 *
	 * @param closed the new closed
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	
	/**
	 * Gets the word.
	 *
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Sets the word.
	 *
	 * @param word the new word
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}

}
