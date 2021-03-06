.PHONY: build clean docker-build docker-push install test

PREFIX ?= "${HOME}/local"
VERSION := $(shell git describe)

build:
	sbt compile pack

clean:
	sbt clean

docker-build:
	docker build -t exaspace/avrotool:$(VERSION) ./
	docker tag exaspace/avrotool:$(VERSION) exaspace/avrotool:latest

docker-push:
	docker push exaspace/avrotool:$(VERSION)
	docker push exaspace/avrotool:latest

install:
	cd target/pack && $(MAKE) install PREFIX=${PREFIX}
	@echo "avrotool installed in ${PREFIX}/bin (You may need to add this directory to your PATH)"

test:
	sbt test
