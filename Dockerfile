FROM hseeberger/scala-sbt:8u151-2.12.4-1.1.1
LABEL maintainer=ben.george@exaspace.org
WORKDIR /work
ADD . /work/
RUN sbt pack
ENTRYPOINT /work/target/pack/bin/avrotool
