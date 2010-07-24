(ns pelrapeire.layouts.nulllayout
  (use hiccup.core))

(defn render [map-data]
  (:content map-data))