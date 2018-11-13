[![Build Status](https://travis-ci.com/izaopan/vehiclemonitor.svg?branch=master)](https://travis-ci.com/izaopan/vehiclemonitor)

# Vehicle Status Monitors

Features
- Maintain Vehicles status
- Provide endpoints to update and view all vehicles 
- Dockerized with maven-plugin (Create jar and docker image with one command)

## How to run this example :

```sh
##only build java code
mvn clean install

##build a jar and a docker image
./mvnw install dockerfile:build

##To start the docker container
 docker run -p 8080:8080 -t [IMAGE_NAME]
```
## Once service is up locally, below endpoints are available
GET to http://localhost:8080/vehiclelist 
To view all vehicles's status

POST to http://localhost:8080/vehicle 
Tell the monitor a specific vehicle is connected at this moment
Example request:
{"vehicleId":"VLUR4X20009048066"}

## Extend to full solution 