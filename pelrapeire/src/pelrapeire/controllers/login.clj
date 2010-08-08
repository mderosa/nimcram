(ns pelrapeire.controllers.login)

(defn run [fn-db-get params]
  {:view :users-n-projects
   :layout :minimallayout
   :params params})


(defn query-user []
  (try
   ()
   (catch )))

(defn authenticate-password [])