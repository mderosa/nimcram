
(ns 
    #^{:doc "this is a high level api that insulates clojure from
having to interact with couch db through strings"}
  pelrapeire.repository.dbops
  (:use pelrapeire.dbconfig
	clojure.contrib.json.read
	clojure.contrib.json.write))

(defn repo-get
  #^{:pre [(not (nil? id))]
     :post [(and (% "_id") (% "_rev"))]}
  [#^String id fn-access]
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

(defn repo-create 
  (#^{:pre [(and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (not (nil? %)) (not (nil? (% "_id"))) (not (nil? (% "_rev"))))
	     (initial-rev? %)]}
   [map access-fn]
     (let [json-in (json-str map)
	   json-out (access-fn json-in db-config)]
       (read-json json-out)))
  (#^{:pre [(not (nil? id))
	    (and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (% "_id") (% "_rev"))
	     (initial-rev? %)]}
   [id map access-fn]
     (let [json-in (json-str map)
	   json-out (access-fn id json-in db-config)]
       (read-json json-out))))

(defn 
  repo-update
  #^{:pre [(not (nil? (map "_id"))) (not (nil? (map "_rev")))
	   (or (= mode :append) (= mode :write))]
     :post [(and (% "_id") (% "_rev"))]}
  [map mode access-fn]
  (if (= mode :write)
    (let [json-out (access-fn (map "_id") (json-str map) db-config)]
      (read-json json-out))
    ()))

(defn 
  repo-delete
  #^{:pre [(not (nil? (map "_id"))) (not (nil? (map "_rev")))]
     :post (nil? %)}
  [#^String map])