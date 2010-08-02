(ns pelrapeire.app.taskstatistics
  (:use pelrapeire.app.convert)
  (:import org.joda.time.DateTime 
	   org.joda.time.Days))

(defn days-in-progress [map-data]
  {:pre [(or (= "proposed" (map-data "progress"))
	    (= "in-progress" (map-data "progress"))
	    (= "delivered" (map-data "progress")))]
   :post [(>= % 0)]}
  (let [start (if (map-data "task-start-date")
		(vector-to-datetime (map-data "task-start-date")) nil)
	end (if (map-data "task-complete-date")
	      (vector-to-datetime (map-data "task-complete-date")) nil)]
    (condp = (map-data "progress")
      "proposed" 0
      "in-progress" (do
		      (assert start)
		      (. (Days/daysBetween start (DateTime.)) getDays))
      "delivered" (do
		    (assert start)
		    (assert end)
		    (. (Days/daysBetween start end) getDays)))))