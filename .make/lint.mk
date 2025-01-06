lint-check:
	@mvn spotless:check

lint-check-ci:
	@mvn spotless:check --batch-mode --no-transfer-progress

lint:
	@mvn spotless:apply