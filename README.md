# CS1660-Project

## Links:

- [DockerHub Repository](https://hub.docker.com/repository/docker/mitchkelly8/cs1660-project)
- [Video Walkthrough]()

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
1. Install Git
2. Install Homebrew

### Steps to Clone the Repository:
3. Create a new directory on your local file system. 
4. In terminal, navigate to your newly created directory. 
5. In terminal, call `git clone git@github.com:mitchkelly8/CS1660-Project.git` to clone the repository to your local directory. 
6. In terminal, call `cd CS1660-Project` to navigate to the cloned project.
7. In terminal, call `cd runnable_jar_files` to navigate to the folder containing the JAR files. 

### Steps for Google Authentication: 
8. Authenticate to a Google Cloud API (see [reference](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)).
9. In terminal, call `export GOOGLE_APPLICATION_CREDENTIALS="[PATH]"` to set your Google Application Credentials. 
   - NOTE: You must replace PATH with the Path to the downloaded JSON file. 

### Steps for GUI Setup (MacOS):
10. In terminal, call `brew cast install xquartz` to install XQuartz.
11. In terminal, call `open -a XQuartz`to open XQuartz.
12. Once XQuartz is open: 
    - Click **XQuartz** in the menu bar. 
    - Click **Preferences**. 
    - Click **Security**
    - Select **Allow connections from network clients**
13. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
14. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution:
15. In terminal, call `java -jar project.jar -e DISPLAY=$ip:0` to launch the application.

## References:
- [Inverted Index](https://acadgild.com/blog/building-inverted-index-mapreduce)
- [Maven](https://youtu.be/sNEcpw8LPpo)
- [Google Authentication](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)
- [Google Cloud API Functions](https://cloud.google.com/storage/docs/how-to)
