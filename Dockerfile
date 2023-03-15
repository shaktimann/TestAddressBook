FROM maven:3.8.7 as BUILD_IMAGE
ENV APP_HOME=/root/dev/myapp/
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME
COPY . .
RUN mvn clean -B package -e -X --file pom.xml

# FROM openjdk:17-jdk-alpine
# EXPOSE 8081
# CMD ls /root/dev/myapp/src/main/java
# # ARG JAR_FILE=/root/dev/myapp/target/AddressBook-1.0-SNAPSHOT.jar
# # ADD ${JAR_FILE} addressbook-app.jar
# # ENTRYPOINT ["java","-jar","addressbook-app.jar"]
#
FROM openjdk:17-jdk-alpine
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/myapp/target/AddressBook-1.0-SNAPSHOT.jar .
CMD java -jar AddressBook-1.0-SNAPSHOT.jar