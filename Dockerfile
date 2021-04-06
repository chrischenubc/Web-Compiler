FROM openjdk:8
COPY ./target/compiler_service-0.0.1-SNAPSHOT.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar","compiler_service-0.0.1-SNAPSHOT.jar"]
