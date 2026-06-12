# Etapa 1: build con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime liviano
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Usamos *.jar para evitar fallos si cambia el nombre o la versión en el pom.xml
COPY --from=build /app/target/*.jar app_syo.jar
EXPOSE ${PORT}

# Pasamos el puerto dinámico directamente como argumento de Spring Boot al arrancar
CMD ["sh", "-c", "java -Xmx400m -jar app_syo.jar --server.port=${PORT}"]
ENTRYPOINT ["java", "-Xmx400m", "-jar", "app_syo.jar"]