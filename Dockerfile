
FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .

RUN mvn -B dependency:go-offline

COPY testng.xml .

COPY src ./src

CMD ["mvn", "-B", "clean", "test"]
