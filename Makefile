default: help
install:
	@mvn clean install -DskipTests -U -fae

test:
	@mvn test

test-ci:
	@mvn test --batch-mode --no-transfer-progress -Dci

clean:
	@mvn clean

javadoc:
	@mvn javadoc:aggregate

lint-check:
	@mvn rewrite:dryRun

lint-check-ci:
	@mvn rewrite:dryRun --batch-mode --no-transfer-progress

lint:
	@mvn rewrite:run

dbup:
	COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml up -d

dbdown:
	COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml down

monitorup:
	COMPOSE_PROFILES=db,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

monitordown:
	COMPOSE_PROFILES=db,monitoring docker-compose --env-file env/env.properties -f dc-local.yml down

build:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

builddev:
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build

build-no-cache:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build --no-cache

up:
	COMPOSE_PROFILES=db,backend,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

updev:
	COMPOSE_PROFILES=db,backend_dev,monitoring docker-compose --env-file env/env.properties -f dc-local.yml up -d

down:
	COMPOSE_PROFILES=db,backend,monitoring,backend_dev docker-compose --env-file env/env.properties -f dc-local.yml down

logs:
	COMPOSE_PROFILES=db,backend,monitoring docker-compose --env-file env/env.properties -f dc-local.yml logs -f

prune:
	@docker system prune -a -f
	@docker image prune -f

purge: purgedb purgelogs

purgedb:
	@rm -rf docker/data/postgres/*
	@echo "Postgres data purged"

purgelogs:
	@rm -rf docker/data/lsadf_backend_data/*
	@rm -rf docker/data/lsadf_backend_dev_data/*
	@echo "Backend logs purged"

help:
	@echo "[Containers]"
	@echo "> build               |-----------------------------------------|  Build docker images"
	@echo "> build-no-cache      |-----------------------------------------|  Build docker images with --no-cache option"
	@echo "> up                  |-----------------------------------------|  Up docker images"
	@echo "> down                |-----------------------------------------|  Down docker images"
	@echo "> dbup                |-----------------------------------------|  Runs postgresql db + redis docker images"
	@echo "> dbdown              |-----------------------------------------|  Kills postgresql db + redis docker images"
	@echo "> monitorup           |-----------------------------------------|  Runs db docker images + monitoring tools"
	@echo "> monitordown         |-----------------------------------------|  Kills db + monitoring tools docker images"
	@echo "> prune               |-----------------------------------------|  Cleans all docker local storages"
	@echo "> purge               |-----------------------------------------|  Performs purgedb and purgelogs"
	@echo "> purgedb             |-----------------------------------------|  Cleans all data from docker-data folder for db"
	@echo "> purgelogs           |-----------------------------------------|  Cleans all logs from docker-data folder for lsadf_backend_data"
	@echo "> logs                |-----------------------------------------|  Reads all logs from docker images"
	@echo ""
	@echo "[Java Project]"
	@echo "> install             |-----------------------------------------|  Build locally Java Project"
	@echo "> test                |-----------------------------------------|  Runs BDD & unit tests"
	@echo "> test-ci             |-----------------------------------------|  Runs BDD & unit tests with few logs for CI"
	@echo "> clean               |-----------------------------------------|  Clean local build files"
	@echo "> javadoc             |-----------------------------------------|  Generates all project JavaDoc files"
	@echo "> lint-check-ci       |-----------------------------------------|  Dry lints code for CI. Throws error if code not linted"
	@echo "> lint-check          |-----------------------------------------|  Dry lints code and lists issues in target/rewrite folder"
	@echo "> lint                |-----------------------------------------|  Lints and fixes issues in source code"
