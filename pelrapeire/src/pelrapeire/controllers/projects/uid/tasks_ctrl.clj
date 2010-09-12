(ns pelrapeire.controllers.projects.uid.tasks-ctrl
  (:use clojure.contrib.trace
	pelrapeire.app.convert
	pelrapeire.app.validators
	pelrapeire.app.specification.task)
  (:import org.joda.time.DateTime))

(defn 
  #^{:doc "does initial task creation returning an object like
{'ok' true 'id' xxx 'rev' yyy}"}
  run-create-task [fn-create-task params]
  {:pre [(not (nil? (params "title")))]}
  (let [new-task (create-task params)]
	(fn-create-task new-task)))

(defn 
  #^{:doc "pulls out '_id', '_rev', progress parameters and then adds a 
'taskStartDate' parameter or a taskCompleteDate"}
  run-update-progress [fn-update params]
  {:pre [(params "_id") (revision? (params "_rev")) (#{"in-progress" "delivered"} (params "progress"))]}
  (let [extract (select-keys params ["_id" "_rev" "progress"])
	augmented (condp = (params "progress")
		    "in-progress" (assoc extract "taskStartDate" 
					 (datetime-to-vector (DateTime.)))
		    "delivered" (assoc extract "taskCompleteDate" 
				       (datetime-to-vector (DateTime.))))]
    (fn-update augmented :append)))

(defn 
  #^{:doc "pulls out '_id', '_rev', priority parameters"}
  run-update-priority [fn-update params]
  {:pre [(params "_id") (revision? (params "_rev")) (#{"1" "2" "3"} (params "priority"))]}
  (let [extract (assoc 
		    (select-keys params ["_id" "_rev"]) 
		  "priority" (Integer/parseInt (params "priority")))]
    (fn-update extract :append)))


(defn run [fn-create-task fn-update-task fn-get-task params]
  (cond
   (nil? (params "action"))
   (let [created-info (run-create-task fn-create-task params)
	 created-data (fn-get-task (created-info "id"))]
     {:view :json-view
      :layout :json-layout
      :content created-data})

   (= "update-progress" (params "action"))
   (let [updated-resp (run-update-progress fn-update-task params)]
     {:view :json-view
      :layout :json-layout
      :content updated-resp})

   (= "update-priority" (params "action"))
   (let [updated-resp (run-update-priority fn-update-task params)]
     {:view :json-view
      :layout :json-layout
      :content updated-resp})
   true (throw (IllegalArgumentException. "request not properly specified"))))

