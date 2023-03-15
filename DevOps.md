The following document contains the methodology we'll adopt to support the Backend teams, along with the experiments run and the POCs done.

Design:
As per the best practices, we'd like to accomplish this in two parts: CI and CD. 

CI part refers to the pipeline which will comprise the following steps:
    1. Compile, test and build the code in a docker container.
    2. Create a dockerized image for the artifact generated in the last step.
    3. Push it to a central repository, such as Docker Hub, Azure Container Registry, etc.

CD part refers to the pipeline which will comprise the following steps:
    1. Create a containerized instance of required DB and deploy it on cloud.
    2. Pull the docker image from the Docker registry (pushed in the CI pipeline) and deploy it on Azure Container Instances.

Since the teams are not yet ready with their implementations, in order to test our pipeline, we created two pipelines, one for Java + SpringBoot + Maven and the other for Java + SpringBoot + Gradle because those are the primary tech stacks of backend teams.

The link to repos are as follows (They are private for now):

Repo 1 - https://github.com/shaktimann/TestAddressBook

Repo 2 - https://github.com/shaktimann/TestAddressBook




## CI pipeline -  Implementation

Following are the steps to create a CI pipeline for a backend team.

1. Create a multi-stage Dockerfile to build in the 1st stage and dockerize and push docker image in the second stage.

A sample Dockerfile (works for the sample project) looks like follows:
```
FROM maven:3.8.7 as BUILD_IMAGE
ENV APP_HOME=/root/dev/myapp/
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME
COPY . .
RUN mvn clean -B package -e -X --file pom.xml

FROM openjdk:17-jdk-alpine
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/myapp/target/AddressBook-1.0-SNAPSHOT.jar .
CMD java -jar AddressBook-1.0-SNAPSHOT.jar
```

2. Create a Standard D2sv3 VM in Azure and set it up as a self hosted runner to execute builds.

    i. Create a VM with the help of: https://learn.microsoft.com/en-us/azure/virtual-machines/windows/quick-create-portal

    ii. Go to your git repo > Settings > Actions > Runners > New self-hosted runner

    iii. Choose appropriate settings. Login to your VM and follow the enlisted steps to setup your VM as a runner.

<img src="./Screenshot%202023-03-15%20142527.png"  width="800" height="800">

3. Go to your git repo > Settings > Secrets and variables > Actions. Add Secrets for DOCKER_USERNAME and DOCKER_PASSWORD.

4. Go to your git repo > Actions > New workflow.

5. Create a new workflow to build your SpringBoot project, build a docker image from the packaged jar and push the docker image to Docker Hub / Azure Container Reqgistry.

A sample main.yaml (works for the sample project) looks like follows (Note that the value for 'runs-on' needs to be configured as per the label of your runner, obtained after step 2.):

```
# This workflow will build a Java project with Maven, and cache/restore any dependencies to improve the workflow execution time
# For more information see: https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven

# This workflow uses actions that are not certified by GitHub.
# They are provided by a third-party and are governed by
# separate terms of service, privacy policy, and support
# documentation.

name: Java CI with Maven

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
        
  build_and_push:
    runs-on: self-hosted
    steps:
    - uses: actions/checkout@v3
    - name: Docker build
      run: |
        docker build . -t manunigotiya/address-book:spring-docker
    - name: Login to DockerHub
      uses: docker/login-action@v1
      with:
        username: ${{ secrets.DOCKER_USERNAME }}
        password: ${{ secrets.DOCKER_PASSWORD }}

    - name: Docker push
      run: |
        docker push manunigotiya/address-book:spring-docker
```

5. Commit the main.yaml file.

## CD pipeline -  Workflow

For continuous delivery, we have broken down the workflow into two components:
i. Deployment of the application.
ii. Deployment of a DB instance as per the team's requirements. 

Following are some references and links one might helpful to set up a CD pipeline.

Can use docker cli or vscode extension to put containers on azure
“Docker Azure Integration enables developers to use native Docker commands to run applications in Azure Container Instances (ACI) “
https://docs.docker.com/cloud/aci-integration/ 

Can use azure cli and azure container registry
    3 part tutorial on https://learn.microsoft.com/en-us/azure/container-instances/container-instances-tutorial-prepare-app

Can use github actions for CD to azure
Build an image from a Dockerfile → Push the image to an Azure container registry → Deploy the container image to an Azure container instance
Link 1: https://learn.microsoft.com/en-us/azure/container-instances/container-instances-github-action?tabs=userlevel 
Link 2: https://docs.github.com/en/actions/deployment/deploying-to-your-cloud-provider/deploying-to-azure/deploying-docker-to-azure-app-service 

How to run the DBs on Azure 
First try on local using docker compose (Link: https://blog.christian-schou.dk/run-postgresql-database-using-docker-compose/)
General types offered https://azure.microsoft.com/en-ca/products/category/databases/ 
PostgreSQL DB on Azure
Single server and Flexible server https://learn.microsoft.com/en-us/azure/postgresql/single-server/overview-postgres-choose-server-options?source=recommendations 

Tutorial: (uses the Azure portal) https://learn.microsoft.com/en-us/azure/postgresql/single-server/tutorial-design-database-using-azure-portal 
Not sure what to do about H2 database?

Important links:
https://medium.com/hardwareandro/install-docker-on-azure-virtual-machine-github-docker-hub-azure-deploy-pipeline-part-1-4b1e73dd0d7

https://www.digitalocean.com/community/questions/how-to-fix-docker-got-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket

https://dev.to/shayki5/github-action-for-building-maven-project-with-docker-and-push-it-docker-hub-3c3k


## Teams specifics and Future Work

Backend Team 3 - The team proposes to use RabbitMQ to build a notification service. As they progress and make a final decision on this, we'll begin to look into providing the with MQ services.


Backend Team 4 - The team proposes to use RabbitMQ to build a notification service. They have also mentioned using Redis as optional for caching. As they progress and make a final decision on this, we'll provide them with required services.

Backend Team 5 - The team is yet to choose between SQL or NoSQL DB. We'll offer them support accordingly.
