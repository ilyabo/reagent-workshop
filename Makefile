CLIENT_PATH=./

CLJSBUILD = client


run: autocompile

open:
	(sleep 1 && open index.html) &


autocompile:
	@cd "$(CLIENT_PATH)" && \
	rm -rf target; \
	lein with-profile dev cljsbuild auto $(CLJSBUILD)

dist:
	@cd "$(CLIENT_PATH)" && \
	rm -rf target  && \
	lein do with-profile prod cljsbuild once

clean:
	@cd "$(CLIENT_PATH)" && \
	lein -o clean
