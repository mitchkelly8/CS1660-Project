# CS1660-Project

## Grading Criteria

| Item | Done?  |
| :----- | :-: |
| First Java Application Implementation and Execution on Docker | Yes |
| Docker to Local (or GCP) Cluster Communication | Yes |
| Inverted Indexing MapReduce Implementation and Execution on the Cluster (GCP) | Yes |
| Term and Top-N Search | Yes |

## Steps to run the Docker image:

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
9. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
10. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution: 
11. In terminal, call `docker pull mitchkelly8/cs1660-project:firstpush` to download the image from Docker Hub. 
12. In terminal, call `docker run -e DISPLAY=$ip:0 -e GOOGLE_APPLICATION_CREDENTIALS=credentials.json mitchkelly8/cs1660-project:firstpush` to execute the docker image. 

## Links:

- [DockerHub Repository](https://hub.docker.com/repository/docker/mitchkelly8/cs1660-project)
- [Video Walkthrough]()

## References:
- [Inverted Index](https://acadgild.com/blog/building-inverted-index-mapreduce)
- [Maven](https://youtu.be/sNEcpw8LPpo)
- [Google Cloud API Functions](https://cloud.google.com/storage/docs/how-to)
