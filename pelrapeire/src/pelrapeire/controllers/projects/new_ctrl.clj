(ns pelrapeire.controllers.projects.new-ctrl
  (:use clojure.contrib.trace))

(defn run [{:keys [fn-get fn-create fn-update]} {params :params :as req}]
  {:pre [(not (nil? (params "project")))]}
  (let [ctx (trace (:context req))
	user (fn-get (:user-uid (:context req)))]
    {:view :users.n.projects
     :layout :minimallayout
     :object user}))