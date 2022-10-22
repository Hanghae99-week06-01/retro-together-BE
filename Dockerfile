FROM openjdk:17-alpine
COPY ./build/libs/retro_together-*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]