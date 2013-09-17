all:
	mvn install

example: all
	$(MAKE) -C indexablelistview-sample/

.DEFAULT_GOAL := example
