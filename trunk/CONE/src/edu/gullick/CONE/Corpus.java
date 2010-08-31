package edu.gullick.CONE;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



// this class represents all of the information found in one of the xml files for scotts tool.

public class Corpus {
	
	public String fileName = "";
	public double minTScore = Double.MAX_VALUE;
	public double maxTScore = Double.MIN_VALUE;
	public double minAffinity = Double.MAX_VALUE;
	public double maxAffinity = Double.MIN_VALUE;
	
	public enum LIMIT_TYPE {NUMBER, PERCENTAGE};
	
	
	
	public HashMap<String, Vector<LinkInformation>> info = new HashMap<String, Vector<LinkInformation>>();
	
	
	public Corpus(String fileName){
		this.fileName = fileName;
	}
	
	public boolean doesWordExist(String word){
		return info.containsKey(word);
	}
	
	public Vector<LinkInformation> getNeighbours(String word){
		return info.get(word);
	}
	

	
	public Vector<LinkInformation> getInverseNeighbours(String word, LIMIT_TYPE percOrNum, int topNumber){

		Vector<LinkInformation> tempInfo = info.get(word);
		Vector<LinkInformation> toReturn = new Vector<LinkInformation>();
		if(tempInfo == null){
			return toReturn;
		}
		
		
		int limit = 0;
		
		if(percOrNum == LIMIT_TYPE.NUMBER){
			/*given limit as number form*/
			limit = Math.min(tempInfo.size(), topNumber);
		}else{
			/*given limit as percentage form*/
			limit =  (tempInfo.size()  * topNumber) / 100;
		}	
		
		
		for(int a = limit; a < tempInfo.size(); a++){
			LinkInformation li = tempInfo.get(a);
			toReturn.add(li);
		}
		
		
		return toReturn;
	}
	
	public Vector<LinkInformation> getNeighbours(String word, LIMIT_TYPE percOrNum, int topNumber){
		
		
		Vector<LinkInformation> tempInfo = info.get(word);
		Vector<LinkInformation> toReturn = new Vector<LinkInformation>();
		if(tempInfo == null){
			return toReturn;
		}
		
		
		int limit = 0;
		
		if(percOrNum == LIMIT_TYPE.NUMBER){
			/*given limit as number form*/
			limit = Math.min(tempInfo.size(), topNumber);
		}else{
			/*given limit as percentage form*/
			limit =  (tempInfo.size()  * topNumber) / 100;
		}	
			
		for(int a = 0; a < limit; a++){
			LinkInformation li = tempInfo.get(a);
			toReturn.add(li);
		}
		return toReturn;
	}
	
	public HashMap<String, Vector<LinkInformation>> getInfo(){
		return info;
	}

	String getFileName() {
		return fileName;
	}
	
	public void addLink(String word1, String word2, String affinity, String Tscore, String frequency){
		LinkInformation temp = null;
		Double theAffinity = -1D;
		Double theTScore = -1D;
		Double frequency1 = -1D;
		Double frequency2 = -1D;
//		00004;2;0;363615
		
		try{
			Pattern statisticsPattern = Pattern.compile("(\\d+);(\\d+);(\\d+);(\\d+)");
			Matcher matcher = statisticsPattern.matcher(frequency);
			int word1Count = 0;
			int word2Count = 0;
			int combinedCount = 0;
			int totalCountMinusOthers = 0;
			
		    if(matcher.find()){
	         	word1Count = Integer.parseInt(matcher.group(1));
	         	word2Count =Integer.parseInt(matcher.group(2));
	         	combinedCount =Integer.parseInt(matcher.group(3));
	         	totalCountMinusOthers =Integer.parseInt(matcher.group(4));
	         	
	         	frequency1 =  ((double)word1Count + combinedCount) / ((double)word1Count + word2Count + combinedCount + totalCountMinusOthers);
	         	frequency2 = ((double)word1Count + combinedCount) / ((double)word1Count + word2Count + combinedCount + totalCountMinusOthers);
	       
	         	System.out.println("word1: " + word1 + " : " + frequency1 );
	         	System.out.println("word2: " + word2 + " : " + frequency2 );
		    }else{
	        	 System.out.println("There was an error calculating the frequency of the words.");
	         }
		}catch(Exception e){
			e.printStackTrace();
		}
         
		try{
			theAffinity = Double.parseDouble(affinity);
		}catch(Exception e){}
		
		try{
			theTScore = Double.parseDouble(Tscore);
		}catch(Exception e){}
		
		if(theAffinity > maxAffinity){
    		maxAffinity = theAffinity;
   		}
   		if(theAffinity < minAffinity){
   			minAffinity = theAffinity;
   		}
    	if(theTScore > maxTScore){
    		maxTScore = theTScore;
   		}
   		if(theTScore < minTScore){
   			minTScore = theTScore;
   		}
		
		temp = new LinkInformation (word1, word2, theAffinity, theTScore,  frequency1, frequency2);
		
	
		if(!info.containsKey(word1)){
			info.put(word1, new Vector<LinkInformation>());
		}
		
		if(!info.containsKey(word2)){
			info.put(word2, new Vector<LinkInformation>());
		}
		
		info.get(word1).add(temp);
		info.get(word2).add(temp);
	}
	
	
	
}
