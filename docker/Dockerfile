FROM mcr.microsoft.com/java/maven:8u212-zulu-debian9

######################################################################
### Install extensions
######################################################################
RUN apt-get update && apt-get install --yes --force-yes \
	wget \
	nano \
	&& rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /usr/share/doc/*

COPY prod /srv

RUN wget https://www-us.apache.org/dist/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz -P /tmp && \
    tar xf /tmp/apache-maven-*.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.6.0 /opt/maven

######################################################################
### Config
######################################################################
RUN echo  '                                                     \n\
export JAVA_HOME=/usr/lib/jvm/default-java                      \n\
export M2_HOME=/opt/maven                                       \n\
export MAVEN_HOME=/opt/maven                                    \n\
export PATH=${M2_HOME}/bin:${PATH}                              \n'\
>> /etc/profile.d/maven.sh

RUN chmod +x /etc/profile.d/maven.sh
RUN /bin/bash -c "source /etc/profile.d/maven.sh"

ENTRYPOINT ["java", "-jar", "/srv/target/movie-rental-store-application-0.0.1-SNAPSHOT.jar"]
