# To Do App

Requirements
------------------
Couchbase Server
Docker

How to run?
------------------

Should set your couchbase settings on application.yml

DOCKER : <br /><br />
Go to project folder in command line and run the following commands : <br /> <br />
docker build . (will return image name) <br />
docker run --name TODO -p 8080:8080 -p 8081:8081 --link db -d {IMAGENAME}


Sonar Report 
------------------

mvn clean jacoco:prepare-agent install sonar:sonar

Endpoints 
------------------

http://localhost:8080/register 

http://localhost:8080/login

http://localhost:8080/user

http://localhost:8081/env (to see environment props)

http://localhost:8081/metrics (to see metrics of project)

Swagger-ui
------------------
http://localhost:8080/swagger-ui.html (You should register to access swagger endpoint)

