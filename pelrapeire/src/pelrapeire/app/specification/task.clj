(ns pelrapeire.app.specification.task
  ^{:doc "this module preconditions data that is sent in by form submission"}
  (:use pelrapeire.app.convert
	pelrapeire.app.specification.conditioners
	pelrapeire.app.validators)
  (:require [clojure.contrib.str-utils2 :as s])
  (:import org.joda.time.DateTime
	   org.joda.time.DateTimeZone))

(def template-task
     {"type" "task" 
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
      "namespace" nil})

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
     (vector? ns) (filter #(not (nil? %)) (map ns-string-to-map ns))
     true (throw (IllegalArgumentException. "the argument has an invalid type"))))

(defn condition-priority [p]
  {:pre [(or (nil? p) (#{"1" "2" "3"} p))]}
  (Integer/parseInt p))

(defn condition-progress [p]
  {:pre [(progress? p)]}
   p)

(def condition-fns
     {"_id" (fn [s] {:pre (id? s)} s)
      "_rev" (fn [s] {:pre (revision? s)} s)
      "title" (fn [t] (. t trim))
      "specification" (fn [s]
			(if (s/blank? s) nil (. s trim)))
      "project" (fn [p] p)
      "deliversUserFunctionality" (fn [f]
				    (if (= "true" f) true false))
      "progress" condition-progress
      "priority" condition-priority
      "namespace" ns-param-to-vector-map}
     )

(defn 
  #^{:doc "this function is responsible for preprocessing task data before that task data
is sent to the backend for update. Conditioning should only be done for data that already
exists in the database and is presently undergoing a update"}
  condition-task [map-data]
  (condition-obj condition-fns map-data))

(defn 
  #^{:doc "this function is responsible for creating a task that meets
specifications"}
  create-task [map-data]
  {:pre [(not (s/blank? (map-data "title"))) (not (s/blank? (map-data "project")))]
   :post [(not (nil? (% "title")))
	  (not (nil? (% "project")))
	  (not (nil? (% "taskCreateDate")))]}
  (let [spec ((condition-fns "specification") (map-data "specification"))
	user-func ((condition-fns "deliversUserFunctionality") (map-data "deliversUserFunctionality"))
	create-dt (datetime-to-vector (DateTime. DateTimeZone/UTC))
	nm-space ((condition-fns "namespace") (map-data "namespace"))
	filtered-data (select-keys map-data (keys template-task))
	conditioned-data (assoc filtered-data "specification" spec
				"deliversUserFunctionality" user-func
				"namespace" nm-space
				"taskCreateDate" create-dt)]
    (conj template-task conditioned-data)))

