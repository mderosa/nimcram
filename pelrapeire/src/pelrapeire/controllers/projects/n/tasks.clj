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

(defn 
  #^{:doc "pulls out just the _id _rev and progress parameters and then adds a 
'task-start-date' parameter."}
  run-update-progress [fn-update params])

(defn run [fn-create-task fn-update-task fn-get-task params]
  (cond
   (nil? (params "action"))
   (let [created-info (run-create-task fn-create-task params)
	 created-data (fn-get-task (created-info "id"))]
     {:view :null
      :layout :nulllayout
      :content created-data})
   (= "update-progress" (params "action"))
   (let [updated-resp (fn-update-task (select-keys params ["_id" "_rev" "progress"]) :append)]
     {:view :null
      :layout :nulllayout
      :content updated-resp})))

