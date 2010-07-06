(ns 
    #^{:doc "this is the interface to the pelrapeire database"}
  pelrapeire.repository.dbpelrapeire
  (:use pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops))

(defn pel-get [id]
  (op-get id db-config))

(defn pel-create 
  ([map-data]
     (op-create map-data db-config))
  ([id map-data]
     (op-create id map-data db-config)))

(defn pel-update [map-data mode]
  (op-update map-data mode db-config))

(defn pel-delete [map-data]
  (op-delete map-data db-config))
    

;;curl http://localhost:5984/picominmin/_design/picominmin/_view/project-tasks?key=%22PicoMinMin%22