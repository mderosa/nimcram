(ns pelrapeire.controllers.projects.uid.home-ctrl
  (:import org.joda.time.DateTime
	   org.joda.time.DateTimeZone))

(defn 
  #^{:doc "get proposed, work in progress, and tasks completed within the last 
two weeks. Send them back in a map structure to clients {:proposed (tasks) 
:wip (tasks) :completed (tasks)}"}
  run [fn-get-proposed-tasks fn-get-wip-tasks fn-get-delivered-tasks params]
  {:pre [(not (nil? (params "project-uid")))
	 (> (.length (.trim (params "project-uid"))) 0)]
   :post [(not (nil? (:wip %)))
	  (not (nil? (:completed %)))]}
  (let [{project-uid "project-uid"} params
	date-later-than (. (. (DateTime. DateTimeZone/UTC) minusDays 14) withTime 0 0 0 0)
	proposed (fn-get-proposed-tasks project-uid)
	wip (fn-get-wip-tasks project-uid)
	completed (fn-get-delivered-tasks project-uid date-later-than)]
    {:proposed (map #(% "value") (proposed "rows"))
     :wip (map #(% "value") (wip "rows")) 
     :completed (map #(% "value") (completed "rows"))
     :view :projects.n.home
     :layout :projectlayout
     :params params}))
