(ns pelrapeire.app.specification.project
  (:require [clojure.contrib.str-utils2 :as s]))

(def template-project
     {"type" "project"})

(defn 
  ^{:doc "since the project name will become the id we only have one field to set here.  The project
id itself is set through an overload of the db-create api"}
  create-project []
  template-project)