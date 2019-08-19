FROM openjdk:11-slim as build
WORKDIR /MyLights

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -Dmaven.test.skip
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*jar)

FROM openjdk:11-jre-slim
WORKDIR /MyLights

VOLUME /tmp

ARG DEPENDENCY=./MyLights/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes ./

EXPOSE 80

ENTRYPOINT ["java","-cp","./:./lib/*","com.syn.MyLightsServer.MyLightsServerApplication","--spring.config.location=classpath:/"]