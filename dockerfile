FROM eclipse-temurin:21-jre
WORKDIR /app
COPY ./target/itmoprog2sem-1.0-Lab6.jar app.jar
ENV LABWORK_FILE=/data/data.xml
RUN mkdir -p /data
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar \"$LABWORK_FILE\""]