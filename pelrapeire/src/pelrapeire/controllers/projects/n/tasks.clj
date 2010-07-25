(ns pelrapeire.controllers.projects.n.tasks)

(defn 
  #^{:doc "does initial task creation returning an object like
{'ok' true 'id' xxx 'rev' yyy}"}
  run-create-task [fn-create-task params]
  {:pre [(not (nil? (params "title")))]}
  (let [submit-data (assoc params "type" "task" "progress" "proposed")
	conditioned-data (if (= "true" (submit-data "delivers-user-functionality"))
			   (assoc submit-data "delivers-user-functionality" true)
			   (assoc submit-data "delivers-user-functionality" false))]
	(fn-create-task conditioned-data)))

(defn run [fn-create-task fn-get-task params]
  (let [created-info (run-create-task fn-create-task params)
	created-data (fn-get-task (created-info "id"))]
    {:view :null
     :layout :nulllayout
     :content created-data}))

