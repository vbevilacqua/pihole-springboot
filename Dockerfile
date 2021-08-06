FROM fabric8/java-centos-openjdk11-jre

ENV JAVA_OPTS="-Xmx256m"

EXPOSE 8080

ADD target/*.jar /app.jar

CMD echo "Jimmy application starting..." && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar