package com.cs1660;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.ImmutableList;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.gax.paging.Page;
import com.google.api.services.dataproc.Dataproc;
import com.google.api.services.dataproc.DataprocScopes;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

public class GoogleCloudApi {
	
	// Variables 
	String projectId; 
	String bucketName; 
	String clusterName;
	String region; 
	ArrayList<Document> documents;
	int n;

	/**
	 * Initializer. 
	 */
	public GoogleCloudApi() {
		
	}
	
	/**
	 * This function gets the inverted index for the input documents.
	 * @param inputProjectId - The project id in Google Cloud.
	 * @param inputBucketName - The bucket name in Google Cloud.
	 * @param inputClusterName - The cluster name in Google Cloud. 
	 * @param inputDocuments - The input Documents for the Inverted Index. 
	 * @param inputRegion - The region in Google Cloud. 
	 * @return - The Inverted Index as a string. 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getInvertedIndex(String inputProjectId, String inputBucketName, String inputClusterName, ArrayList<Document> inputDocuments, String inputRegion) throws IOException, InterruptedException {
		
		// Set the variables 
		projectId = inputProjectId; 
		bucketName = inputBucketName; 
		documents = inputDocuments; 
		clusterName = inputClusterName; 
		region = inputRegion; 
		
		// Upload the documents 
		for (Document document : documents) {
			uploadObject(projectId, bucketName, Integer.toString(document.documentId), document.file.getPath());
		}
		
		// Construct Inverted Index 
		constructInvertedIndex(projectId, region, clusterName, bucketName);
		
		// Download the documents
		while (true) {
			try {
				return downloadObject(projectId, bucketName, "OutputFiles/");
			}
			catch (Exception exception) {
				try {
					Thread.sleep(15000);
				}
				catch (InterruptedException interuptException) {
					System.out.println("Interrupted Exception");
				}
			}
		}
	}

	/**
	 * This function uploads an object to a Google Cloud Storage Bucket
	 * @param inputProjectId - The project id for the project that has the bucket. 
	 * @param inputBucketName - The name of the bucket
	 * @param inputObjectName - The name of the object being uploaded
	 * @param inputFilePath - The path to the object being uploaded 
	 * @throws IOException
	 * @reference https://cloud.google.com/storage/docs/uploading-objects#storage-upload-object-java
	 */
	public void uploadObject(String inputProjectId, String inputBucketName, String inputObjectName, String inputFilePath) throws IOException {
	    
		Storage storage = StorageOptions.newBuilder().setProjectId(inputProjectId).build().getService();
	    BlobId blobId = BlobId.of(inputBucketName, "InputFiles/" + inputObjectName);
	    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
	    storage.create(blobInfo, Files.readAllBytes(Paths.get(inputFilePath)));
	}
	
	/**
	 * This function creates and executes a job on Google Cloud to construct the inverted index using an InvertedIndex.jar file. 
	 * @param inputProjectId - The Google Cloud Project Id. 
	 * @param inputRegion - The region of the bucket/cluster on Google Cloud. 
	 * @param inputClusterName - The name of the cluster that will be used to execute the job on Google Cloud. 
	 * @param inputBucketName - The name of the bucket that contains the input/jar files on Google Cloud. 
	 * @throws IOException
	 * @throws InterruptedException
	 * @reference https://cloud.google.com/dataproc/docs/guides/submit-job
	 */
	public void constructInvertedIndex(String inputProjectId, String inputRegion, String inputClusterName, String inputBucketName) throws IOException, InterruptedException {
		
		Dataproc constructInvertedIndexDataProc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
                new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
                .setApplicationName("cs1660-project-maven/0.0.1").build();
		
		constructInvertedIndexDataProc.projects().regions().jobs().submit(inputProjectId, inputRegion, new SubmitJobRequest()
                .setJob(new Job().setPlacement(new JobPlacement().setClusterName(inputClusterName))
                .setHadoopJob(new HadoopJob().setMainClass("InvertedIndex")
                    .setJarFileUris(ImmutableList.of("gs://" + inputBucketName + "/InvertedIndex.jar"))
                    .setArgs(ImmutableList.of(
                        "gs://" + inputBucketName + "/InputFiles", "gs://" + inputBucketName + "/OutputFiles")))))
            .execute();
	}
	
