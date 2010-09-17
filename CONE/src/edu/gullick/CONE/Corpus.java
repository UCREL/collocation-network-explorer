package edu.gullick.CONE;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Class Corpus - his class represents all of the information found in one of the xml files from scotts tool.
 */
public class Corpus {

	/** The file name of the imported file. */
	public String fileName = "";
	
	/** The min t score found in the file. */
	public double minTScore = Double.MAX_VALUE;
	
	/** The max t score found in the file. */
	public double maxTScore = Double.MIN_VALUE;
	
	/** The min affinity found in the file. */
	public double minAffinity = Double.MAX_VALUE;
	
	/** The max affinity found in the file. */
	public double maxAffinity = Double.MIN_VALUE;
	
	public double maxFrequency = Double.MIN_VALUE;
	
	public double minFrequency = Double.MAX_VALUE;	

	/**
	 * The Enum LIMIT_TYPE - whether the limit for lookups is a percentage of total, or an actual number to lookup
	 */
	public enum LIMIT_TYPE {
		
		/** The NUMBER. */
		NUMBER, 
		/** The PERCENTAGE. */
		PERCENTAGE
	};

	/** Global information for every word held within the corpus */
	public LinkedHashMap<String, LinkedHashMap<String, LinkInformation>> info = new LinkedHashMap<String, LinkedHashMap<String,LinkInformation>>();

	
	
	/**
	 * Instantiates a new corpus.
	 *
	 * @param fileName the file name
	 */
	public Corpus(String fileName) {
		this.fileName = fileName;
	}

	
	
	/**
	 * Checks whether the word exists
	 *
	 * @param word the word to lookup
	 * @return true, if successful
	 */
	public boolean doesWordExist(String word) {
		return info.containsKey(word);
	}

	/**
	 * Gets the neighbours of a word.
	 *
	 * @param word the word to lookup
	 * @return the neighbours of the word
	 */
	public LinkedHashMap<String, LinkInformation> getNeighbours(String word) {
		return info.get(word);
	}

	/**
	 * Gets the inverse neighbours, so if you normally looked up the top 10%, you would get the bottom 90%.
	 *
	 * @param word the word to lookup
	 * @param percOrNum the perc or num to
	 * @param topNumber the top number
	 * @return the inverse neighbours
	 */
	public LinkedHashMap<String, LinkInformation> getInverseNeighbours(String word,
			LIMIT_TYPE percOrNum, int topNumber) {

		LinkedHashMap<String, LinkInformation> tempInfo = info.get(word);
		LinkedHashMap<String, LinkInformation> toReturn = new LinkedHashMap<String, LinkInformation>();
		
		if (tempInfo == null) {
			return toReturn;
		}

		int limit = 0;

		if (percOrNum == LIMIT_TYPE.NUMBER) {
			/* given limit as number form */
			limit = Math.min(tempInfo.size(), topNumber);
		} else {
			/* given limit as percentage form */
			limit = (tempInfo.size() * topNumber) / 100;
		}

		
		Collection<LinkInformation> c = tempInfo.values();
		 
	    //obtain an Iterator for Collection
	    java.util.Iterator<LinkInformation> itr = c.iterator();
	 
	    //iterate through LinkedHashMap values iterator
	    while(itr.hasNext() && limit > 0){
	       	limit--;
	    }
	    
	    while(itr.hasNext()){
	    	LinkInformation temp = itr.next();
	    	
	    	if(temp.getWord1().equals(word)){
	    		toReturn.put(temp.getWord2(), temp);
	    	}else{
	    		toReturn.put(temp.getWord1(), temp);
	    	}
	 
	    }
	

		return toReturn;
	}

	
	/**
	 * Gets the neighbours.
	 *
	 * @param word the word
	 * @param percOrNum the perc or num
	 * @param topNumber the top number
	 * @return the neighbours
	 */LinkedHashMap<String,LinkInformation> getNeighbours(String word,
			LIMIT_TYPE percOrNum, int topNumber) {


			LinkedHashMap<String, LinkInformation> tempInfo = info.get(word);
			LinkedHashMap<String, LinkInformation> toReturn = new LinkedHashMap<String, LinkInformation>();
			
			if (tempInfo == null) {
				return toReturn;
			}

			int limit = 0;

			if (percOrNum == LIMIT_TYPE.NUMBER) {
				/* given limit as number form */
				limit = Math.min(tempInfo.size(), topNumber);
			} else {
				/* given limit as percentage form */
				limit = (tempInfo.size() * topNumber) / 100;
			}

			
			Collection<LinkInformation> c = tempInfo.values();
			 
		    //obtain an Iterator for Collection
		    java.util.Iterator<LinkInformation> itr = c.iterator();
		 
		    //iterate through LinkedHashMap values iterator
		    while(itr.hasNext() && limit > 0){
		    	LinkInformation temp = itr.next();
		    	
		    	if(temp.getWord1().equals(word)){
		    		toReturn.put(temp.getWord2(), temp);
		    	}else{
		    		toReturn.put(temp.getWord1(), temp);
		    	}
		 
		      	limit--;
		   }
		

			return toReturn;
	}

	
	
