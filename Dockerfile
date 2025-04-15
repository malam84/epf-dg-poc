# FROM image-registry.openshift-image-registry.svc:5000/openshift/java-runtime:openjdk-17-ubi8
FROM quay.io/malam/devops/maven:3.8.3-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM image-registry.openshift-image-registry.svc:5000/openshift/java-runtime:openjdk-17-ubi8

COPY dg.crt /usr/local/share/ca-certificates/

ENV JAVA_OPTS=-Djavax.net.debug=ssl


WORKDIR /app
COPY --from=build /app/target/*.jar kwsp.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "kwsp.jar"]
