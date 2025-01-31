include .make/*

default: help

install-venv-help:
	@echo 'Run the following commands to install the virtual environment:'
	@echo 'python3 -m venv .venv'
	@echo 'source .venv/bin/activate'
	@echo 'pip install -r requirements.txt'


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