	/**
	 * Gets the whole info.
	 *
	 * @return the info
	 */
	public LinkedHashMap<String, LinkedHashMap<String, LinkInformation>> getInfo() {
		return info;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	String getFileName() {
		return fileName;
	}

	/**
	 * Adds the link to the corpus.
	 *
	 * @param word1 the word1
	 * @param word2 the word2
	 * @param affinity the affinity
	 * @param Tscore the tscore
	 * @param frequency the frequency
	 */
	public void addLink(String word1, String word2, String affinity,
		String Tscore, String frequency) {
		LinkInformation temp = null;
		Double theAffinity = -1D;
		Double theTScore = -1D;
		Double frequency1 = -1D;
		Double frequency2 = -1D;
		Double frequencyBoth = -1D;

		try {
			Pattern statisticsPattern = Pattern
					.compile("(\\d+);(\\d+);(\\d+);(\\d+)");
			Matcher matcher = statisticsPattern.matcher(frequency);
			int word1Count = 0;
			int word2Count = 0;
			int combinedCount = 0;
			int totalCountMinusOthers = 0;

			if (matcher.find()) {
				word1Count = Integer.parseInt(matcher.group(1));
				word2Count = Integer.parseInt(matcher.group(2));
				combinedCount = Integer.parseInt(matcher.group(3));
				totalCountMinusOthers = Integer.parseInt(matcher.group(4));

				frequency1 = ((double) word1Count + combinedCount)
						/ ((double) word1Count + word2Count + combinedCount + totalCountMinusOthers);
				frequency2 = ((double) word1Count + combinedCount)
						/ ((double) word1Count + word2Count + combinedCount + totalCountMinusOthers);
				
				if(frequency1 > maxFrequency){
					maxFrequency = frequency1;
				}
				
				if(frequency1 < minFrequency){
					minFrequency = frequency1;
				}
				
				if(frequency2 > maxFrequency){
					maxFrequency = frequency2;
				}
				
				if(frequency2 < minFrequency){
					minFrequency = frequency2;
				}
				
				
				frequencyBoth = ((double)  combinedCount)
				/ ((double) word1Count + word2Count + combinedCount + totalCountMinusOthers);


			} else {
				System.out
						.println("There was an error calculating the frequency of the words.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			theAffinity = Double.parseDouble(affinity);
		} catch (Exception e) {
		}

		try {
			theTScore = Double.parseDouble(Tscore);
		} catch (Exception e) {
		}

		if (theAffinity > maxAffinity) {
			maxAffinity = theAffinity;
		}
		if (theAffinity < minAffinity) {
			minAffinity = theAffinity;
		}
		if (theTScore > maxTScore) {
			maxTScore = theTScore;
		}
		if (theTScore < minTScore) {
			minTScore = theTScore;
		}

		temp = new LinkInformation(word1, word2, theAffinity, theTScore,
				frequency1, frequency2, frequencyBoth);

		if (!info.containsKey(word1)) {
			info.put(word1, new LinkedHashMap<String, LinkInformation>());
		}

		if (!info.containsKey(word2)) {
			info.put(word2, new LinkedHashMap<String, LinkInformation>());
		}

		info.get(word1).put(word2, temp);
		info.get(word2).put(word1, temp);
	}
	
	
	
	/**
	 * 
	 * @param word to lookup
	 * @return the frequency of that word in the corpus
	 */
	public Double getWordFrequency(String word){
		Double toReturn = -10D;
		if(!doesWordExist(word)){
			return 0D;
		}
		for(LinkInformation wordInfo : getNeighbours(word, Corpus.LIMIT_TYPE.NUMBER, 1).values()){		
				if (word.equals(wordInfo.getWord1())) {
					toReturn = wordInfo.frequency1;
				} else {
					toReturn = wordInfo.frequency1;
				}
			
		}
		return toReturn;
	}

}
