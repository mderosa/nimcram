(ns pelrapeire.controllers.users.uid.projects.uid.home-ctrl)

(defn run [{:keys [fn-get fn-update]} {params :params :as req}]
  {:view :users.uid.projects.uid.home
   :layout :projectlayout
   :object nil})