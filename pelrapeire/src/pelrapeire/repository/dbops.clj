
(ns 
    #^{:doc "this is a high level api that insulates clojure from
having to interact with couch db through strings"}
  pelrapeire.repository.dbops
  (:use pelrapeire.repository.dbapi))

(defn repo-get
  #^{:pre [(not (nil? id))]
     :post [(and (% "_id") (% "_rev"))]}
  [#^String id])

(defn repo-create 
  (#^{:pre [(and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (% "_id") (% "_rev"))]}
   [map])
  (#^{:pre [(not (nil? id))
	    (and (nil? (map "_id")) (nil? (map "_rev")))]
      :post [(and (% "_id") (% "_rev"))]}
   [id map]))

(defn 
  repo-update
  #^{:pre [(not (nil? (map "_id"))) (not (nil? (map "_rev")))
	   (or (= mode :append) (= mode :write))]
     :post [(and (% "_id") (% "_rev"))]}
  [map mode])

(defn 
  repo-delete
  #^{:pre [(not (nil? (map "_id"))) (not (nil? (map "_rev")))]
     :post (nil? %)}
  [#^String map])