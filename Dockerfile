#### IMPROVED IMAGE#2 - < 170 MB
## --- Stage 1: Build & Custom JRE Creation ---
# --- Stage 1: Build with the Alpine-based JDK ---
FROM amazoncorretto:25-alpine AS builder
WORKDIR /app

# Alpine uses 'apk' instead of 'yum'
RUN apk add --no-cache findutils binutils

COPY . .
RUN ./gradlew :app:bootJar --no-daemon
RUN cp app/build/libs/exchangerates*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Create the custom JRE (compatible with Alpine/musl)
RUN $JAVA_HOME/bin/jlink \
    --add-modules java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,jdk.unsupported \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress 2 \
    --output /custom-jre

# --- Stage 2: Tiny Runtime Stage ---
FROM alpine:latest AS runtime
WORKDIR /app

# We still need these for the JVM to talk to the OS
RUN apk add --no-cache gcompat libstdc++

COPY --from=builder /custom-jre /opt/java/runtime
ENV PATH="/opt/java/runtime/bin:$PATH"

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]



#FROM amazoncorretto:25 AS builder
#WORKDIR /app
#RUN yum install -y findutils binutils
#
#COPY . .
#RUN ./gradlew :app:bootJar --no-daemon
#RUN cp app/build/libs/exchangerates*.jar app.jar
#RUN java -Djarmode=layertools -jar app.jar extract
#
## Create a custom JRE
## --add-modules: We include only what Spring Boot Web needs
#RUN $JAVA_HOME/bin/jlink \
#    --add-modules java.base,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,jdk.unsupported \
#    --verbose \
#    --strip-debug \
#    --compress 2 \
#    --no-header-files \
#    --no-man-pages \
#    --output /custom-jre
#
## --- Stage 2: Tiny Runtime Stage ---
#FROM alpine:latest AS runtime
#WORKDIR /app
#
## Install dependencies needed by the JVM on Alpine
#RUN apk add --no-cache gcompat libstdc++
#
## Copy the CUSTOM JRE we just built
#COPY --from=builder /custom-jre /opt/java/runtime
#ENV PATH="/opt/java/runtime/bin:$PATH"
#
## Copy the extracted layers
#COPY --from=builder /app/dependencies/ ./
#COPY --from=builder /app/spring-boot-loader/ ./
#COPY --from=builder /app/snapshot-dependencies/ ./
#COPY --from=builder /app/application/ ./
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]



#### IMPROVED IMAGE#2 - < 300 MB
## --- Stage 1: Build & Extract (The heavy lifting) ---
#FROM amazoncorretto:25 AS builder
#WORKDIR /app
#RUN yum install -y findutils
#COPY . .
#RUN ./gradlew :app:bootJar --no-daemon
#RUN cp app/build/libs/exchangerates*.jar app.jar
#RUN java -Djarmode=layertools -jar app.jar extract
#
## --- Stage 2: Runtime (The tiny part) ---
## Switching to Liberica JRE Alpine - specifically designed for microservices
#FROM bellsoft/liberica-openjre-alpine:25 AS runtime
#WORKDIR /app
#
## Copy the extracted layers
#COPY --from=builder /app/dependencies/ ./
#COPY --from=builder /app/spring-boot-loader/ ./
#COPY --from=builder /app/snapshot-dependencies/ ./
#COPY --from=builder /app/application/ ./
#
#EXPOSE 8080
#ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]


#### FIRST IMAGE
#FROM ghcr.io/graalvm/jdk-community:25
#WORKDIR /app
#COPY app/build/libs/exchangerates.jar /app
#EXPOSE 8080
#CMD ["java", "-jar", "exchangerates.jar"]


#### IMPROVED IMAGE#1 < 600MB
## Stage 1: Build the JAR
#FROM amazoncorretto:25 AS builder
#WORKDIR /app
#
## Install xargs (part of findutils) so gradlew can run
#RUN yum install -y findutils
#
## Copy gradle files first
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#COPY . .
#
## Build the executable JAR
#RUN ./gradlew :app:bootJar --no-daemon
#
## Stage 2: Runtime
#FROM amazoncorretto:25-alpine
#WORKDIR /app
#
## Note: Using archiveBaseName "exchangerates" from your build.gradle
#COPY --from=builder /app/app/build/libs/exchangerates-*.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]