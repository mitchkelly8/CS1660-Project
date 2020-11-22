package com.cs1660;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI {

		// UI Components 
		JFrame frame = new JFrame();
		JPanel panel = new JPanel(); 
		
		// Buttons
		JButton chooseFilesButton = new JButton("Choose Files");
		JButton setCloudVariablesButton = new JButton("Set Google Cloud Variables"); 
		JButton submitVariablesButton = new JButton("Submit Variables");
		JButton loadEngineButton = new JButton("Load Engine");
		JButton searchForTermButton = new JButton("Search for Term");
		JButton topNButton = new JButton("Top-N");
		JButton termSearchButton = new JButton("Search");
		JButton nSearchButton = new JButton("Search");
		JButton nGoBackToSearchButton = new JButton("Go Back To Search");
		JButton termGoBackToSearchButton = new JButton("Go Back To Search");
		
		// Labels
		JLabel loadMyEngineLabel = new JLabel("Load My Engine", SwingConstants.CENTER);
		JLabel fileLabel = new JLabel("", SwingConstants.CENTER); 
		JLabel engineWasLoadedLabel = new JLabel("<html><center><b>Engine was loaded<br>&<br>Inverted indices were constructed successfully!</b></center></html>", SwingConstants.CENTER);
		JLabel engineLoadTimeLabel = new JLabel("", SwingConstants.CENTER);
		JLabel pleaseSelectActionLabel = new JLabel("<html><center><b>Please Select Action</b></center></html>", SwingConstants.CENTER);
		JLabel enterYourSearchTermLabel = new JLabel("<html><center><b>Enter Your Search Term</b></center></html>", SwingConstants.CENTER);
		JLabel enterYourNValueLabel = new JLabel("<html><center><b>Enter Your N Value</b></center></html>", SwingConstants.CENTER);
		JLabel termSearchLabel = new JLabel("", SwingConstants.LEFT);
		JLabel topNFrequentTermsLabel = new JLabel("Top-N Frequent Terms", SwingConstants.LEFT);
		JLabel topNLoadTimeLabel = new JLabel("", SwingConstants.LEFT);
		
		// Text Fields 
		JTextField typeYourSearchTermTextField = new JTextField("Type Your Search Here ...");
		JTextField typeYourNTextField = new JTextField("Type Your N ...");
		JTextField typeYourProjectId = new JTextField("Type Your Project Id...");
		JTextField typeYourBucketName = new JTextField("Type Your Bucket Name...");
		JTextField typeYourClusterName = new JTextField("Type Your Cluster Name...");
		JTextField typeYourRegion = new JTextField("Type Your Region...");
		
		// Scroll Panes
		JScrollPane nScrollPane; 
		JScrollPane invertedIndexScrollPane;
		
		// Tables 
		JTable nTable; 
		JTable invertedIndexTable; 
		
		// Variables
		ArrayList<Document> documents = new ArrayList<>();
		ArrayList<File> displayFiles = new ArrayList<>();
		String currentDirectory = "";
		boolean nSearchButtonActionAdded = false;
		boolean nBackButtonActionAdded = false;
		boolean termSearchButtonActionAdded = false; 
		boolean termBackButtonActionAdded = false;
		boolean submitVariablesButtonActionAdded = false; 
		InvertedIndex index;
		
		// Cloud Variables 
		String projectId; 
		String bucketName; 
		String clusterName; 
		String region;

		// API
		GoogleCloudApi gcp;

		/**
		 *  Initializer.
		 */
		public GUI() {
			generateInitialGUI();
		}
		
		/**
		 * The first GUI that is loaded after the application is launched.
		 */
		public void generateInitialGUI() {
			panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
			panel.setLayout(new GridLayout(5, 5));
			
			// Add Label 
			Font mainLabelFont = loadMyEngineLabel.getFont();
			loadMyEngineLabel.setFont(mainLabelFont.deriveFont(mainLabelFont.getStyle() | Font.BOLD));
			panel.add(loadMyEngineLabel);
			
			// Choose Files
			panel.add(chooseFilesButton); 
	        chooseFilesButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                chooseFilesButtonPressed();
	            }
	        });
	        
	        // Set Cloud Variables 
			panel.add(setCloudVariablesButton);
			setCloudVariablesButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setCloudVariablesButtonPressed();
				}
			});
			
			// Set Frame Preferences
			frame.add(panel, BorderLayout.CENTER);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle("Mitch Kelly Search Engine");
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
			frame.setUndecorated(true);
			frame.setVisible(true);
		}
		
		/**
		 * The function that is called when the user presses the chooseFilesButton.
		 */
		public void chooseFilesButtonPressed() {
			// Open the file selector
			final JFileChooser fc = new JFileChooser(); 
			fc.setMultiSelectionEnabled(true);
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(frame);
			
			
			// If files were chosen
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				// Iterate the selected items
				for (File item : fc.getSelectedFiles()) {
					
					// Add the item to the display files	
					displayFiles.add(item);
					
					// If the item is a directory 
					if (item.isDirectory()) {
						currentDirectory = item.getName();
						getTxtFilesFromDirectory(item);
					}
					// If the item is a file, add it
					else {
						Document currentDocument = new Document(documents.size() + 1, item.getName(), currentDirectory, item);
						documents.add(currentDocument);
					}
				}
				
				// Remove UI Elements
				panel.remove(setCloudVariablesButton);
				
				// Add UI Elements
				// Adjust the label that shows the file names
				String totalFileString = "<html>"; 
				for (File file : displayFiles) {
					totalFileString = totalFileString + file.toString() + "<br>";
				}
				totalFileString = totalFileString + "</html>";
				fileLabel.setText(totalFileString); 
				panel.add(fileLabel);
				
				panel.add(loadEngineButton); 
		        loadEngineButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                try {
							loadEngineButtonPressed();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				
				refresh();
			}
		}
		
		/**
		 * A recursive function that will get all of the files from the input directory.
		 * @param inputDirectory - The directory to search
		 */
		public void getTxtFilesFromDirectory(File inputDirectory) {
			for (File item : inputDirectory.listFiles()) {
				
				// If it is another directory, recursive call.
				if (item.isDirectory()) {
					currentDirectory = item.getName();
					getTxtFilesFromDirectory(item);
				}
				// If the item is a file, add it
				else {
					Document currentDocument = new Document(documents.size() + 1, item.getName(), currentDirectory, item);
					documents.add(currentDocument);
				}
			}
		}
		
		/**
		 * The function that is called when the user presses the setCloudVariablesButton.
		 */
		public void setCloudVariablesButtonPressed() {
			// Remove UI Elements 
			panel.remove(loadMyEngineLabel);
			panel.remove(chooseFilesButton);
			panel.remove(setCloudVariablesButton);
			
			// Add UI Elements 
			panel.add(typeYourProjectId); 
			panel.add(typeYourBucketName); 
			panel.add(typeYourClusterName);
			panel.add(typeYourRegion);
			panel.add(submitVariablesButton);
			
			if (!submitVariablesButtonActionAdded) {
				
				submitVariablesButtonActionAdded = true;
				
				submitVariablesButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						submitVariablesButtonPressed();
					}
				});
			}
			
			refresh(); 

		}
		
		/**
		 * The function that is called when the user presses the submitVariablesButton.
		 */
		public void submitVariablesButtonPressed() {
			// Set cloud variables 
			projectId = typeYourProjectId.getText(); 
			bucketName = typeYourBucketName.getText();
			clusterName = typeYourClusterName.getText(); 
			region = typeYourRegion.getText();
			
			// Remove UI Elements
			panel.remove(typeYourProjectId);
			panel.remove(typeYourBucketName);
			panel.remove(typeYourClusterName);
			panel.remove(typeYourRegion);
			panel.remove(submitVariablesButton);
			
			// Add UI Elements 
			panel.add(loadMyEngineLabel);
			panel.add(chooseFilesButton);
			panel.add(setCloudVariablesButton);
			
			refresh();
		}
		
		/**
		 * The function that is called when the user presses the load engine button.
		 * @throws Exception
		 */
		public void loadEngineButtonPressed() throws Exception {
			// Load Engine Functionality
			
			long startTime = System.currentTimeMillis();
			
			// Upload the documents to the Google Cloud Bucket 
			GoogleCloudApi gui = new GoogleCloudApi();
			String outputString = gui.getInvertedIndex(projectId, bucketName, clusterName, documents, region);

			index = new InvertedIndex(outputString, documents);
			
			long endTime = System.currentTimeMillis();

			
			// Update UI 
			
			// Remove UI Elements 
			panel.remove(loadMyEngineLabel);
			panel.remove(chooseFilesButton);
			panel.remove(fileLabel);
			panel.remove(loadEngineButton);
			
			// Add UI Elements
			panel.add(engineWasLoadedLabel);
			engineLoadTimeLabel.setText("<html>Inverted Index Constructed in <b>" + (endTime - startTime) + "</b> ms</html>");
			panel.add(engineLoadTimeLabel);
			panel.add(pleaseSelectActionLabel);
			
			panel.add(searchForTermButton);
	        searchForTermButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                searchForTermButtonPressed();
	            }
	        });
	        
			panel.add(topNButton);
	        topNButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                try {
						topNButtonPressed();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	        });
			
			refresh();
		}
		
		/**
		 * The function that is called when the user presses the searchForTermButton.
		 */
		public void searchForTermButtonPressed() {
			// Update UI
			
			// Remove UI Elements 
			panel.remove(engineWasLoadedLabel);
			panel.remove(engineLoadTimeLabel);
			panel.remove(pleaseSelectActionLabel); 
			panel.remove(searchForTermButton);
			panel.remove(topNButton);
			
			// Add UI Elements 
			panel.add(enterYourSearchTermLabel);
			panel.add(typeYourSearchTermTextField);
			panel.add(termSearchButton);
			
			// If the action has not been added for the term search button, add it
			if (!termSearchButtonActionAdded) {
				
				// Change the boolean so that it will not be added twice 
				termSearchButtonActionAdded = true;
				
		        termSearchButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                try {
							termSearchButtonPressed();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
				
			}
			
			refresh();
		}
		
		/**
		 * The function that is called when the user pressed the topNButton.
		 * @throws IOException
		 */
		public void topNButtonPressed() throws IOException {

			
			// Remove UI Elements 
			panel.remove(engineWasLoadedLabel);
			panel.remove(engineLoadTimeLabel);
			panel.remove(pleaseSelectActionLabel); 
			panel.remove(searchForTermButton);
			panel.remove(topNButton);
			
			// Add UI Elements
			panel.add(enterYourNValueLabel);
			panel.add(typeYourNTextField);
			panel.add(nSearchButton);
			
			// If the action for the n search button has not been added, add it 
			if (!nSearchButtonActionAdded) {
				
				// Since the action has been added, set the boolean to true 
				nSearchButtonActionAdded = true;
				
		        nSearchButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                try {
							nSearchButtonPressed();
						} catch (IOException | InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        });
			}

			
			refresh();
		}
		
		/**
		 * The function that is called when the user presses the termSearchButton.
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public void termSearchButtonPressed() throws IOException, InterruptedException {
			// Perform Search
			long startTime = System.currentTimeMillis();
			
			String searchValue = typeYourSearchTermTextField.getText();

			TableData td = new TableData();
			
			String[][] data = td.getInvertedIndexTableData(index, searchValue);
			
			long endTime = System.currentTimeMillis();
			
			// Update UI 
			
			// Remove UI Elements 
			panel.remove(enterYourSearchTermLabel);
			panel.remove(typeYourSearchTermTextField);
			panel.remove(termSearchButton);
			
			// Add UI Elements
			
			// If the action has not been added for the back button, add it 
			if (!termBackButtonActionAdded) {
				
				termBackButtonActionAdded = true; 
				
		        termGoBackToSearchButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                termBackButtonPressed();
		            }
		        });
				
			}
			panel.add(termGoBackToSearchButton);

			
			termSearchLabel.setText("<html>You searched for the term: <b>" + searchValue + "</b><br>Your search was executed in <b>" + (endTime - startTime) + "</b> ms</html>");
			panel.add(termSearchLabel);
			
			// Create the table
			String[] columnNames = {"Doc ID", "Doc Folder", "Doc Name", "Frequencies"};
			
			invertedIndexTable = null;
			invertedIndexScrollPane = null;
			
			invertedIndexTable = new JTable(data, columnNames); 
			invertedIndexTable.setPreferredScrollableViewportSize(new Dimension(500, 50)); 
			invertedIndexTable.setFillsViewportHeight(true);
			invertedIndexTable.setGridColor(Color.black);
			
			invertedIndexScrollPane = new JScrollPane(invertedIndexTable);
			panel.add(invertedIndexScrollPane);
			
			refresh();
			
		}
		
		/**
		 * The function that is called when the user presses the nSearchButton.
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public void nSearchButtonPressed() throws IOException, InterruptedException {
			// Perform Search
			long startTime = System.currentTimeMillis();
			
			String nText = typeYourNTextField.getText();
			int n = Integer.parseInt(nText);
			
			TableData td = new TableData();

			String[][] data = td.getTopNTableData(index, n);
			
			long endTime = System.currentTimeMillis();
			
			/*
			LEGACY CODE FOR OLD TOP N
			// Set Google Cloud Variables 
		    // The ID of your GCP project
		    String projectId = "cs1660-intotocloud";

		    // The ID of your GCS bucket
		    String bucketName = "dataproc-staging-us-central1-822806606687-1glt9to4";
		    
		    String clusterName = "cluster-f843";
		    
		    String region = "us-central1";
		    
		    String nText = typeYourNTextField.getText();
		    int n = Integer.parseInt(nText);
			
			// Upload the documents to the Google Cloud Bucket 
			GoogleCloudApi gui = new GoogleCloudApi();
			String outputString = gui.getTopN(projectId, bucketName, clusterName, documents, region, n);

			String[] splitString = outputString.split("\n");
			
			String[][] data = new String[n][2];
			
			int rowNumber = n-1;
			for (String s : splitString) {
				String[] splitString2 = s.split("\t", -1);
				
				int colNumber = 1;
				for (String s2 : splitString2) {
					data[rowNumber][colNumber] = s2;
					colNumber--;
				}
				rowNumber--;
			}
			*/
			
			// Update UI 

			// Remove UI Elements 
			panel.remove(enterYourNValueLabel);
			panel.remove(typeYourNTextField);
			panel.remove(nSearchButton);
			
			// TODO: Add UI Elements 
			panel.add(nGoBackToSearchButton);
			
			if (!nBackButtonActionAdded) {
				
				nBackButtonActionAdded = true;
				
		        nGoBackToSearchButton.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		                nBackButtonPressed();
		            }
		        });
			}

			
			panel.add(topNFrequentTermsLabel);
			
			topNLoadTimeLabel.setText("<html>Top N Found in <b>" + (endTime - startTime) + "</b> ms</html>");
			panel.add(topNLoadTimeLabel);

			// Create the table
			String[] columnNames = {"Term", "Total Frequencies"};
			
			nTable = null;
			nScrollPane = null;
			
			nTable = new JTable(data, columnNames); 
			nTable.setPreferredScrollableViewportSize(new Dimension(500, 50)); 
			nTable.setFillsViewportHeight(true);
			nTable.setGridColor(Color.black);
			
			nScrollPane = new JScrollPane(nTable);
			panel.add(nScrollPane);
			
			


			
			refresh();
			
		}
		
		/**
		 * The function that is called when the user presses the nBackButton.
		 */
		public void nBackButtonPressed() {
			// Update UI
			
			
			// Remove UI Elements
			panel.remove(nGoBackToSearchButton);
			panel.remove(topNFrequentTermsLabel);
			panel.remove(topNLoadTimeLabel);
			nScrollPane.removeAll();
			panel.remove(nScrollPane);
			
			panel.removeAll();
			
			// Reset Iteration Variables
			//nIterationCounter = 1; 
			//nTableCounter = nTableCounter * 3;
			//System.out.println("\n\n");
			
			// Add UI Elements
			panel.add(engineWasLoadedLabel);
			panel.add(pleaseSelectActionLabel);
			
			panel.add(searchForTermButton);
	        panel.add(topNButton);
	        
	        refresh();
		}
		
		/**
		 * The function that is called when the user presses the termBackButton.
		 */
		public void termBackButtonPressed() {
			// Update UI
			
			// Remove UI Elements
			panel.remove(termGoBackToSearchButton);
			panel.remove(termSearchLabel);
			panel.remove(invertedIndexTable);
			panel.remove(invertedIndexScrollPane);
			
			//tableIsPresent = false;
			
			// Add UI Elements
			panel.add(engineWasLoadedLabel);
			panel.add(pleaseSelectActionLabel);
			
			panel.add(searchForTermButton);
			panel.add(topNButton);
	        
	        refresh();
		}
		
		/**
		 * This function refreshes the user interface
		 */
		public void refresh() {
			frame.invalidate();
			frame.validate();
			frame.repaint();
		}
}
