
(ns pelrapeire.testall
  (:use clojure.test)
  (:require pelrapeire.pages.tilestest))

(run-tests 'pelrapeire.pages.tilestest)
(run-tests 'pelrapeire.repository.dbapitest)