# Sử dụng base image với JDK 21
FROM eclipse-temurin:21-jdk AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file Gradle và cấu hình vào container
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Cấp quyền thực thi cho Gradle Wrapper
RUN chmod +x gradlew

# Build project (biên dịch mà không chạy test để tiết kiệm thời gian)
COPY src src
RUN ./gradlew build -x test

# Tạo image runtime
FROM eclipse-temurin:21-jre

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR đã build từ stage trước
COPY --from=build /app/build/libs/*.jar app.jar

# Cấu hình cổng chạy của Spring Boot
EXPOSE 8082

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
