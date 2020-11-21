package com.cs1660;

import java.io.File;

public class Document {
	
	// Variables 
	int documentId; 
	String documentName;
	String documentFolder;
	File file; 
	
	/**
	 * Constructor
	 * @param inputDocumentId - The identification number of the document 
	 * @param inputDocumentName - The name of the document
	 * @param inputDocumentFolder - The name of the folder that the document is in 
	 * @param inputFile - The file object 
	 */
	public Document(int inputDocumentId, String inputDocumentName, String inputDocumentFolder, File inputFile) {
		documentId = inputDocumentId; 
		documentName = inputDocumentName; 
		documentFolder = inputDocumentFolder;
		file = inputFile; 
	}
	
	/**
	 * Prints out the document 
	 */
	public String toString() {
		return documentId + " " + documentFolder + " " + documentName;
	}
}
