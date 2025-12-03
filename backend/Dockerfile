# ====== 1단계: Build Stage ======
FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

# Gradle 캐시 최적화 위해 먼저 빌드 스크립트 복사
COPY build.gradle settings.gradle ./

# 의존성 미리 다운
RUN gradle dependencies --no-daemon || return 0

# 나머지 소스 복사
COPY . .

# JAR 빌드
RUN gradle build --no-daemon -x test


# ====== 2단계: Run Stage ======
FROM eclipse-temurin:21-jre

WORKDIR /app

# 1단계에서 빌드된 JAR 복사
COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Render는 PORT 환경변수로 포트 지정 → 반드시 8080으로 받도록 함
ENV PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]