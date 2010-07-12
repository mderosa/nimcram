
(ns pelrapeire.controllers.projects.n.home
  (:import org.joda.time.DateTime))

(defn 
  #^{:doc "get active-tasks + tasks completed within the last two weeks and send
them back in a map structure to clients {:active (tasks) :completed (tasks)}"}
  run [fn-get-active-tasks fn-get-completed-tasks #^String project-name]
  {:pre [(not (nil? project-name)) (> (.length (.trim project-name)) 0)]
   :post [(not (nil? (:active %))) (not (nil? (:completed %)))]}
  (let [date-later-than (. (. (DateTime.) minusDays 14) withTime 0 0 0 0)
	active (fn-get-active-tasks project-name)
	completed (fn-get-completed-tasks project-name date-later-than)]
    {:active (map #(% "value") (active "rows")) 
     :completed (map #(% "value") (completed "rows"))
     :view :projects.n.home
     :layout :projectlayout}))