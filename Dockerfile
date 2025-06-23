# Этап 1: сборка проекта
FROM maven:3.6.3-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn package -Dmaven.test.skip=true

# Этап 2: Liquibase миграции (с Maven, но не в финале)
FROM maven:3.6.3-openjdk-17 AS liquibase
WORKDIR /liquibase

# Копируем только нужное для Liquibase
COPY --from=builder /app/pom.xml ./pom.xml
COPY --from=builder /app/target/classes ./target/classes

# Выполняем Liquibase миграции
RUN mvn liquibase:update -Pdocker

# Этап 3: финальный минимальный образ
FROM openjdk:17-jdk-slim
WORKDIR /app

# Копируем только jar
COPY --from=builder /app/target/tracker-1.0.jar ./tracker-1.0.jar

# Запуск jar
CMD ["java", "-jar", "tracker-1.0.jar"]