
(ns 
    #^{:doc "this is a high level api that insulates clojure from
having to interact with couch db through strings"}
  pelrapeire.repository.db.dbapiwrapper
  (:use clojure.contrib.json.read
	clojure.contrib.json.write))

(defn wrapper-get
  #^{:pre [(not (nil? id))]
     :post [(and (% "_id") (% "_rev"))]}
  [#^String id fn-access db-config]
  (let [json-data (fn-access id db-config)]
    (read-json json-data)))

(defn 
  #^{:doc "determines if a give map is an initial revision based on the logic
exists key='_rev' and the rev is like '1_%w'"}
  initial-rev? [map]
  (let [regex #"^(\d)-.+"
	val (map "_rev")]
    (if val
      (= "1" (get (re-find regex val) 1))
      false)))

(defn wrapper-create 
  (#^{:pre [(and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (not (nil? %)) (not (nil? (% "_id"))) (not (nil? (% "_rev"))))
	     (initial-rev? %)]}
   [map access-fn db-config]
     (let [json-in (json-str map)
	   json-out (access-fn json-in db-config)]
       (read-json json-out)))
  (#^{:pre [(not (nil? id))
	    (and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (% "_id") (% "_rev"))
	     (initial-rev? %)]}
   [id map access-fn db-config]
     (let [json-in (json-str map)
	   json-out (access-fn id json-in db-config)]
       (read-json json-out))))

(defn 
  wrapper-update
  #^{:pre [(not (nil? (map-data "_id"))) (not (nil? (map-data "_rev")))
	   (or (= mode :append) (= mode :write))]
     :post [(and (% "_id") (% "_rev"))]}
  [map-data mode fn-get fn-put db-config]
  (if (= mode :write)
    (let [str-json (fn-put (map-data "_id") (json-str map-data) db-config)]
      (read-json str-json))
    (let [str-json-org (fn-get (map-data "_id") db-config)
	  map-merged (merge (read-json str-json-org) map-data)
	  str-json-out (fn-put (map-data "_id") (json-str map-merged) db-config)]
      (read-json str-json-out))))

(defn 
  wrapper-delete
  #^{:pre [(not (nil? (map-data "_id"))) (not (nil? (map-data "_rev")))]
     :post (nil? %)}
  [map-data fn-del db-config]
  (read-json (fn-del (map-data "_id") (map-data "_rev") db-config)))
