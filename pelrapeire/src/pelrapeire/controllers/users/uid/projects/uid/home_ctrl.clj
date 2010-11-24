(ns pelrapeire.controllers.users.uid.projects.uid.home-ctrl
  (:use pelrapeire.app.convert
	pelrapeire.app.statistics
	clojure.contrib.trace)
  (:import org.joda.time.Days))

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
  calculate-days-in-process [kv]
    {:pre [(kv "taskStartDate")  (or (kv "taskCompleteDate") (kv "taskTerminateDate"))]
     :post [(>= % 0)]}
    (let [startDt (vector-to-datetime (kv "taskStartDate"))
	  endDt (vector-to-datetime
		 (if (kv "taskTerminateDate") 
		   (kv "taskTerminateDate")
		   (kv "taskCompleteDate")))
	  multiplier (if (kv "taskTerminateDate") 2 1)]
      (* multiplier (.getDays (Days/daysBetween startDt endDt)))))

(defn calculate-subgroup-averages [ls]
  (map #(average %) ls))

(defn calculate-subgroup-std-deviations [ls]
  (map #(std-deviation %) ls))

(defn 
  xbar-ucl [xbars ss group-size]
  {:pre [(= (count xbars) (count ss))]}
  (if (= 0 (count xbars))
    nil
    (let [a (* 3 (average ss))
	  b (* (c4-factor group-size) (Math/sqrt group-size))]
      (+ (average xbars) (/ a b)))))

(defn 
  xbar-lcl [xbars ss group-size]
  {:pre [(= (count xbars) (count ss))]}
  (if (= 0 (count xbars))
    nil
    (let [a (* 3 (average ss))
	  b (* (c4-factor group-size) (Math/sqrt group-size))]
      (- (average xbars) (/ a b)))))

(defn 
  ^{:doc "Returns a data structure which contains data which can be used to build an x (and r control chart
eventually). the returned data structure is of the form 
{:average x :ucl su :lcl sl :subgroups [[a b c]...] :subgroup-avgs [a b c]}"}
  run [{:keys [fn-get fn-update]} fn-n-recent-delivered-tasks {params :params :as req}]
(let [recently-delivered-raw (reverse (map #(% "value") ((fn-n-recent-delivered-tasks (params "project-uid") 300) "rows")))
      recently-delivered-days (map #(calculate-days-in-process %) recently-delivered-raw)
      days-subgroups (make-rational-subgroups recently-delivered-days 3)
      xs (calculate-subgroup-averages days-subgroups)
      ss (calculate-subgroup-std-deviations days-subgroups)]
  {:view :users.uid.projects.uid.home
   :layout :projectlayout
   :object {:xbarbar (average xs)
	    :xbarucl (xbar-ucl xs ss 3)
	    :xbarlcl (xbar-lcl xs ss 3)
	    :subgroups days-subgroups
	    :subgroup-avgs xs
	    }}))
