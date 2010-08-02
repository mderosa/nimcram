(ns pelrapeire.controllers.projects.n.tasks
  (:use clojure.contrib.trace
	pelrapeire.app.convert)
  (:import org.joda.time.DateTime))

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
  #^{:doc "pulls out '_id', '_rev', progress parameters and then adds a 
'task-start-date' parameter or a task-complete-date"}
  run-update-progress [fn-update params]
  {:pre [(params "_id") (params "_rev") (#{"in-progress" "delivered"} (params "progress"))]}
  (let [extract (select-keys params ["_id" "_rev" "progress"])
	augmented (condp = (params "progress")
		    "in-progress" (assoc extract "task-start-date" 
					 (datetime-to-vector (DateTime.)))
		    "delivered" (assoc extract "task-complete-date" 
				       (datetime-to-vector (DateTime.))))]
    (fn-update augmented :append)))

(defn 
  #^{:doc "pulls out '_id', '_rev', priority parameters"}
  run-update-priority [fn-update params]
  {:pre [(params "_id") (params "_rev") (#{"1" "2" "3"} (params "priority"))]}
  (let [extract (assoc 
		    (select-keys params ["_id" "_rev"]) 
		  "priority" (Integer/parseInt (params "priority")))]
    (fn-update extract :append)))


(defn run [fn-create-task fn-update-task fn-get-task params]
  (cond
   (nil? (params "action"))
   (let [created-info (run-create-task fn-create-task params)
	 created-data (fn-get-task (created-info "id"))]
     {:view :null
      :layout :nulllayout
      :content created-data})

   (= "update-progress" (params "action"))
   (let [updated-resp (run-update-progress fn-update-task params)]
     {:view :null
      :layout :nulllayout
      :content updated-resp})

   (= "update-priority" (params "action"))
   (let [updated-resp (run-update-priority fn-update-task params)]
     {:view :null
      :layout :nulllayout
      :content updated-resp})
   true (throw (IllegalArgumentException. "request not properly specified"))))

