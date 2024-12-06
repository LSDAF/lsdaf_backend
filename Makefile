default: help
install: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport

install-ci: clean
	@mvn install -DskipTests -U -fae -DskipSurefireReport -q --batch-mode --no-transfer-progress

test:
	@mvn verify -DskipSurefireReport

test-unit:
	@mvn test -fae

test-bdd:
	@mvn package failsafe:verify -DskipSurefireReport -fae

test-ci:
	@mvn verify --batch-mode --no-transfer-progress -Dci -fae -DskipSurefireReport

report:
	@mvn surefire-report:report-only -DcucumberReport

clean:
	@mvn clean -q

admin-install:
	@npm --prefix ./lsadf_admin install

javadoc:
	@mvn javadoc:aggregate

lint-check:
	@mvn spotless:check

lint-check-ci:
	@mvn spotless:check --batch-mode --no-transfer-progress

lint:
	@mvn spotless:apply

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

build-ci:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

builddev: install
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build

builddev-ci: install-ci
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

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

install-pre-commit:
	pre-commit clean
	pre-commit install

help:
	@echo "[Containers]"
	@echo "> build               |-----------------------------------------|  Build docker images"
	@echo "> builddev            |-----------------------------------------|  Build docker dev images"
	@echo "> build-ci            |-----------------------------------------|  Build docker images for CI"
	@echo "> builddev-ci         |-----------------------------------------|  Build docker dev images for CI"
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
	@echo "[CI/CD]"
	@echo "> test-ci             |-----------------------------------------|  Runs BDD & unit tests with few logs for CI"
	@echo "> lint-check-ci       |-----------------------------------------|  Dry lints code for CI. Throws error if code not linted"
	@echo "> report              |-----------------------------------------|  Generates report for unit tests"
	@echo ""
	@echo "[Java Project]"
	@echo "> install             |-----------------------------------------|  Build locally Java Project"
	@echo "> install-ci          |-----------------------------------------|  Build locally Java Project for CI"
	@echo "> admin-install       |-----------------------------------------|  Build locally npm project for admin UI"
	@echo "> test                |-----------------------------------------|  Runs BDD & unit tests"
	@echo "> test-unit           |-----------------------------------------|  Runs unit tests only"
	@echo "> test-bdd            |-----------------------------------------|  Runs BDD tests only"
	@echo "> clean               |-----------------------------------------|  Clean local build files"
	@echo "> javadoc             |-----------------------------------------|  Generates all project JavaDoc files"
	@echo "> lint-check          |-----------------------------------------|  Dry lints code and lists issues in target/rewrite folder"
	@echo "> lint                |-----------------------------------------|  Lints and fixes issues in source code"
	@echo "[Dev Environment]"
	@echo "> install-pre-commit  |-----------------------------------------|  Install pre-commit hooks"
	@echo "> install-venv-help   |-----------------------------------------|  Show help to install virtual environment"