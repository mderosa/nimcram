(ns pelrapeire.controllers.contactus-ctrl)

(defn run [req]
  {:view :contactus
   :layout :minimallayout
   :params (:params req)})