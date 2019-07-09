FROM java:8-jre

ADD ./target/offline-message-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx50m", "-jar", "/app/offline-message-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081