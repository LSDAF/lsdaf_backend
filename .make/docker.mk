GIT_BRANCH := $(shell git rev-parse --abbrev-ref HEAD | tr '/' '-')
dbup:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml up -d

dbdown:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db docker-compose --env-file env/env.properties -f dc-local.yml down


monitordown:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=db,monitoring docker-compose --env-file env/env.properties -f dc-local.yml down

upadmin:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml up -d

upadmindev:
	@echo "Using GIT_BRANCH=$(GIT_BRANCH)"
	BRANCH_NAME=$(GIT_BRANCH) COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml up -d