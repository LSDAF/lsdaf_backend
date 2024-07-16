default: help
install:
	@mvn clean install -DskipTests -U -fae

test:
	@mvn test

clean:
	@mvn clean

dbup:
	@docker-compose --env-file env/env.properties -f dc-local.yml --profile db up

dbdown:
	@docker-compose --env-file env/env.properties -f dc-local.yml --profile db down

build:
	@docker-compose --env-file env/env.properties -f dc-local.yml --profile backend build

up:
	@docker-compose --env-file env/env.properties -f dc-local.yml --profile backend up


down:
	@docker-compose --env-file env/env.properties -f dc-local.yml down


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
	@echo ""
	@echo "[Java Project]"
	@echo "> install             |-----------------------------------------|  Build locally Java Project"
	@echo "> test                |-----------------------------------------|  Runs BDD & unit tests"
	@echo "> clean               |-----------------------------------------|  Clean local build files"
