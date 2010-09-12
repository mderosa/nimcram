(ns pelrapeire.app.ui-control)

(defn rounded-corner-crown []
  [:div {:class "bmrcp-n"}
    [:div {:class "bmrcp-e"}
     [:div {:class "bmrcp-w"}]]])
  
(defn rounded-corner-base [header-title]
  [:div {:class "bmrcp-head"} header-title])

(defn accumulate [base errors]
  (if (or (nil? errors) (empty? errors))
    base
    (accumulate (conj base [:li (first errors)]) (rest errors))))

(defn error-list [map-data]
  (let [base [:ul {:class "errors"}]]
    (accumulate base (:errors map-data))))

(defn include-js [js]
  (for [j js]
    [:script {:type "text/javascript", :src j}]))

