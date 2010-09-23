(ns pelrapeire.controllers.projects.uid.home-ctrl
  (:import org.joda.time.DateTime
	   org.joda.time.DateTimeZone))

(defn 
  #^{:doc "get proposed, work in progress, and tasks completed within the last 
two weeks. Send them back in a map structure to clients {:proposed (tasks) 
:wip (tasks) :completed (tasks)}"}
  run [fn-get-proposed-tasks fn-get-wip-tasks fn-get-completed-tasks params]
  {:pre [(not (nil? (params "project-name")))
	 (> (.length (.trim (params "project-name"))) 0)]
   :post [(not (nil? (:wip %)))
	  (not (nil? (:completed %)))]}
  (let [{project-name "project-name"} params
	date-later-than (. (. (DateTime. DateTimeZone/UTC) minusDays 14) withTime 0 0 0 0)
	proposed (fn-get-proposed-tasks project-name)
	wip (fn-get-wip-tasks project-name)
	completed (fn-get-completed-tasks project-name date-later-than)]
    {:proposed (map #(% "value") (proposed "rows"))
     :wip (map #(% "value") (wip "rows")) 
     :completed (map #(% "value") (completed "rows"))
     :view :projects.n.home
     :layout :projectlayout
     :params params}))
