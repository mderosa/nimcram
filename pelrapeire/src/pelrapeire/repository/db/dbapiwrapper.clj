(ns 
    #^{:doc "this is a high level api that insulates clojure from
having to interact with couch db through strings"}
  pelrapeire.repository.db.dbapiwrapper
  (:use clojure.contrib.json))

(defn 
  #^{:doc "this is a view specific wrapper.  we need it because view results dont
satisfy the post condition of a simple get wrapper"}
  wrapper-view [#^String loc fn-access db-config]
  {:pre [(not (nil? loc))]
   :post [(and (% "total_rows") (% "rows"))]}
  (let [str-json (fn-access loc db-config)]
    (read-json str-json false)))

(defn wrapper-get
  #^{:pre [(not (nil? id))]
     :post [(and (% "_id") (% "_rev"))]}
  [#^String id fn-access db-config]
  (let [json-data (fn-access id db-config)]
    (read-json json-data false)))

(defn 
  #^{:doc "determines if a give map is an initial revision based on the logic
exists key='_rev' and the rev is like '1_%w'"}
  initial-rev? [map]
  (let [regex #"^(\d)-.+"
	val (or (map "_rev") (map "rev"))]
    (if val
      (= "1" (get (re-find regex val) 1))
      false)))

(defn wrapper-create 
  (#^{:pre [(and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (not (nil? %)) (not (nil? (% "id"))) (not (nil? (% "rev"))))
	     (initial-rev? %)]}
   [map access-fn db-config]
     (let [json-in (json-str map)
	   json-out (access-fn json-in db-config)]
	 (read-json json-out false)))
  (#^{:pre [(not (nil? id))
	    (and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (% "id") (% "rev"))
	     (initial-rev? %)]}
   [id map access-fn db-config]
     (let [json-in (json-str map)
	   json-out (access-fn id json-in db-config)]
       (read-json json-out false))))

(defn 
  wrapper-update
  #^{:pre [(not (nil? (map-data "_id"))) (not (nil? (map-data "_rev")))
	   (or (= mode :append) (= mode :write))]
     :post [(and (% "id") (% "rev"))]}
  [map-data mode fn-get fn-put db-config]
  (if (= mode :write)
    (let [str-json (fn-put (map-data "_id") (json-str (dissoc map-data "_id")) db-config)]
      (read-json str-json false))
    (let [str-json-org (fn-get (map-data "_id") db-config)
	  map-merged (dissoc (merge (read-json str-json-org false) map-data) "_id")
	  str-json-out (fn-put (map-data "_id") (json-str map-merged) db-config)]
      (read-json str-json-out false))))

(defn 
  wrapper-delete
  #^{:pre [(not (nil? (map-data "_id"))) (not (nil? (map-data "_rev")))]
     :post (nil? %)}
  [map-data fn-del db-config]
  (read-json (fn-del (map-data "_id") (map-data "_rev") db-config) false))
