(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl
  (:use pelrapeire.app.validators
	pelrapeire.app.specification.task
	clojure.contrib.trace
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
'taskStartDate' parameter or a taskCompleteDate.  This function just updates the progress
parameter for all other updates we should go through (run-update-all)"}
  run-update-progress [fn-update {progress "progress"  originalProgress "originalProgress" :as params}]
  {:pre [(params "_id") (revision? (params "_rev")) 
	 (progress? progress)
	 (progress? originalProgress)]}
  (let [extract (select-keys params ["_id" "_rev" "progress"])
	start-dt (datetime-to-vector (DateTime. DateTimeZone/UTC))
	complete-dt (datetime-to-vector (DateTime. DateTimeZone/UTC))
	augmented (cond
		    (and (= originalProgress "proposed") (= progress "in-progress"))
		    (assoc extract "taskStartDate" start-dt)

		    (and (= originalProgress "proposed") (= progress "delivered"))
		    (assoc extract "taskStartDate" start-dt "taskCompleteDate" complete-dt)

		    (and (= originalProgress "in-progress") (= progress "delivered"))
		    (assoc extract "taskCompleteDate" complete-dt)

		    (and (= originalProgress "in-progress") (= progress "proposed"))
		    nil
		    
		    (= originalProgress "delivered")
		    nil)]
    (if augmented (fn-update augmented :append) nil)))

(defn
  #^{:doc "take object as supplied by the front end and append it.  This function will update all parameters except
for the 'progress', which need special processing logic"}
  run-update-all [fn-update params]
  (let [conditioned-data (condition-task (dissoc params "progress"))]
    (fn-update conditioned-data :append)))

(defn 
  #^{:doc "handles a post request and returns a full task object"}
  run-post [fn-get fn-update params] 
  (cond
   (nil? (params "action"))
   (let [ok-err-resp (run-update-all fn-update params)
	 task (fn-get (params "task-uid"))]
     {:view :json-view
      :layout :json-layout
      :content task})
   (= "update-progress" (params "action"))
   (let [ok-err-resp (run-update-progress fn-update params)
	 task (fn-get (params "task-uid"))]
     {:view :json-view
      :layout :json-layout
      :content task})

   true (throw (IllegalArgumentException. "request not properly specified"))))

(defn run-delete [fn-get fn-update fn-delete {params :params :as req}]
  (let [task (fn-get (params "task-uid"))
	ok-err-resp (if (= "proposed" (task "progress"))
		      (fn-delete task)
		      (fn-update 
		       (assoc task "taskTerminateDate" 
			      (datetime-to-vector (DateTime. DateTimeZone/UTC)))
			      :append))]
    {:view :json-view
     :layout :json-layout
     :content ""}))

(defn run [fn-get fn-update fn-delete req]
  (condp = (:request-method req)
    :get (run-get (:params req))
    :post (run-post fn-get fn-update (:params req))
    :delete (trace(run-delete fn-get fn-update fn-delete req))))