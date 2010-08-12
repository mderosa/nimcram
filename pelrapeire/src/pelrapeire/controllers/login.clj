(ns pelrapeire.controllers.login
  (:use clojure.contrib.trace)
  (:import org.apache.http.client.HttpResponseException))

(defn 
  #^{:doc "returns either a map of user information or a map of error info"}
  query-user [fn-db-get uid]
  (try
   (fn-db-get uid)
   (catch HttpResponseException e
     (let [errorCode (. e getStatusCode)]
       (if (= 1 errorCode)
	 {:errors '("unable to find a matching user name")}
	 {:errors '((str "unexpected login error - " (code-to-error errorCode)))})))))

(defn 
  #^{:doc "returns a user object or a map of errors"}
  authenticate-password [in-pwd user]
  (cond
   (:errors user) user
   (not= in-pwd (:password user)) {:errors '("incorrect password")}
   true user))

(deftrace run [fn-db-get params]
  (let [userA (query-user fn-db-get (params "email"))
	userB (authenticate-password (params "password") userA)]
    (if (:errors userB)
      {:view :index :layout :minimallayout :errors (:errors userB)}
      (let [login-name (. (userB "email") substring 0 (. (userB "email") indexOf "@"))]
	{:view :redirect 
	 :layout :passthrough 
	 :params {:url (str "/users/" login-name "/projects")}}))))