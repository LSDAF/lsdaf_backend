default: help
install:
	@mvn clean install -DskipTests -U -fae

test:
	@mvn test

clean:
	@mvn clean

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

purgedb:
	@rm -rf docker/data/postgres/*
	@echo "Postgres data purged"

purgelogs:
	@rm -rf docker/data/lsadf_backend_data/*
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
	@echo "> purgedb             |-----------------------------------------|  Cleans all data from docker-data folder for db"
	@echo "> purgelogs           |-----------------------------------------|  Cleans all logs from docker-data folder for lsadf_backend_data"
	@echo "> logs                |-----------------------------------------|  Reads all logs from docker images"
	@echo ""
	@echo "[Java Project]"
	@echo "> install             |-----------------------------------------|  Build locally Java Project"
	@echo "> test                |-----------------------------------------|  Runs BDD & unit tests"
	@echo "> clean               |-----------------------------------------|  Clean local build files"
