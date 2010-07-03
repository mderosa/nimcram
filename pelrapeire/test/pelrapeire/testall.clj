
(ns pelrapeire.testall
  (:use clojure.test)
  (:require pelrapeire.pages.tilestest
	    pelrapeire.repository.dbapitest
	    pelrapeire.repository.dbapiwrappertest))

(run-tests 'pelrapeire.pages.tilestest)
(run-tests 'pelrapeire.repository.dbapitest)
(run-tests 'pelrapeire.repository.dbapiwrappertest)