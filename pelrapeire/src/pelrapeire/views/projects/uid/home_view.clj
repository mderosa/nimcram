(ns pelrapeire.views.projects.uid.home-view
  (:use pelrapeire.app.ui-control
	clojure.contrib.json
	pelrapeire.app.task-statistics))

(defn 
  #^{:doc "return a vector of the form [:td [:span] [:span]..]"}
  make-priority-display [task] 
  (let [trans (fn [x y] (if (= 1 x) 
			[:img {:src "/img/star-on.gif" :class "clickable" :title y}]
			[:img {:src "/img/star-off.gif" :class "clickable" :title y}]))
	priority-enum ["low" "medium" "high"]]
    (into [:td {:class "priority"}] (cond
		  (nil? (task "priority")) (map trans [0 0 0 ] priority-enum)
		  (= 1 (task "priority")) (map trans [1 0 0 ] priority-enum)
		  (= 2 (task "priority")) (map trans [1 1 0 ] priority-enum)
		  (= 3 (task "priority")) (map trans [1 1 1 ] priority-enum)
		  true (throw (IllegalArgumentException. "priority value is not valid"))))))

(defn 
  #^{:doc "we want a task to have a class type of 'task usr-func' or 'task n-user-func' this
function creates the classes for that designation"}
     make-task-class [task]
     (if (task "deliversUserFunctionality")
       "task usr-func"
       "task n-usr-func"))

(defn make-task [task] 
  [:table {:id (str (task "_id") "." (task "_rev")) :class (make-task-class task)}
   [:tr
    [:td {:class "rawData"} (json-str task) ]
    [:td [:a {:href "#" :class "collapsible"} "+"]]
    [:td {:class "title"} (task "title")]
    (if (= "proposed" (task "progress"))
      (make-priority-display task)
      [:td {:class "statistic"} (days-in-progress task)])]])

(defn make-tasks [tasks progress]
  {:pre [(contains? #{"proposed" "in-progress" "delivered"} progress)]}
  (let [tasks-subset (filter #(= progress (% "progress")) tasks)]
    (map make-task tasks-subset)))
   
(defn show [map-data]
  (let [js ["/js/projects/n/task.js" "/js/projects/n/home.js"]
	css nil
	title "current project activity"
	content 
  [:table {:class "buckets"}
   [:tr
    [:td {:id "proposed" :class "bucket"}
     (rounded-corner-crown)
     [:div {:class "bmrcp-head"} "proposed"
      [:span
       [:a {:id "new" :href "#" :style "margin-left:20px"} "[new]"]]]
     (into [:div {:class "tasks"}] (make-tasks (:proposed map-data) "proposed"))]
    [:td {:id "in-progress" :class "bucket"}
     (rounded-corner-crown)
     (rounded-corner-base "work in progress")
     (into [:div {:class "tasks"}] (make-tasks (:wip map-data) "in-progress"))]
    [:td {:id "delivered" :class "bucket"}
     (rounded-corner-crown)
     (rounded-corner-base "delivered to user")
     (into [:div {:class "tasks"}] (make-tasks (:completed map-data) "delivered"))]]]]
    {:js js :css css :title title :content content :params (map-data :params)}))
     
