check-deps:
	make -C app check-deps
	
setup:
	make -C app setup

clean:
	make -C app clean

build:
	make -C app build

run:
	make -C app run

install:
	make -C app install

lint:
	make -C app lint

test:
	make -C app test

report:
	make -C app report