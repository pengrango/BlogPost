You can make a jar and build a docker image with one command
./mvnw install dockerfile:build
 docker run -p 8080:8080 -t IMAGE_NAME