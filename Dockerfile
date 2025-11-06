FROM eclipse-temurin:21-jdk
RUN ./gradlew clean build -x test

COPY build/libs/exam-online-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app.jar"]
