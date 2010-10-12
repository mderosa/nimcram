(ns pelrapeire.controllers.users.new-ctrl)

(defn run [{:keys [fn-get fn-put]} req]
  {:view :users.new
   :layout :minimallayout})
