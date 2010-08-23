(ns migrator.do.00005
  #^{:doc "change task-complete-date to camel case"}
  (:use pelrapeire.repository.dbpelrapeire
	pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops))

(defn query-all-tasks []
  (let [loc "_design/picominmin/_view/project-tasks"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn add-create-date [task]
  (if (nil? (task "taskCreateDate"))
      (assoc task "taskCreateDate" [2010 8 17 0 0 0])
      task))

(defn add-task-attribute [task]
  (if (nil? (task "taskCreateDate"))
    (pel-update (add-create-date task) :write)))

(defn migrate []
  (doseq [t (query-all-tasks)]
    (println 
     (add-task-attribute t))))