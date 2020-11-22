# CS1660-Project

## Steps to run the Docker image

### Prerequisite Steps:
1. Install Docker

### Steps for Google Authentication 
1. Authenticate to a Google Cloud API (see [reference](https://cloud.google.com/docs/authentication/getting-started#auth-cloud-implicit-java)).  

### Steps for GUI Setup (MacOS):
1. In terminal, call `brew cast install xquartz` to install XQuartz.
2. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
3. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution: 
1. In terminal, call `docker pull mitchkelly8/cs1660-project:firstpush` to download the image from Docker Hub. 
2. In terminal, call `docker run -e DISPLAY=$ip:0 -e GOOGLE_APPLICATION_CREDENTIALS=credentials.json project` to execute the docker image. 
