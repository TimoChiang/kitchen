FROM adoptopenjdk/openjdk11:alpine AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build || return 0
COPY . .
RUN ./gradlew build

FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-alpine-slim
COPY --from=TEMP_BUILD_IMAGE /usr/app/build/libs/kitchen-0.1.0.jar .
ENTRYPOINT ["java","-jar","/kitchen-0.1.0.jar"]