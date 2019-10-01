FROM openjdk:11-slim

WORKDIR /mylights

COPY target/*.jar mylights.jar

#VOLUME /tmp

EXPOSE 80

ENTRYPOINT ["java", "-jar", "mylights.jar"]
