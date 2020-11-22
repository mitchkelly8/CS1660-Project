# CS1660-Project

## Steps to run the Docker image

### Prerequisite Steps:
1. Install Docker

### Steps for Google Authentication 
2. Authenticate to a Google Cloud API (see [reference](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)).
3. Create a new directory on your local file system. 
4. Add the downloaded JSON file to your new directory. 
5. Rename the downloaded JSON file to credentials.json.
  - NOTE: This step is optional. However, you will need to update the JSON file name in step 10 to reflect your credentials file name. 

### Steps for GUI Setup (MacOS):
6. In terminal, call `brew cast install xquartz` to install XQuartz.
7. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
8. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution: 
9. In terminal, call `docker pull mitchkelly8/cs1660-project:firstpush` to download the image from Docker Hub. 
10. In terminal, call `docker run -e DISPLAY=$ip:0 -e GOOGLE_APPLICATION_CREDENTIALS=credentials.json project` to execute the docker image. 
