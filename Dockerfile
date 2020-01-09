FROM openjdk:8
MAINTAINER "http://outerboxtech.com.br/"
ADD target/obt-sca-api.jar .
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=oauth-security,prod", "obt-sca-api.jar"]