(ns pelrapeire.controllers.index)

(defn run [params]
  {:view :index
   :layout :projectlayout
   :params params})