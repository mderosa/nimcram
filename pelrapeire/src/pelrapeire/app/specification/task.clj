(ns pelrapeire.app.specification.task
  (:use pelrapeire.app.convert)
  (:require [clojure.contrib.str-utils2 :as s])
  (:import org.joda.time.DateTime))

(defn 
  #^{:doc "this function is responsible for creating a task that meets
specifications"}
  create-task [map-data]
  {:pre [(map-data "title") (map-data "project")]
   :post [(not (nil? (% "title")))
	  (not (nil? (% "project")))
	  (not (nil? (% "taskCreateDate")))]}
  (let [spec (if (s/blank? (map-data "specification"))
	       nil 
	       (map-data "specification"))
	user-func (if (= "true" (map-data "deliversUserFunctionality"))
		    true
		    false)
	create-dt (datetime-to-vector (DateTime.))
	conditioned-data (assoc map-data "specification" spec
				"deliversUserFunctionality" user-func
				"taskCreateDate" create-dt)

	default {"type" "task" 
		 "title" nil
		 "specification" nil
		 "project" nil
		 "solutionTeam" []
		 "deliversUserFunctionality" false
		 "taskCreateDate" nil
		 "taskStartDate" nil
		 "taskCompleteDate" nil
		 "progress" "proposed"
		 "priority" nil
		 "namespace" []}]
    (conj default conditioned-data)))