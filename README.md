# CS1660-Project

## Grading Criteria

| Item | Done?  |
| :----- | :-: |
| First Java Application Implementation and Execution on Docker | Yes |
| Docker to Local (or GCP) Cluster Communication | Yes |
| Inverted Indexing MapReduce Implementation and Execution on the Cluster (GCP) | Yes |
| Term and Top-N Search | Yes |

## Steps to run the application (Docker Image):

The following are steps to run the application using the Docker image that is available in a public repository on my Docker Hub Account. 

### Prerequisite Steps:
1. Install Docker
2. Install Homebrew

### Steps for Google Authentication: 
3. Authenticate to a Google Cloud API (see [reference](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)).
4. Create a new directory on your local file system. 
5. Add the downloaded JSON file to your new directory. 
6. Rename the downloaded JSON file to `credentials.json`.
   - NOTE: This step is optional. However, you will need to update the JSON file name in step 10 to reflect your credentials file name. 
7. In terminal, navigate to your newly created directory. 

### Steps for GUI Setup (MacOS):
8. In terminal, call `brew cast install xquartz` to install XQuartz.
9. In terminal, call `open -a XQuartz`to open XQuartz.
10. Once XQuartz is open: 
    - Click **XQuartz** in the menu bar. 
    - Click **Preferences**. 
    - Click **Security**
    - Select **Allow connections from network clients**
11. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
12. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution: 
13. In terminal, call `docker pull mitchkelly8/cs1660-project:firstpush` to download the image from Docker Hub. 
14. In terminal, call `docker run -e DISPLAY=$ip:0 -e GOOGLE_APPLICATION_CREDENTIALS=credentials.json mitchkelly8/cs1660-project:firstpush` to execute the docker image. 

## Steps to run the application (JAR):

The following steps outline how to run the program locally (not through Docker) using the Runnable Jar. 

### Prerequisite Steps:
1. Install Homebrew

### Steps to Clone the Repository:
2. Create a new directory on your local file system. 
3. In terminal, navigate to your newly created directory. 


### Steps for Google Authentication: 
2. Authenticate to a Google Cloud API (see [reference](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)).

## Links:

- [DockerHub Repository](https://hub.docker.com/repository/docker/mitchkelly8/cs1660-project)
- [Video Walkthrough]()

## References:
- [Inverted Index](https://acadgild.com/blog/building-inverted-index-mapreduce)
- [Maven](https://youtu.be/sNEcpw8LPpo)
- [Google Authentication](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)
- [Google Cloud API Functions](https://cloud.google.com/storage/docs/how-to)
