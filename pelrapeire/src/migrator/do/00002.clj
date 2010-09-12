(ns 
    #^{:doc "change 'delivers-user-functionality' to camel case'"}
  migrator.do.00002
  (:use pelrapeire.repository.dbpelrapeire
	pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops))

(defn query-all-tasks []
  (let [loc "_design/picominmin/_view/project-tasks"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn change-user-func-to-camel [task]
  (if (nil? (task "deliversUserFunctionality"))
    (let [user-fnc (task "delivers-user-functionality")]
      (assoc (dissoc task "delivers-user-functionality")
	"deliversUserFunctionality" user-fnc))))

(defn change-task-attribute [task]
  (if (nil? (task "deliversUserFunctionality"))
    (pel-update (change-user-func-to-camel task) :write)))

(defn migrate []
  (doseq [t (query-all-tasks)]
    (println 
     (change-task-attribute t))))

