# CS1660-Project

## Steps to run the Docker image

### Prerequisite Steps:
- Install Docker

### Steps for GUI Execution (MacOS):
1. In terminal, call `brew cast install xquartz` to install XQuartz.
2. In terminal, call `ip=$(ifconfig en0 | grep inet | awk '$1=="inet" {print $2}')` to set your ip environment variable.
3. In terminal, call `xhost + $ip` to allow connections from your local machine.

### Steps for Docker Image Execution: 
