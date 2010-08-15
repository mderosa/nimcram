(ns pelrapeire.controllers.users.uid.projects-ctrl
  (:use clojure.contrib.trace))

(defn run [fn-get params]
  {:pre [(not (nil? (params "user-id")))]}
  (let [user (trace (fn-get (params "user-id")))]
  {:view :users.n.projects
   :layout :minimallayout
   :object user}))
