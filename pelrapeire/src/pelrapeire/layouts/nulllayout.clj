(ns pelrapeire.layouts.nulllayout
  (use hiccup.core))

(defn render [map-data]
  (html (:content map-data)))