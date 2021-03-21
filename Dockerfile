FROM adoptopenjdk/openjdk11-openj9
MAINTAINER Felix Honer

RUN apt update && apt-get install -y git wget unzip
RUN git clone https://github.com/fhoner/incidence-sms.git

RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
RUN unzip gradle-6.5-bin.zip
ENV GRADLE_HOME /gradle-6.5
ENV PATH $PATH:/gradle-6.5/bin

WORKDIR incidence-sms
RUN gradle clean build --rerun-tasks --no-build-cache
ENTRYPOINT ["sh", "-c", "java -jar build/libs/inzidenz-sms-0.1.jar $params"]
