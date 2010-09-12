(ns pelrapeire.app.specification.task
  (:use pelrapeire.app.convert)
  (:require [clojure.contrib.str-utils2 :as s])
  (:import org.joda.time.DateTime))

(defn ns-string-to-map [ns]
  (if (s/blank? ns) 
    nil
    (let [halves (. ns split "=")]
      (cond 
       (not (= 2 (alength halves))) nil
       (s/blank? (aget halves 0)) nil
       (s/blank? (aget halves 1)) nil
       true {(aget halves 0) (aget halves 1)}))))

(defn ns-param-to-vector-map [ns]
    (cond 
     (nil? ns) []
     (string? ns) (ns-param-to-vector-map [ns])
     (vector? ns) (filter #(not (nil? %)) (map ns-string-to-map ns))))

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
	nm-space (ns-param-to-vector-map (map-data "namespace"))
	conditioned-data (assoc map-data "specification" spec
				"deliversUserFunctionality" user-func
				"namespace" nm-space
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
		 "namespace" nm-space}]
    (conj default conditioned-data)))
