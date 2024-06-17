check-deps:
	./gradlew dependencyUpdates -Drevision=release

setup:
	gradle wrapper --gradle-version 8.7

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew run

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain

test:
	./gradlew test

.PHONY: build