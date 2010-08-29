(ns pelrapeire.layouts.json-layt
  (use clojure.contrib.json))

(defn render [map-data]
  (json-str (:content map-data)))
