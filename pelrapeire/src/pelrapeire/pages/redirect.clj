(ns pelrapeire.pages.redirect
  (:use ring.util.response))

(defn show [map-data]
  (redirect (:url map-data)))