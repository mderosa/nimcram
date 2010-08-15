(ns pelrapeire.views.redirect-view
  (:use ring.util.response))

(defn show [map-data]
  (redirect (:url map-data)))
