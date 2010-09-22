(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl
  (:use pelrapeire.app.validators
	pelrapeire.app.specification.task
	pelrapeire.app.convert)
  (:import org.joda.time.DateTime
	   org.joda.time.DateTimeZone))

(defn 
  #^{:doc "this function expects a task GET and returns a full task
object"}
  run-get [fn-get params]
  (let [task (fn-get (params "task-uid"))]
  {:view :json-view
   :layout :json-layout
   :content task}))

(defn 
  #^{:doc "pulls out '_id', '_rev', progress parameters and then adds a 
'taskStartDate' parameter or a taskCompleteDate"}
  run-update-progress [fn-update params]
  {:pre [(params "_id") (revision? (params "_rev")) (#{"in-progress" "delivered"} (params "progress"))]}
  (let [extract (select-keys params ["_id" "_rev" "progress"])
	augmented (condp = (params "progress")
		    "in-progress" (assoc extract "taskStartDate" 
					 (datetime-to-vector (DateTime. DateTimeZone/UTC)))
		    "delivered" (assoc extract "taskCompleteDate" 
				       (datetime-to-vector (DateTime. DateTimeZone/UTC))))]
    (fn-update augmented :append)))

(defn 
  #^{:doc "pulls out '_id', '_rev', priority parameters"}
  run-update-priority [fn-update params]
  {:pre [(params "_id") (revision? (params "_rev")) (#{"1" "2" "3"} (params "priority"))]}
  (let [extract (assoc 
		    (select-keys params ["_id" "_rev"]) 
		  "priority" (Integer/parseInt (params "priority")))]
    (fn-update extract :append)))

(defn
  #^{:doc "take object as supplied by the front end and append it"}
  run-update-all [fn-update params]
  (let [clean-params (dissoc params "project-uid" "task-uid")
	add-proj-params (assoc clean-params "project" (params "project-uid"))]
  (fn-update (condition-task add-proj-params) :append)))

(defn 
  #^{:doc "handles a post request and returns a full task object"}
  run-post [fn-update-task params] 
  (cond
   (nil? (params "action"))
   (let [updated-resp (run-update-all fn-update-task params)]
     {:view :json-view
      :layout :json-layout
      :content updated-resp})     
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

(defn run [fn-get fn-update req]
  (condp = (:request-method req)
    :get (run-get (:params req))
    :post (run-post fn-update (:params req))))