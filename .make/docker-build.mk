build:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api: install
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml build

build-api-dev: install
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml build

build-admin: install admin-install
	COMPOSE_PROFILES=backend_admin docker-compose --env-file env/env.properties -f dc-local.yml build

build-admin-dev: install admin-install
	COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml build
