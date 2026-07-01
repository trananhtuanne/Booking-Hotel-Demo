FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml trước để cache dependency
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy source
COPY src ./src

# Build WAR
RUN mvn clean package -DskipTests
FROM tomcat:10.1-jdk17-temurin

# Xóa ứng dụng mặc định
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file WAR
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]