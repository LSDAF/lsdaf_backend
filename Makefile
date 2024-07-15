default:
	@echo "${COLOR_YELLOW_BOLD_UNDERLINE}[Containers]${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}build${COLOR_DEFAULT}               |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Build Docker images${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}up${COLOR_DEFAULT}                  |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Up Docker images${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}down${COLOR_DEFAULT}                |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Down Docker images${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}dbup${COLOR_DEFAULT}                |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Runs postgresql db + pgadmin docker images${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}dbdown${COLOR_DEFAULT}              |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Kills postgresql db + pgadmin docker images${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}prune${COLOR_DEFAULT}               |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Cleans all docker local storages${COLOR_RESET}"
	@echo ""
	@echo "${COLOR_YELLOW_BOLD_UNDERLINE}[Java Project]${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}install${COLOR_DEFAULT}             |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Build locally Java Project${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}test${COLOR_DEFAULT}                |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Runs BDD & unit tests${COLOR_RESET}"
	@echo "${COLOR_DEFAULT}> ${COLOR_GREEN_BOLD}clean${COLOR_DEFAULT}               |-----------------------------------------|  ${COLOR_DEFAULT_BOLD_ITALIC}Clean local build files${COLOR_RESET}"

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
