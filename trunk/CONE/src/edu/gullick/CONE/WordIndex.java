package edu.gullick.CONE;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class WordIndex{

	Double totalMinAffinity = Double.MAX_VALUE;
	Double totalMaxAffinity = Double.MIN_VALUE;
	Double totalMinTScore = Double.MAX_VALUE;
	Double totalMaxTScore = Double.MIN_VALUE;
	
	public Corpus createCorpusInRam(String fileName){
		Corpus toReturn = new Corpus(fileName);
		
		return toReturn;
	}

	//TODO: this is horrible - re-do it so that it uses an XML parser.
	public Corpus analyzeXMLDOC(String filename, GUI theGUI) throws Exception{
			Corpus toReturn = new Corpus(filename);
			
	    	File theFile = new File(filename); 
	    	BufferedReader input =  new BufferedReader(new FileReader(theFile));
			Matcher matcher;
			Pattern statisticsPattern = Pattern.compile("<statistics collocate_entries=\"(\\d+)\"/>");
		    Pattern collocationPattern = Pattern.compile("<collocates affinity=\"([-\\d]\\d*\\.\\d*)\" tscore=\"([-\\d]\\d*\\.\\d*)\" freq=\"(\\d*;\\d*;\\d*;\\d*)\">(.*) (.*)</collocates>");
			int totalCount = 0;
			int count = 0;
			String line = null;
		
			
			while (( line = input.readLine()) != null){            
	            matcher = statisticsPattern.matcher( line );
	            if(matcher.find()){
	            	totalCount =Integer.parseInt(matcher.group(1));
	            	break;
	            }
			} 
	       

			theGUI.initProgressbar(0, totalCount);
			
			while (( line = input.readLine()) != null){            
	            matcher = collocationPattern.matcher( line );
	            while ( matcher.find() ) {
	            	
	    
	            	toReturn.addLink(matcher.group(4), matcher.group(5), matcher.group(1), matcher.group(2), matcher.group(3));
             	
	            	theGUI.setProgressBarLevel(count+1);
	            	theGUI.setProgressBarString("Indexing " + (count+1) + "/" + totalCount + " collocates");
	             	count++;
	             }
	            
	          
			}
	    	
			
			updateTotalVals(toReturn);
			return toReturn;


	}
	
	public void updateTotalVals(Corpus corpus){
	
		if(totalMinAffinity > corpus.minAffinity){
			totalMinAffinity = corpus.minAffinity;
   		}
   		if(totalMaxAffinity < corpus.maxAffinity){
   			totalMaxAffinity = corpus.maxAffinity;
   		}
    	if(totalMinTScore > corpus.minTScore){
    		totalMinTScore = corpus.minTScore;
   		}
   		if(totalMaxTScore < corpus.maxTScore){
   			totalMaxTScore = corpus.maxTScore;
   		}
   		
	}

	
	//constructor.
	public WordIndex(){

		
	}

	// returns a list of the words direct neighbours.
	public Vector<LinkInformation> lookupWordNeighbours( String word, Corpus theCorpus) throws Exception{
		return theCorpus.getNeighbours(word);
	}
	
	
	public boolean lookUpWord(String word, Corpus corpus){
		return corpus.doesWordExist(word);
	}


	

	

		
}
