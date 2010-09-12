(ns migrator.do.00003
  #^{:doc "change task-start-date to camel case"}
  (:use pelrapeire.repository.dbpelrapeire
	pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops))

(defn query-all-tasks []
  (let [loc "_design/picominmin/_view/project-tasks"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn start-date-to-camel-case [task]
  (if (nil? (task "taskStartDate"))
    (let [start-date (task "task-start-date")]
      (assoc (dissoc task "task-start-date")
	"taskStartDate" start-date))))

(defn change-task-attribute [task]
  (if (nil? (task "taskStartDate"))
    (pel-update (start-date-to-camel-case task) :write)))

(defn migrate []
  (doseq [t (query-all-tasks)]
    (println 
     (change-task-attribute t))))

