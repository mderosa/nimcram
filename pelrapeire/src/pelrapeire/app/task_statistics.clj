(ns pelrapeire.app.task-statistics
  (:use pelrapeire.app.convert)
  (:import org.joda.time.DateTime 
	   org.joda.time.DateTimeZone
	   org.joda.time.Days))

(defn days-in-progress [map-data]
  {:pre [(or (= "proposed" (map-data "progress"))
	    (= "in-progress" (map-data "progress"))
	    (= "delivered" (map-data "progress")))]
   :post [(>= % 0)]}
  (let [start (if (map-data "taskStartDate")
		(vector-to-datetime (map-data "taskStartDate")) nil)
	end (if (map-data "taskCompleteDate")
	      (vector-to-datetime (map-data "taskCompleteDate")) nil)]
    (condp = (map-data "progress")
      "proposed" 0
      "in-progress" (do
		      (assert start)
		      (. (Days/daysBetween start (DateTime. DateTimeZone/UTC)) getDays))
      "delivered" (do
		    (assert start)
		    (assert end)
		    (. (Days/daysBetween start end) getDays)))))
