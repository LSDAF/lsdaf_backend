install: clean
	@mvn install -DskipTests -U -fae -Dcopy-env -DskipSurefireReport

admin-install:
	@npm --prefix ./lsadf_admin install

javadoc:
	@mvn javadoc:aggregate