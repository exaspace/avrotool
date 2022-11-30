FROM eclipse-temurin:18

LABEL maintainer=ben.george@exaspace.org

ADD target/pack /avrotool
ADD docker/entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

CMD ["--help"]
