FROM openjdk:19-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/AddressBook-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} addressbook-app.jar
ENTRYPOINT ["java","-jar","/addressbook-app.jar"]