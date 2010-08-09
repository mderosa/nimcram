(ns pelrapeire.controllers.login
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

(defn run [fn-db-get params]
  (let [userA (query-user fn-db-get (params "email"))
	userB (authenticate-password (params "password") userA)]
    (if (:errors userB)
      {:view :index :layout :minimallayout :errors (:errors userB)}
      {:view :users-n-projects :layout :minimallayout :params params})))