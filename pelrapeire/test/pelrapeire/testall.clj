
(ns pelrapeire.testall
  (:use clojure.test)
  (:require pelrapeire.pages.tilestest
	    pelrapeire.repository.db.dbapitest
	    pelrapeire.repository.db.dbapiwrappertest))

(run-tests 'pelrapeire.pages.tilestest)
(run-tests 'pelrapeire.repository.db.dbapitest)
(run-tests 'pelrapeire.repository.db.dbapiwrappertest)
