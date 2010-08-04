(ns pelrapeire.app.uicontrol)

(defn rounded-corner-crown []
  [:div {:class "bmrcp-n"}
    [:div {:class "bmrcp-e"}
     [:div {:class "bmrcp-w"}]]])
  
(defn rounded-corner-base [header-title]
  [:div {:class "bmrcp-head"} header-title])
