(ns pelrapeire.pages.projects.n.home
  (:use pelrapeire.tiles))

(def arrows
 {:north "&#8679;" 
  :north-east "&#11008;"
  :east "&#8680"
  :south-east "&#11010;"
  :south "&#8681;"
  :south-west "&#11011;"
  :west "&#8678;"
  :north-west "&#11009;"})

(defn make-task [progress title days] 
     [:table {:class "task"}
      [:tr
       [:td [:a {:href "#" :class "collapsible"} "+"]]
       [:td {:class "title"} title]
       [:td {:class "statistic"} (arrows progress)]
       [:td {:class "statistic"} (str days)]]])

(defn show-page []
  [:table {:class "buckets"}
   [:tr
    [:td {:id "backburner" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "waiting to start"
      [:span
       [:a {:href "#" :style "margin-left:20px"} "[new]"]]]
     [:div {:class "tasks"}
      [:table {:class "task"} [:tr [:td]]]
      (make-task :north-east "another task that has a really really really long title that takes up a lot of space and may push stuff out of the way" 0)
      (make-task :north-east "some task here" 0)
      (make-task :north-east "some task here2" 0)
]]
    [:td {:id "active" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "work in progress"]
     [:div {:class "tasks"}]]
    [:td {:id "live" :class "bucket"}
     [:div {:class "bmrcp-n"}
      [:div {:class "bmrcp-e"}
       [:div {:class "bmrcp-w"}]]]
     [:div {:class "bmrcp-head"} "delivered to user"]
     [:div {:class "tasks"}]]]])
     