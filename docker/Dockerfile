FROM eclipse-temurin:17
RUN mkdir /home/export-service
WORKDIR /home/export-service
COPY export-service/target/export-service.jar /home/export-service/export-service.jar
ENTRYPOINT ["java", "-jar", "export-service.jar"]
CMD ["server", "config.yml"]