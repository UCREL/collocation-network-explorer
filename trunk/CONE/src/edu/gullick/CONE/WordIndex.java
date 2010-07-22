package edu.gullick.CONE;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class WordIndex{

	private HashMap<String,Vector<Integer>> index = new HashMap<String,Vector<Integer>>();
	private String theFileName = "";
	public Double maxAffinity = Double.MIN_VALUE;
	public Double minAffinity = Double.MAX_VALUE;

	
	//TODO: this is horrible - re-do it so that it uses an XML parser.
	public void analyzeXMLDOC(String fileName, GUI theGUI) throws Exception{

	    	File theFile = new File(fileName); 
	    	this.theFileName = fileName;
	    	createIndexFile();
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
	            	addToIndex( matcher.group(4), count);
	            	addToIndex( matcher.group(5), count);
	            	theGUI.setProgressBarLevel(count+1);
	            	theGUI.setProgressBarString("Indexing " + (count+1) + "/" + totalCount + " collocates");
	             	count++;
	             }
	            
	          
			}
	    	

	}

	
	//constructor.
	public WordIndex(){

		
	}

	// returns a list of the words direct neighbours.
	public Vector<LinkInformation> lookupWordNeighbours(String word) throws Exception{
		Vector<Integer> lineNumbers = index.get(word);
		Vector<LinkInformation> neighbours = new Vector<LinkInformation>();
		File theFile = new File(theFileName); 
    	BufferedReader input =  new BufferedReader(new FileReader(theFile));
		Matcher matcher;
	    Pattern collocationPattern = Pattern.compile("<collocates affinity=\"([-\\d]\\d*\\.\\d*)\" tscore=\"([-\\d]\\d*\\.\\d*)\" freq=\"(\\d*;\\d*;\\d*;\\d*)\">(.*) (.*)</collocates>");
		int count = 0;
		int currentPos = 0;
		String line = null;
		
		for(int x = 0; x < lineNumbers.size();x++){
			try {
				
	
			
				
				while (( line = input.readLine()) != null){            
		            matcher = collocationPattern.matcher( line );
		            if(matcher.find() ){
		            	if(count == lineNumbers.get(currentPos)){
		            		double affinity =  Double.parseDouble(matcher.group(1));
		            		
		            		neighbours.add(new LinkInformation(matcher.group(4),matcher.group(5),affinity,Double.parseDouble(matcher.group(2)), matcher.group(3)));
		            		if(affinity > maxAffinity){
		               			maxAffinity = affinity;
		               		}
		               		
		               		if(affinity < minAffinity){
		               			minAffinity = affinity;
		               		}
		               		
		            		
		            		
		            		currentPos++;
		            		count++;
		            	}else{
		            		count++;
		            	}
		            }
				}
				
				
			} catch (Exception e) {}
		}
		return neighbours;
	}
	
	
	//looks up a word in the corpus: checks for existence and returns information about it.
	public boolean lookUpWord(String word){
		if(index.containsKey(word)){
			return true;
		}else{
			return false;
		}
	 
	}
	


	public void createIndexFile(){
		index = new HashMap<String,Vector<Integer>>();
	}
	
	public  void addToIndex(String word, int lineNumber) throws Exception{
			if(index.containsKey(word)){
				Vector<Integer> temp = index.get(word);
				temp.add(new Integer(lineNumber));
				index.put(word,temp);
			
			}else{
				Vector<Integer> temp = new Vector<Integer>();
				temp.add(new Integer(lineNumber));
				index.put(word,temp);
			}
	}
	



	
		
}
