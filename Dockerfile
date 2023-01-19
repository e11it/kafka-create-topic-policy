FROM maven:3.8-openjdk-18 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

FROM alpine:3.14
COPY --from=build /usr/src/app/target/ra-1.0.jar /usr/app/ra-1.0.jar 
