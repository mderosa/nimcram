(ns pelrapeire.controllers.projects.uid.tasks-ctrl
  (:use pelrapeire.app.specification.task))

(defn 
  #^{:doc "does initial task creation returning an object like
{'ok' true 'id' xxx 'rev' yyy}"}
  run-create-task [fn-create-task params]
  {:pre [(not (nil? (params "title")))]}
  (let [new-task (create-task params)]
	(fn-create-task new-task)))

(defn run [fn-create-task fn-update-task fn-get-task params]
   (let [created-info (run-create-task fn-create-task params)
	 created-data (fn-get-task (created-info "id"))]
     {:view :json-view
      :layout :json-layout
      :content created-data}))
