.DEFAULT_GOAL := all
NAME=flatttl

app/build/libs/app-all.jar:
	./gradlew -q shadowJar

dist/$(NAME)/$(NAME).jar: app/build/libs/app-all.jar
	mkdir -p dist/$(NAME)
	cp $< $@

dist/$(NAME)/$(NAME): $(NAME).sh
	mkdir -p dist/$(NAME)
	cp $< $@

dist/$(NAME).tgz: dist/$(NAME)/$(NAME).jar dist/$(NAME)/$(NAME)
	tar --strip-components 1 -czf $@ dist/$(NAME)

.PHONY: all
all: dist/$(NAME).tgz

.PHONY: clean
clean:
	rm -rf dist
	./gradlew -q clean
