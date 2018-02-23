FROM hseeberger/scala-sbt:8u151-2.12.4-1.1.1
LABEL maintainer=ben.george@exaspace.org
ADD . /avrotool/
RUN cd /avrotool && sbt pack
ENTRYPOINT /avrotool/docker/entrypoint.sh
