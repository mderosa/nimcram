(ns migrator.do.00001
  (:use pelrapeire.repository.dbpelrapeire
	pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops))

(defn query-all-tasks []
  (let [loc "_design/picominmin/_view/project-tasks"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn add-default-namespace [task]
  (if (nil? (task "namespace"))
    (pel-update (assoc task "namespace" []) :write)))

(defn migrate []
  (doseq [t (query-all-tasks)]
    (println (add-default-namespace t))))

