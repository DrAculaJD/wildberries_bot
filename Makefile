build:
	make -C app build

run-dist:
	java -jar app/out/artifacts/app_jar/app.jar

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

.PHONY: build
