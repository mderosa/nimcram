(ns 
    #^{:doc "this namespace contains generic database access functions. the functions
should take just data and configuration infomation passed in from the user"}
  pelrapeire.repository.db.dbops
  (:use pelrapeire.repository.db.dbapi
	pelrapeire.repository.db.dbapiwrapper))

(defn 
  #^{:doc "this function is used for calling views the usage is like:
(op-get-view '_design/picominmin/_view/project-tasks?key=%22PicoMinMin%22' db-config). the
return information is a result set as well as sumary statistics"}
 op-get-view [#^String loc cfg]
  (wrapper-view loc get-doc cfg))

(defn op-get [#^String id cfg]
  (wrapper-get id get-doc cfg))

(defn op-create 
  ([map-data cfg]
     (wrapper-create map-data create-doc cfg))
  ([id map-data cfg]
     (wrapper-create id map-data create-named-doc cfg)))

(defn op-update [map-data mode cfg]
  (wrapper-update map-data mode get-doc overwrite-doc cfg))

(defn op-delete [map-data cfg]
  (wrapper-delete map-data delete-doc cfg))
