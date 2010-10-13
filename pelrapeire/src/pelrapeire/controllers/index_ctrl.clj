(ns pelrapeire.controllers.index-ctrl)

(defn run [{params :params}]
  {:view :index
   :layout :minimallayout
   :params params})
