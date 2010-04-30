(ns pelrapeire.pages.project.activity
  (:use pelrapeire.tiles))

(defn show-current-activity []
  [:table 
   [:tr
    [:td {:class "icebox"}]
    [:td {:class "active"}]
    [:td {:class "live"}]]])
     