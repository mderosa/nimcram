(ns pelrapeire.controllers.users.uid.projects.uid.home-ctrl
  (:use pelrapeire.app.convert
	pelrapeire.app.statistics
	clojure.contrib.trace)
  (:import org.joda.time.Days))

(defn 
  ^{:doc "Returns a data structure which contains data which can be used to build an x (and r control chart
eventually). the returned data structure is of the form 
{:average x :ucl su :lcl sl :rationalsubgroups [[a b c]...] :subgroupaverage [a b c]}"}
  run [{:keys [fn-get fn-update]} fn-n-recent-delivered-tasks {params :params :as req}]
  {:view :users.uid.projects.uid.home
   :layout :projectlayout
   :object nil})

(defn 
  ^{:doc "takes a sequence of the form (a b c d e f) and starting at the beginning returns every nth element.
For example (a d)"} 
  take-first-of-each-n [ls n]
  (let [frst (first ls)]
    (if (nil? frst) 
     '()
     (cons (first ls) (take-first-of-each-n (drop n ls) n)))))

(defn 
  ^{:doc "takes a seqence of elements and the elements in vectors of size = group-size.  Any elements at the 
end of the list that are insufficient to make a full group are dropped"}
  make-rational-subgroups [ls group-size]
  (let [subgroup (map (fn [a b c] [a b c]) ls (drop 1 ls) (drop 2 ls))]
    (take-first-of-each-n subgroup group-size)))

(defn 
  ^{:doc "takes an object which have a taskStartDate and a taskCompleteDate and maybe a taskTerminateDate and
calculates the time in process.  Tasks that have a terminate data are docked twice time"}
  calculate-days-in-process [map]
    {:pre [(map "taskStartDate")  (or (map "taskCompleteDate") (map "taskTerminateDate"))]
     :post [(>= % 0)]}
    (let [startDt (vector-to-datetime (map "taskStartDate"))
	  endDt (vector-to-datetime
		 (if (map "taskTerminateDate") 
		   (map "taskTerminateDate")
		   (map "taskCompleteDate")))
	  multiplier (if (map "taskTerminateDate") 2 1)]
      (* multiplier (.getDays (Days/daysBetween startDt endDt)))))

(defn calculate-subgroup-averages [ls]
  (map (fn [v] (/ (apply + v) (count v))) ls))

(defn 
  ^{:doc "takes a sequence of the form ([a b c] [d e f]...) and returns an estimate of the subgroup standard
deviation"}
  estimate-subgroup-std-deviation [ls])