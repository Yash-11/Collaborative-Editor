FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/editor-0.0.1-SNAPSHOT.jar editor.jar
ENTRYPOINT ["java","-jar","/editor.jar"]