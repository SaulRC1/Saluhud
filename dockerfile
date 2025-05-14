# Base image Alpine Linux + Eclipse Adoptium JDK 21
FROM eclipse-temurin:21-jdk-alpine

# establish work directory inside the container
WORKDIR /app

# Copy JAR file to app directory
COPY target/saluhud-1.0.jar saluhud.jar

# Copy nutrition translations to app directory
COPY saluhud_nutrition_translations/ ./saluhud_nutrition_translations/

# Expose Saluhud's port
EXPOSE 9000

# Define environment variable for nutrition translations
ENV SALUHUD_NUTRITION_TRANSLATIONS=/app/saluhud_nutrition_translations

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "saluhud.jar"]