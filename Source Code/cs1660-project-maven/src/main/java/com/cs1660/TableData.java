package com.cs1660;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TableData {
	
	// Variables 
	ArrayList<String> stopWords = new ArrayList<String>();
	
	/**
	 * Initializer
	 */
	public TableData() {
		
	}
	
	/**
	 * This function gets the table data for the term search. 
	 * @param inputInvertedIndex - The inverted index to use for the term search.
	 * @param searchTerm - The term that is being searched. 
	 * @return - A 2D array that will used to populate a table in the GUI. 
	 */
	public String[][] getInvertedIndexTableData(InvertedIndex inputInvertedIndex, String searchTerm) {
		
		for (Word invertedIndexWord : inputInvertedIndex.words) {
			
			if (invertedIndexWord.word.equals(searchTerm)) {
				
				String[][] outputData = new String[invertedIndexWord.documents.size()][4];
				
				int rowIterator = 0; 
				for (Document document : invertedIndexWord.documents) {
					
					outputData[rowIterator][0] = Integer.toString(document.documentId); 
					outputData[rowIterator][1] = document.documentFolder;
					outputData[rowIterator][2] = document.documentName;
					outputData[rowIterator][3] = Integer.toString(invertedIndexWord.frequencies.get(rowIterator));
					
					rowIterator++;
					
				}
				return outputData;
			}
		}
		
		return null;
	}
	
	/**
	 * This function gets the table data for the Top N. 
	 * @param inputInvertedIndex - The inverted index to use for the Top N. 
	 * @param inputN - The number of results wanted. 
	 * @return - A 2D array that will be used to populate a table in the GUI. 
	 */
	public String[][] getTopNTableData(InvertedIndex inputInvertedIndex, int inputN) {
		
		TreeMap<Integer, String> tmap = new TreeMap<Integer, String>();
		createStopWordList();
		
		// Add all words and total frequencies to a tree map
		for (Word invertedIndexWord : inputInvertedIndex.words) {
			
			
			boolean stopWordFound = false;
			for (String stopWord : stopWords) {
				
				if (invertedIndexWord.word.equals(stopWord)) {
					stopWordFound = true;
					break;
				}
			}
			
			// If the word is not in the stop word list, add it to the map
			if (!stopWordFound) {
				tmap.put(invertedIndexWord.totalFrequency, invertedIndexWord.word);
				
				if (tmap.size() > inputN) {
					tmap.remove(tmap.firstKey());
				}
			}
		}
		
		String[][] output = new String[inputN][2];
		
		int rowIterator = inputN - 1;
		for (Map.Entry<Integer, String> entry : tmap.entrySet()) {
			int count = entry.getKey();
			String name = entry.getValue(); 
			
			output[rowIterator][0] = name; 
			output[rowIterator][1] = Integer.toString(count);
			
			rowIterator--;
		}
		
		return output;
	}
	
	/**
	 * This function creates a list of stop words that will be excluded from any Top N searches. 
	 * More stop words can easily be added by using the code stopWords.add("[input any word]")
	 */
	public void createStopWordList() {
		stopWords.add("one");
		stopWords.add("the");
		stopWords.add("and");
		stopWords.add("of");
		stopWords.add("to");
		stopWords.add("a");
		stopWords.add("i");
		stopWords.add("in");
		stopWords.add("that");
		stopWords.add("his");
		stopWords.add("he");
		stopWords.add("with");
		stopWords.add("my");
		stopWords.add("you");
		stopWords.add("with");
		stopWords.add("not");
		stopWords.add("is");
		stopWords.add("for");
		stopWords.add("it");
		stopWords.add("was");
		stopWords.add("but");
		stopWords.add("as");
		stopWords.add("be");
		stopWords.add("her");
		stopWords.add("this");
		stopWords.add("your");
		stopWords.add("she");
		stopWords.add("at");
		stopWords.add("had");
		stopWords.add("all");
		stopWords.add("on");
		stopWords.add("so");
		stopWords.add("what");
		stopWords.add("by");
		stopWords.add("me");
		stopWords.add("will");
		stopWords.add("thou");
		stopWords.add("him");
		stopWords.add("which");
		stopWords.add("from");
		stopWords.add("are");
		stopWords.add("if");
		stopWords.add("do");
		stopWords.add("thy");
		stopWords.add("no");
		stopWords.add("they");
		stopWords.add("we");
		stopWords.add("shall");
		stopWords.add("would");
		stopWords.add("our");
		stopWords.add("or");
		stopWords.add("when");
		stopWords.add("their");
		stopWords.add("an");
		stopWords.add("were");
		stopWords.add("more");
		stopWords.add("how");
		stopWords.add("there");
		stopWords.add("than");
		stopWords.add("did");
	}
	
	
	
	
	
}
