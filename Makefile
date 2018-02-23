.PHONY: build clean test install

PREFIX ?= "${HOME}/local"
VERSION := $(shell git describe)

build:
	sbt compile pack

clean:
	sbt clean

docker-build:
	docker build -t exaspace/avrotool:$(VERSION) .

docker-push:
	docker push exaspace/avrotool:$(VERSION)

install:
	cd target/pack && $(MAKE) install PREFIX=${PREFIX}
	@echo "avrotool installed in ${PREFIX}/bin (You may need to add this directory to your PATH)"

test:
	sbt test

