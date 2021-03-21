FROM adoptopenjdk/openjdk11-openj9
MAINTAINER Felix Honer
COPY build/libs/inzidenz-sms-0.1.jar /home/inzidenz-sms-0.1.jar
ENTRYPOINT ["sh", "-c", "java -jar /home/inzidenz-sms-0.1.jar $params"]
