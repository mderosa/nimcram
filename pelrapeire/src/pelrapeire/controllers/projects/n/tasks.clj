(ns pelrapeire.controllers.projects.n.tasks)

(defn run [fn-create-task params]
  {:pre []}
  (let [submit-data (assoc params "type" "task" "progress" "proposed")
	conditioned-data (if (= "true" (submit-data "delivers-user-functionality"))
			   (assoc submit-data "delivers-user-functionality" true)
			   (assoc submit-data "delivers-user-functionality" false))
	created-data (fn-create-task conditioned-data)]
    {:view :null
     :layout :nulllayout
     :content created-data}))