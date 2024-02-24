FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
COPY . .
RUN mvn clean package -DskipTests
FROM eclipse-temurin:21.0.2_13-jre-alpine 
COPY --from=build /target/track_student_app-0.0.1-SNAPSHOT.jar track_student_app.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","track_student_app.jar" ];