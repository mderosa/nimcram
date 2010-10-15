(ns migrator.do.00007
 ^{:doc "add a 'contributors' and 'followers' attribute to all projects"}
 (:use pelrapeire.repository.dbpelrapeire
       pelrapeire.repository.dbconfig
       pelrapeire.repository.db.dbops))

(defn query-all-projects []
  (let [loc "_design/picominmin/_view/projects"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn augment-doc [doc]
  (assoc doc "contributors" [] "followers" []))

(defn migrate []
  (doseq [doc (query-all-projects)]
    (println (pel-update (augment-doc doc) :write))))