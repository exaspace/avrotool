.PHONY: build clean test install

PREFIX ?= "${HOME}/local"

build:
	sbt compile pack

clean:
	sbt clean

install:
	cd target/pack && $(MAKE) install PREFIX=${PREFIX}
	@echo "avrotool installed in ${PREFIX}/bin (You may need to add this directory to your PATH)"

test:
	sbt test

