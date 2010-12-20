(ns pelrapeire.controllers.contactus-ctrl
  (:use clojure.contrib.trace))

(defn run [{:keys [fn-get]} req]
  (let [user-id (:user-uid (:context req))
	user (if (nil? user-id)
		nil
		(fn-get user-id))]
    {:view :contactus
     :layout :minimallayout
     :object user}))
