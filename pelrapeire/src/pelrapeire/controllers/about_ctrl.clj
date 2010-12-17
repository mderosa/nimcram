(ns pelrapeire.controllers.about-ctrl)

(defn run [{params :params}]
  {:view :about
   :layout :minimallayout
   :params params})