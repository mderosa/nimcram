(ns pelrapeire.layouts.json-layt
  (use clojure.contrib.json.write))

(defn render [map-data]
  (json-str (:content map-data)))
