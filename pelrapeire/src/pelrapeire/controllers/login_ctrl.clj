(ns pelrapeire.controllers.login-ctrl
  (:use clojure.contrib.trace
	pelrapeire.app.encrypt)
  (:import org.apache.http.client.HttpResponseException))

(defn 
  #^{:doc "returns either a map of user information or a map of error info"}
  query-user [fn-db-get uid]
  (let [db-rsp-rows ((fn-db-get uid) "rows")]
    (if (not= 1 (count db-rsp-rows))
      {:errors '("unable to find a matching user name")}
      ((get db-rsp-rows 0) "value"))))
	   
(defn 
  #^{:doc "returns a user object or a map of errors"}
  authenticate-password [in-pwd user]
  (cond
   (:errors user) user
   (not= (trace (shaHash in-pwd)) (trace (user "password"))) {:errors '("incorrect password")}
   true user))

(defn run [fn-db-get params]
  (let [userA (query-user fn-db-get (params "email"))
	userB (authenticate-password (params "password") userA)]
    (if (:errors userB)
      {:view :index :layout :minimallayout :errors (:errors userB)}
      {:view :redirect
       :layout nil
       :url (str "/users/" (userB "_id") "/projects")})))
