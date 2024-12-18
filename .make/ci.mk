clean-ci:
	@mvn clean -q

install-ci: clean-ci
	@mvn install -DskipTests -U -fae -DskipSurefireReport -q --batch-mode --no-transfer-progress

test-ci:
	@mvn verify --batch-mode --no-transfer-progress -Dci -fae -DskipSurefireReport

build-ci:
	COMPOSE_PROFILES=backend docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

build-dev-ci: install-ci
	COMPOSE_PROFILES=backend_dev docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build

build-admin-dev-ci: install-ci
	COMPOSE_PROFILES=backend_admin_dev docker-compose --env-file env/env.properties -f dc-local.yml --progress quiet build
