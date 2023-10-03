FROM openjdk:17-alpine
COPY target/batch_website_canary_scan.jar batch_website_canary_scan.jar
ENTRYPOINT ["java", "-jar", "batch_website_canary_scan.jar"]