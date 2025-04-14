FROM image-registry.openshift-image-registry.svc:5000/openshift/java-runtime:openjdk-17-ubi8
EXPOSE 8080
COPY kwsp.jar kwsp.jar
ENTRYPOINT ["java", "-jar", "kwsp.jar"]
