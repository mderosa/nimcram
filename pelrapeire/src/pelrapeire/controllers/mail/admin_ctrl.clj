(ns pelrapeire.controllers.mail.admin-ctrl
  (:use pelrapeire.repository.mail.mail
	clojure.contrib.trace
	pelrapeire.repository.mail.contactusmessage
	pelrapeire.repository.mailconfig)
  (:import pelrapeire.repository.mail.contactusmessage.ContactUsData))

(defn mail-to-admin [params user mail-config]
  (let [data (ContactUsData. 
	      (if (nil? user) nil (user "email"))
	      (params "email") 
	      (str (:username mail-config) "@gmail.com")
	      (params "message"))]
    (send-mail data mail-config)))

(defn run [{:keys [fn-get]} {params :params :as req}]
  (let [user-id (:user-uid (:context req))
	user (if (nil? user-id)
	       nil
	       (fn-get user-id))
	rslt (trace (mail-to-admin params user mail-config))]
    {:view :redirect
     :layout nil
     :url (params "returnurl")}))