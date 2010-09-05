
package edu.gullick.CONE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Class WordIndex - abstracts reading through input files from the Corpus object.
 * Also contains global information about all of the corpuses - max affinity/min affinity
 */
public class WordIndex {

	/** The total min affinity. */
	Double totalMinAffinity = Double.MAX_VALUE;
	
	/** The total max affinity. */
	Double totalMaxAffinity = Double.MIN_VALUE;


	
	/**
	 * Instantiates a new word index 
	 */
	public WordIndex() {
		super();
	}
	
	
	/**
	 * Creates the corpus in ram.
	 *
	 * @param fileName the file name
	 * @return the corpus
	 */
	public Corpus createCorpusInRam(String fileName) {
		Corpus toReturn = new Corpus(fileName);

		return toReturn;
	}
	
	/**
	 * Analyzes an  xml document for information in the correct format.
	 * - currently horrible - change so that it uses  DOM model.
	 * It builds up a returned Corpus object, and updates the global information
	 *
	 * @param filename the filename to parse
	 * @param theGUI the the gui - so the progress bar can be updates
	 * @return the corpus that has been populated
	 * @throws Exception the exception
	 */
	public Corpus analyzeXMLDOC(String filename, GUI theGUI) throws Exception {
		Corpus toReturn = new Corpus(filename);
		File theFile = new File(filename);
		BufferedReader input = new BufferedReader(new FileReader(theFile));
		Matcher matcher;
		Pattern statisticsPattern = Pattern
				.compile("<statistics collocate_entries=\"(\\d+)\"/>");
		Pattern collocationPattern = Pattern
				.compile("<collocates affinity=\"([-\\d]\\d*\\.\\d*)\" tscore=\"([-\\d]\\d*\\.\\d*)\" freq=\"(\\d*;\\d*;\\d*;\\d*)\">(.*) (.*)</collocates>");
		int totalCount = 0;
		int count = 0;
		String line = null;

		while ((line = input.readLine()) != null) {
			matcher = statisticsPattern.matcher(line);
			if (matcher.find()) {
				totalCount = Integer.parseInt(matcher.group(1));
				break;
			}
		}

		theGUI.initProgressbar(0, totalCount);

		while ((line = input.readLine()) != null) {
			matcher = collocationPattern.matcher(line);
			while (matcher.find()) {

				toReturn.addLink(matcher.group(4), matcher.group(5),
						matcher.group(1), matcher.group(2), matcher.group(3));

				theGUI.setProgressBarLevel(count + 1);
				theGUI.setProgressBarString("Indexing " + (count + 1) + "/"
						+ totalCount + " collocates");
				count++;
			}

		}

		updateTotalVals(toReturn);
		return toReturn;

	}
	

	/**
	 * Update the total values by comparing the ones in a given corpus to the current ones.
	 *
	 * @param corpus the corpus
	 */
	public void updateTotalVals(Corpus corpus) {

		if (totalMinAffinity > corpus.minAffinity) {
			totalMinAffinity = corpus.minAffinity;
		}
		if (totalMaxAffinity < corpus.maxAffinity) {
			totalMaxAffinity = corpus.maxAffinity;
		}
	}
	

	/**
	 * Looks up word neighbours, and returns a vector containing LinkInformation for links to each
	 *
	 * @param word the word
	 * @param theCorpus the the corpus
	 * @return the vector
	 * @throws Exception the exception
	 */
	public Vector<LinkInformation> lookupWordNeighbours(String word, Corpus theCorpus) throws Exception {
		return theCorpus.getNeighbours(word);
	}

	
	/**
	 * Check whether word exists in a given corpus
	 *
	 * @param word the word
	 * @param corpus the corpus
	 * @return true, if successful
	 */
	public boolean lookUpWord(String word, Corpus corpus) {
		return corpus.doesWordExist(word);
	}

}
