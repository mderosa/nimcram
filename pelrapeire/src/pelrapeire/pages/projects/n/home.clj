(ns pelrapeire.pages.projects.n.home
  (:use pelrapeire.pages.tiles
	pelrapeire.app.taskstatistics))

(defn make-task [task] 
  [:table {:id (str (task "_id") "." (task "_rev")) :class "task"}
   [:tr
    [:td [:a {:href "#" :class "collapsible"} "+"]]
    [:td {:class "title"} (task "title")]
    (if (= "proposed" (task "progress"))
      (make-priority-display task)
      [:td {:class "statistic"} (days-in-progress task)])]])

(defn 
  #^{:doc "return a vector of the form [:td [:span] [:span]..]"}
  make-priority-display [task] 
  (let [trans (fn [x] (if (= 1 x) 
			[:img {:src "/img/star-on.gif" :class "clickable"}]
			[:img {:src "/img/star-off.gif" :class "clickable"}]))]
    (into [:td {:class "priority"}] (cond
		  (nil? (task "priority")) (map trans [0 0 0 ])
		  (= 1 (task "priority")) (map trans [1 0 0 ])
		  (= 2 (task "priority")) (map trans [1 1 0 ])
		  (= 3 (task "priority")) (map trans [1 1 1 ])
		  true (throw (IllegalArgumentException. "priority value is not valid"))))))

(defn make-tasks [tasks progress]
  {:pre [(contains? #{"proposed" "in-progress" "delivered"} progress)]}
  (let [tasks-subset (filter #(= progress (% "progress")) tasks)]
    (map make-task tasks-subset)))
   
(defn show [map-data]
  (let [js "/js/projects/n/home.js"
	css nil
	title "current project activity"
	content 
  [:table {:class "buckets"}
   [:tr
    [:td {:id "proposed" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "proposed"
      [:span
       [:a {:id "new" :href "#" :style "margin-left:20px"} "[new]"]]]
     (into [:div {:class "tasks"}] (make-tasks (:active map-data) "proposed"))]
    [:td {:id "in-progress" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "work in progress"]
     (into [:div {:class "tasks"}] (make-tasks (:active map-data) "in-progress"))]
    [:td {:id "delivered" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "delivered to user"]
     (into [:div {:class "tasks"}] (make-tasks (:completed map-data) "delivered"))
]]]]
    {:js js :css css :title title :content content :params (map-data :params)}))
     