	/**
	 * This function downloads an object from the Google Cloud Storage Bucket. 
	 * @param inputProjectId - The project id of the Google Cloud project that contains the object.
	 * @param inputBucketName - The name of the bucket on Google Cloud that contains the object. 
	 * @param inputFolderName - The name of the folder in the Google Cloud bucket that contains the object.
	 * @return - A String with the contents of the output files 
	 * @throws IOException
	 * @reference https://cloud.google.com/storage/docs/downloading-objects#storage-download-object-java
	 */
	public String downloadObject(String inputProjectId, String inputBucketName, String inputFolderName) throws IOException {
		
		ArrayList<byte[]> data = new ArrayList<byte[]>();
		int dataLength = 0;
		
		Storage storage = StorageOptions.newBuilder().setProjectId(inputProjectId).build().getService();
		Page<Blob> blobs = storage.list(inputBucketName, BlobListOption.prefix(inputFolderName));
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		
		// Iterate through the bytes
		Iterator<Blob> blobIterator = blobs.iterateAll().iterator();
		blobIterator.next(); 
		while(blobIterator.hasNext()) {
            Blob blob = blobIterator.next();
            
            // Used to wait until the output files have been uploaded entirely 
            if (blob.getName().contains("temporary"))
                throw new IOException();
            blob.downloadTo(byteOutputStream);
            data.add(byteOutputStream.toByteArray());
            dataLength += byteOutputStream.size();
            byteOutputStream.reset();
		}
		byteOutputStream.close();
		
        byte[] mergeArray = new byte[dataLength];
        
        int destLength = 0;
        for (byte[] i: data) {
            System.arraycopy(i, 0, mergeArray, destLength, i.length);
            destLength += i.length;
        }

        return new String(mergeArray);
	}	
	
	
	
	/***********************************************************
	LEGACY CODE 
	************************************************************/
	
	/**
	 * LEGACY CODE - This was using a TopN.jar file.
	 * @param inputProjectId - The project id in Google Cloud.
	 * @param inputBucketName - The bucket name in Google Cloud.
	 * @param inputClusterName - The cluster name in Google Cloud. 
	 * @param inputDocuments - The input Documents for the Inverted Index.
	 * @param inputRegion - The region in Google Cloud. 
	 * @param inputN - The number of results that should be returned from the Top N. 
	 * @return - The TopN results as a string. 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String getTopN(String inputProjectId, String inputBucketName, String inputClusterName, ArrayList<Document> inputDocuments, String inputRegion, int inputN) throws IOException, InterruptedException {
	
		// Set the variables 
		projectId = inputProjectId; 
		bucketName = inputBucketName; 
		documents = inputDocuments;
		clusterName = inputClusterName;
		region = inputRegion;
		n = inputN;
		
		// Upload the documents 
		for (Document document : documents) {
			uploadObject(projectId, bucketName, document.documentName, document.file.getPath());
		}
		
		// Find the Top N 
		findTopN(projectId, region, clusterName, bucketName, n); 
		
		// Download the documents 
		while (true) {
			try {
				return downloadObject(projectId, bucketName, "OutputFiles/");
			}
			catch (Exception exception) {
				try {
					Thread.sleep(15000);
				}
				catch (InterruptedException interuptException) {
					System.out.println("Interrupted Exception");
				}
			}
		}
	}
	
	/**
	 * LEGACY CODE - Submits a job to the Google Cloud cluster to run the TopN jar file. 
	 * @param inputProjectId - The Google Cloud Project Id. 
	 * @param inputRegion - The region of the bucket/cluster on Google Cloud. 
	 * @param inputClusterName - The name of the cluster that will be used to execute the job on Google Cloud. 
	 * @param inputBucketName - The name of the bucket that contains the input/jar files on Google Cloud. 
	 * @param inputN - The number of results that should be returned.
	 * @throws IOException
	 * @throws InterruptedException
	 * @reference https://cloud.google.com/dataproc/docs/guides/submit-job
	 */
	public void findTopN(String inputProjectId, String inputRegion, String inputClusterName, String inputBucketName, int inputN) throws IOException, InterruptedException {
		
		Dataproc topNDataProc = new Dataproc.Builder(new NetHttpTransport(), new JacksonFactory(), 
                new HttpCredentialsAdapter(GoogleCredentials.getApplicationDefault().createScoped(DataprocScopes.all())))
                .setApplicationName("cs1660-project-maven/0.0.1").build();
            
		topNDataProc.projects().regions().jobs().submit(inputProjectId, inputRegion, new SubmitJobRequest()
                .setJob(new Job().setPlacement(new JobPlacement().setClusterName(inputClusterName))
                .setHadoopJob(new HadoopJob().setMainClass("TopN")
                    .setJarFileUris(ImmutableList.of("gs://" + inputBucketName + "/TopN.jar"))
                    .setArgs(ImmutableList.of(
                        "-D n=" + inputN, "gs://" + inputBucketName + "/InputFiles", "gs://" + inputBucketName + "/OutputFiles")))))
            .execute();
	}
}

