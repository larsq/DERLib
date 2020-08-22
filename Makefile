GRADLE_CMD=./gradlew
DEV_VERSION:=$$(./bin/version dev)
CANDIDATE_VERSION:=$$(./bin/version candidate "${APPLICATION_VERSION}")
VERSION_TAG=$$([ -z "${APPLICATION_VERSION}" ]  && echo "${DEV_VERSION}" || echo "${CANDIDATE_VERSION}")

publish: assemble
	;

build: clean
	${GRADLE_CMD} build

assemble: build
	${GRADLE_CMD} -Pversion="${VERSION_TAG}" assemble

test: clean
	${GRADLE_CMD} test

clean:
	${GRADLE_CMD} clean

version:
	@echo "${VERSION_TAG}"