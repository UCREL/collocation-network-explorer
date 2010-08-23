package edu.gullick.CONE;

import java.util.HashMap;
import java.util.Vector;



// this class represents all of the information found in one of the xml files for scotts tool.

public class Corpus {
	
	public String fileName = "";
	public double minTScore = Double.MAX_VALUE;
	public double maxTScore = Double.MIN_VALUE;
	public double minAffinity = Double.MAX_VALUE;
	public double maxAffinity = Double.MIN_VALUE;
	
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
	
	public Vector<LinkInformation> getNeighbours(String word, Double TFilterMin, Double TFilterMax){
		Vector<LinkInformation> tempInfo = info.get(word);
		Vector<LinkInformation> toReturn = new Vector<LinkInformation>();
		for(int a = 0; a < tempInfo.size(); a++){
	
			LinkInformation li = tempInfo.get(a);
			if(li.getTscore() >  TFilterMin && li.getTscore() < TFilterMax){
				toReturn.add(li);
			}
	
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
		
		temp = new LinkInformation (word1, word2, theAffinity, theTScore,  frequency);
		
	
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
