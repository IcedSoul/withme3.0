FROM java:8-jre

ADD ./target/websocket-server-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/websocket-server-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8282