(ns pelrapeire.controllers.index-ctrl)

(defn run [params]
  {:view :index
   :layout :minimallayout
   :params params})
