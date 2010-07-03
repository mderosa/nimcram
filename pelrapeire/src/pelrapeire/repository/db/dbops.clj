(ns pelrapeire.repository.db.dbops
  (:use pelrapeire.repository.db.dbapi
	pelrapeire.repository.db.dbapiwrapper))

(defn op-get [#^String id cfg]
  (wrapper-get id get-doc cfg))

(defn op-create 
  ([map-data cfg]
     (wrapper-create map-data create-doc cfg))
  ([id map-data cfg]
     (wrapper-create id map-data create-named-doc cfg)))

(defn op-update [map-data mode cfg]
  (wrapper-update map-data mode overwrite-doc cfg))

(defn op-delete [map-data cfg]
  (wrapper-delete map-data delete-doc cfg))
