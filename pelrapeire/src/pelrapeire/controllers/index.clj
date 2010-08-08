(ns pelrapeire.controllers.index)

(defn run [params]
  {:view :index
   :layout :minimallayout
   :params params})