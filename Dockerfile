# ===========================
# Stage 1: Build the project
# ===========================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy toàn bộ project (code, gradlew, gradle folder, build.gradle, settings.gradle,...)
COPY . .

# Cấp quyền cho gradlew và build ứng dụng
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# ===========================
# Stage 2: Run the app
# ===========================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy file JAR từ stage build sang
COPY --from=build /app/build/libs/*.jar app.jar

# Mở port 8888
EXPOSE 8888

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
