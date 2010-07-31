(ns pelrapeire.app.convert
    (:import org.joda.time.DateTime))

(defn vector-to-datetime [ns]
  (DateTime. (get ns 0) (get ns 1) (get ns 2)
	     (get ns 3) (get ns 4) (get ns 5) 0))

(defn datetime-to-vector [dt]
  [(.. dt year get) (.. dt monthOfYear get) (.. dt dayOfMonth get)
   (.. dt hourOfDay get) (.. dt minuteOfHour get) (.. dt secondOfMinute get)])
  