package com.cs1660;

import java.util.ArrayList;

public class InvertedIndex {
	
	ArrayList<Word> words;
	
	/**
	 * Initializer. 
	 * @param inputString - The string inverted index. 
	 * @param inputDocuments - The documents that were passed in to the program. 
	 */
	public InvertedIndex(String inputString, ArrayList<Document> inputDocuments) {
		words = getWords(inputString, inputDocuments);
	}
	
	/**
	 * This function converts the Inverted Index to an ArrayList of Word. 
	 * @param inputString - The input inverted index to be converted. 
	 * @param inputDocuments - The documents passed into the program
	 * @return - An ArrayList of Words for each word in the inverted index. 
	 */
	public ArrayList<Word> getWords(String inputString, ArrayList<Document> inputDocuments) {
		String[] enterStrings = inputString.split("\n");	
		ArrayList<Word> outputWords = new ArrayList<Word>();

		for (String enterString : enterStrings) {
			String[] tabStrings = enterString.split("\t", -1);
			
			String word = ""; 
			ArrayList<Integer> frequencies = new ArrayList<Integer>();
			ArrayList<Document> documents = new ArrayList<Document>();
			
			int tabIteration = 1;
			for (String tabString : tabStrings) {
				
				// If it is a word
				if (tabIteration % 2 == 1) {
					word = tabString;
				}
				// If it is the list of files and frequencies 
				else {
					// Remove the first and last character { } 
					tabString = tabString.substring(1, tabString.length() - 1);
					String[] commaStrings = tabString.split(", "); 
					
					for(String commaString : commaStrings) {
						String[] equalStrings = commaString.split("=");
						int equalIteration = 1; 
						for (String equalString : equalStrings) {
							
							// If it is a document 
							if (equalIteration % 2 == 1) {
								// Find the document 
								for (Document document : inputDocuments) { 
									if (document.documentId == Integer.valueOf(equalString)) {
										documents.add(document);
									}
								}
							}
							// If it is a frequency
							else {
								frequencies.add(Integer.valueOf(equalString));
							}
							
							equalIteration++;
						}
						
					}
					
					outputWords.add(new Word(word, documents, frequencies));
					documents.clear();
					frequencies.clear();
					
				}
				
				tabIteration++;
				
			}
		}
		
		return outputWords;
	}
	
	

}
