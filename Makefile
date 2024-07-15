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
