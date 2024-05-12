FROM openjdk:21
VOLUME /tmp
ENV PORT 8080
EXPOSE 8080
RUN mkdir -p /app

ENV SPRING_DATASOURCE_URL: jdbc:mysql://localhost:3306/BartenderToolkit
ENV SPRING_DATASOURCE_USERNAME: root
ENV SPRING_DATASOURCE_PASSWORD: 'airbus'

ARG JAR_FILE=build/libs/BartenderToolkit-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app/BartenderToolkit-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/BartenderToolkit-0.0.1-SNAPSHOT.jar"]