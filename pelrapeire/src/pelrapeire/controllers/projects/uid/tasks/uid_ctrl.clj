(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl)

(defn 
  #^{:doc "this function expects a task GET and returns a full task
object"}
  run [fn-get params]
  (let [task (fn-get (params "task-uid"))]
  {:view :json-view
   :layout :json-layout
   :content task}))
