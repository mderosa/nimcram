(ns pelrapeire.app.specification.project
  (:use pelrapeire.app.validators
	pelrapeire.app.specification.conditioners)
  (:require [clojure.contrib.str-utils2 :as s]))

(def template-project
     {"type" "project"
      "contributors" []
      "followers" []})

(defn 
  ^{:doc "since the project name will become the id we only have one field to set here.  The project
id itself is set through an overload of the db-create api"}
  create-project [map-data]
  {:pre [(map-data "contributors") (email? (map-data "contributors"))]}
  (let [contributors (string-param-to-vector (map-data "contributors"))]
    (assoc template-project "contributors" contributors)))