(ns pelrapeire.controllers.users.uid.projects.uid.home-ctrl
  (:use pelrapeire.app.convert
	pelrapeire.app.statistics.functions
	pelrapeire.app.statistics.xbarchart
	clojure.contrib.trace)
  (:import org.joda.time.Hours))

;; the size of our graph is 934px 276 -> grouped in sets of three -> 92 points which will fit into
;; the graph with a 10px spacing and a 10px offset
(def max-data-points 276)

(defn 
  ^{:doc "takes an object which have a taskStartDate and a taskCompleteDate and maybe a taskTerminateDate and
calculates the time in process.  Tasks that have a terminate data are docked twice time"}
  calculate-hours-in-process [kv]
    {:pre [(kv "taskStartDate")  (or (kv "taskCompleteDate") (kv "taskTerminateDate"))]
     :post [(>= % 0)]}
    (let [startDt (vector-to-datetime (kv "taskStartDate"))
	  endDt (vector-to-datetime
		 (if (kv "taskTerminateDate") 
		   (kv "taskTerminateDate")
		   (kv "taskCompleteDate")))
	  multiplier (if (kv "taskTerminateDate") 2 1)]
      (* multiplier (.getHours (Hours/hoursBetween startDt endDt)))))

(defn 
  ^{:doc "Returns a data structure which contains data which can be used to build an x (and r control chart
eventually). the returned data structure is of the form 
{:average x :ucl su :lcl sl :subgroups [[a b c]...] :subgroup-avgs [a b c]}"}
  run [{:keys [fn-get fn-update]} fn-n-recent-delivered-tasks {params :params :as req}]
(let [recently-delivered-raw (reverse 
			      (map #(% "value") 
				   ((fn-n-recent-delivered-tasks (params "project-uid") max-data-points) "rows")))
      recently-delivered-hours (map #(calculate-hours-in-process %) recently-delivered-raw)
      hours-subgroups (make-rational-subgroups recently-delivered-hours 3)
      xs (calculate-subgroup-averages hours-subgroups)
      ss (calculate-subgroup-std-deviations hours-subgroups)]
  {:view :users.uid.projects.uid.home
   :layout :projectlayout
   :collection hours-subgroups}))
