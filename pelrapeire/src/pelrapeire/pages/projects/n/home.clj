(ns pelrapeire.pages.projects.n.home
  (:use pelrapeire.pages.tiles
	pelrapeire.app.taskstatistics))

(def arrows
 {:north "&#8679;" 
  :north-east "&#11008;"
  :east "&#8680"
  :south-east "&#11010;"
  :south "&#8681;"
  :south-west "&#11011;"
  :west "&#8678;"
  :north-west "&#11009;"})

(defn make-task [task] 
  [:table {:class "task"}
   [:tr
    [:td [:a {:href "#" :class "collapsible"} "+"]]
    [:td {:class "title"} (task "title")]
    [:td {:class "statistic"} (days-in-progress task)]]])

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
     