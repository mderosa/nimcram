(ns pelrapeire.views.json-view)

(defn show [map-data]
    {:js nil :css nil :title nil :content (:content map-data)})
