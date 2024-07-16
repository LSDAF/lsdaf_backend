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

build:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build --no-cache

up:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml up -d


down:
	COMPOSE_PROFILES=db,backend docker-compose --env-file env/env.properties -f dc-local.yml down

logs:
	COMPOSE_PROFILES=db,backend docker-compose --env-file env/env.properties -f dc-local.yml logs -f

prune:
	@docker system prune -a -f
	@docker image prune -f

help:
	@echo "[Containers]"
	@echo "> build               |-----------------------------------------|  Build docker images"
	@echo "> up                  |-----------------------------------------|  Up docker images"
	@echo "> down                |-----------------------------------------|  Down docker images"
	@echo "> dbup                |-----------------------------------------|  Runs postgresql db + pgadmin docker images"
	@echo "> dbdown              |-----------------------------------------|  Kills postgresql db + pgadmin docker images"
	@echo "> prune               |-----------------------------------------|  Cleans all docker local storages"
	@echo "> logs                |-----------------------------------------|  Reads all logs from docker images"
	@echo ""
	@echo "[Java Project]"
	@echo "> install             |-----------------------------------------|  Build locally Java Project"
	@echo "> test                |-----------------------------------------|  Runs BDD & unit tests"
	@echo "> clean               |-----------------------------------------|  Clean local build files"
