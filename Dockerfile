FROM eclipse-temurin:21-jre
WORKDIR /ecommerce
RUN groupadd dev && useradd -g dev matheus
COPY target/*.jar ecommerce.jar
USER matheus
EXPOSE 8081
CMD ["java", "-jar", "ecommerce.jar"]
