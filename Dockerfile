FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/terning-0.0.1-SNAPSHOT.jar /app/terningserver.jar
CMD ["java", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "-jar", "terningserver.jar"]
