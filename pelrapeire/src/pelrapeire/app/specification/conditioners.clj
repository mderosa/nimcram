(ns pelrapeire.app.specification.conditioners
  (:use pelrapeire.app.validators)
  (:require [clojure.string :as cs]))

(defn string-param-to-vector [ns]
    (cond 
     (nil? ns) []
     (string? ns) (string-param-to-vector [ns])
     (coll? ns) (vec (filter #(not (nil? %)) ns))
     true (throw (IllegalArgumentException. "the argument has an invalid type"))))


(defn csv-string-to-vector [^String str]
  (if str
    (vec (filter #(not= "" %) (map cs/trim (cs/split str #","))))
    []))

(defn 
  #^{:doc "this function is responsible for preprocessing object data before that object data
is sent to the backend for update. Conditioning should only be done for data that already
exists in the database and is presently undergoing a update"}
  condition-obj [condition-fns map-data]
  {:pre [(id? (map-data "_id"))
	 (revision? (map-data "_rev"))]}
  (loop [unprocessed-data map-data 
	 processed-data {}]
    (let [current-key (first (first unprocessed-data))
	  current-val (second (first unprocessed-data))
	  conditioning-fn (condition-fns current-key)]
      (if (and (nil? current-key) (nil? current-val))
	processed-data
	(recur (rest unprocessed-data) 
	       (if (nil? conditioning-fn)
		 processed-data
		 (assoc processed-data 
		   current-key
		   ((condition-fns current-key) current-val))))))))