build:
	make -C app build

run-dist:
	make -C run-dist

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

.PHONY: build
