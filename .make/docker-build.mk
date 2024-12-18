GIT_BRANCH := $(shell git rev-parse --abbrev-ref HEAD | tr '/' '-')

build:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api: install
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api-dev: install
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build

build-admin: install-docker admin-install
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) BRANCH_NAME=$(shell git rev-parse --abbrev-ref HEAD) COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml build --build-arg BRANCH_NAME=$BRANCH_NAME

build-admin-dev: install-docker admin-install
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml build
