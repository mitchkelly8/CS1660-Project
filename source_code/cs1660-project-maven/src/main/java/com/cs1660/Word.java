package com.cs1660;

import java.util.ArrayList;

public class Word {
	
	// Variables 
	String word; 
	ArrayList<Document> documents; 
	ArrayList<Integer> frequencies; 
	int totalFrequency;
	
	/**
	 * Initializer. 
	 * @param inputWord - The word of the word. 
	 * @param inputDocuments - The documents that the word appears in. 
	 * @param inputFrequencies - The number of times the word appears in the document (frequency index matches document index).
	 */
	@SuppressWarnings("unchecked")
	public Word(String inputWord, ArrayList<Document> inputDocuments, ArrayList<Integer> inputFrequencies) {
		word = inputWord; 
		documents = (ArrayList<Document>) inputDocuments.clone(); 
		frequencies = (ArrayList<Integer>) inputFrequencies.clone();
		
		for (Integer frequency : frequencies) {
			totalFrequency = totalFrequency + frequency;
		}
	}
}
