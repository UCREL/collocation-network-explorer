
package edu.gullick.CONE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.gullick.CONE.Corpus.LIMIT_TYPE;


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
	public LinkedHashMap<String, LinkInformation> lookupWordNeighbours(String word, Corpus theCorpus) throws Exception {
		return theCorpus.getNeighbours(word);
	}

	public LinkedHashMap<String, LinkInformation> lookupCombinedWordNeighbours(String word, Corpus corpusA, int percentageA, Corpus corpusB, int percentageB, LIMIT_TYPE percOrNum, int topNumber){
		LinkedHashMap<String, LinkInformation> toReturn = new LinkedHashMap<String, LinkInformation>();
		LinkedHashMap<String,LinkInformation> listFromA = new LinkedHashMap<String,LinkInformation>();
		
		LinkedHashMap<String,LinkInformation> listFromB =  new LinkedHashMap<String,LinkInformation>();
		
		if(corpusA != null){
			listFromA = corpusA.getNeighbours(word, percOrNum, topNumber);
		}
		if(corpusB != null){
			listFromB = corpusB.getNeighbours(word, percOrNum, topNumber);
		}
		
		
		
		Double percentageAratio = ((double) percentageA)/100D;
		Double percentageBratio = ((double) percentageB)/100D; 
		for(Map.Entry<String,LinkInformation> entry : listFromA.entrySet()){
			String key = entry.getKey();
			LinkInformation valueA = entry.getValue();
			if(!toReturn.containsKey(key)){
				LinkInformation valueB = listFromB.get(key);
				Double affinityA = valueA.getAffinity();
				Double affinityB = 0D;
				Double tscoreA = valueA.getTscore();
				Double tscoreB = 0D;
				Double frequency1A = valueA.getFrequencyOfWord(word);
				Double frequency2A = valueA.getFrequencyOfWord(key);
				Double frequency1B = 0D;
				Double frequency2B = 0D;
				Double frequencyBothA = valueA.getFrequencyBoth();
				Double frequencyBothB = 0D;
				if(valueB != null){
					affinityB = valueB.getAffinity();
					tscoreB = valueB.getTscore();
					frequency1B = valueB.getFrequencyOfWord(word);
					frequency2B = valueB.getFrequencyOfWord(key);
					frequencyBothB = valueB.getFrequencyBoth();
				}
				affinityA = (affinityA*percentageAratio);
				affinityB = (affinityB*percentageBratio);
				tscoreA = (tscoreA*percentageAratio);
				tscoreB =(tscoreB*percentageBratio);
				frequency1A = (frequency1A*percentageAratio);
				frequency2A = (frequency2A*percentageAratio);
				frequency1B =(frequency1B*percentageBratio);
				frequency2B =(frequency2B*percentageBratio);
				frequencyBothA = (frequencyBothA*percentageAratio);
				frequencyBothB =(frequencyBothB*percentageBratio);
				double averageAffinity = (affinityA+affinityB);
				double averageTScore = (tscoreA+tscoreB);
				double averageFrequency1 = (frequency1A+frequency1B);
				double averageFrequency2 = (frequency2A+frequency2B);
				double averageFrequencyBoth = (frequencyBothA+frequencyBothB);
				LinkInformation tempInfo = new LinkInformation(word, key, averageAffinity, averageTScore, averageFrequency1, averageFrequency2, averageFrequencyBoth); 
				toReturn.put(key, tempInfo);
			}
		}
		for(Map.Entry<String,LinkInformation> entry : listFromB.entrySet()){
			String key = entry.getKey();
			LinkInformation valueB = entry.getValue();
			if(!toReturn.containsKey(key)){
				LinkInformation valueA = listFromB.get(key);
				Double affinityA = valueB.getAffinity();
				Double affinityB = 0D;
				Double tscoreA = valueB.getTscore();
				Double tscoreB = 0D;
				Double frequency1A = valueB.getFrequencyOfWord(word);
				Double frequency2A = valueB.getFrequencyOfWord(key);
				Double frequency1B = 0D;
				Double frequency2B = 0D;
				Double frequencyBothA = valueB.getFrequencyBoth();
				Double frequencyBothB = 0D;
				if(valueA != null){
					affinityA = valueA.getAffinity();
					tscoreA = valueA.getTscore();
					frequency1A = valueA.getFrequencyOfWord(word);
					frequency2A = valueA.getFrequencyOfWord(key);
					frequencyBothA = valueA.getFrequencyBoth();
				}
				affinityA = (affinityA*percentageAratio);
				affinityB = (affinityB*percentageBratio);
				tscoreA = (tscoreA*percentageAratio);
				tscoreB =(tscoreB*percentageBratio);
				frequency1A = (frequency1A*percentageAratio);
				frequency2A = (frequency2A*percentageAratio);
				frequency1B =(frequency1B*percentageBratio);
				frequency2B =(frequency2B*percentageBratio);
				frequencyBothA = (frequencyBothA*percentageAratio);
				frequencyBothB =(frequencyBothB*percentageBratio);
				double averageAffinity = (affinityA+affinityB);
				double averageTScore = (tscoreA+tscoreB);
				double averageFrequency1 = (frequency1A+frequency1B);
				double averageFrequency2 = (frequency2A+frequency2B);
				double averageFrequencyBoth = (frequencyBothA+frequencyBothB);
				LinkInformation tempInfo = new LinkInformation(word, key, averageAffinity, averageTScore, averageFrequency1, averageFrequency2, averageFrequencyBoth); 
				toReturn.put(key, tempInfo);
			}	
		}
		return toReturn;
	}
	
	
	public double lookupCombinedFrequency(String word, Corpus corpusA, int percentageA, Corpus corpusB, int percentageB){
		double toReturn = -1;
		double freqA = 0D;
		double freqB = 0D;
		if(corpusA != null){
			freqA = corpusA.getWordFrequency(word);
		}
		if(corpusB != null){
			freqB = corpusB.getWordFrequency(word);
		}
		
		System.out.println("Freq A =" + freqA + " @ " + percentageA);
		
		System.out.println("Freq B =" + freqB + " @ " + percentageB);
		
		toReturn = (((freqA*percentageA)/100) + ((freqB*percentageB)/100));
		System.out.println("total Freq =" + toReturn);
		return toReturn;
		
	}
	
	
	public int lookUpWordGlobally(String word, LinkedHashMap<Integer, Corpus> list){
		for (Map.Entry<Integer,Corpus> entry : list.entrySet()) {
				if(entry.getValue().doesWordExist(word)){
					return entry.getKey();
				}
		   }
		return -1;
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