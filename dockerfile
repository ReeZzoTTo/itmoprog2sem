FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ./target/itmoprog2sem-1.0-Lab7.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]