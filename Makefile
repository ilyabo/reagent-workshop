run: autocompile

open:
	(sleep 1 && open index.html) &

autocompile:
	rm -rf target; \
	lein with-profile dev cljsbuild auto client

dist:
	rm -rf target  && \
	lein do with-profile prod cljsbuild once