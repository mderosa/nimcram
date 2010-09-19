(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl)

(defn 
  #^{:doc "this function expects a task GET and returns a full task
object"}
  run-get [fn-get params]
  (let [task (fn-get (params "task-uid"))]
  {:view :json-view
   :layout :json-layout
   :content task}))

(defn 
  #^{:doc "handles a post request and returns a full task object"}
  run-post [fn-post params] )

(defn run [fn-get fn-update req]
  (condp = (:request-method req)
    :get (run-get (:params req))
    :post (run-post fn-update (:params req))))