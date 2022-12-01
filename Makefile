.PHONY: build clean docker-build docker-push install test

PREFIX ?= "${HOME}/local"
VERSION := $(shell git describe --tags)

build:
	sbt compile test pack

clean:
	sbt clean

docker-build: build
	docker build -t exaspace/avrotool:$(VERSION) ./
	docker tag exaspace/avrotool:$(VERSION) exaspace/avrotool:latest

docker-login: 
	# WARNING ensure silent output to avoid printing secrets to stdout
	@test -n "$(DOCKER_HUB_TOKEN)"
	@echo $(DOCKER_HUB_TOKEN) | docker login --username exaspace --password-stdin

docker-push: docker-build docker-login
	docker push exaspace/avrotool:$(VERSION)
	docker push exaspace/avrotool:latest

install:
	cd target/pack && $(MAKE) install PREFIX=${PREFIX}
	@echo "avrotool installed in ${PREFIX}/bin (You may need to add this directory to your PATH)"

test:
	sbt test
