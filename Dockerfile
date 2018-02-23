FROM openjdk:9.0.1-jdk

LABEL maintainer=ben.george@exaspace.org

ADD target/pack /avrotool
ADD docker/entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

CMD ["--help"]
