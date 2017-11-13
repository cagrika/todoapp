FROM java:8
ADD /target/todoapp*.jar todoapp.jar
EXPOSE 8080
ENTRYPOINT exec java -jar todoapp.jar