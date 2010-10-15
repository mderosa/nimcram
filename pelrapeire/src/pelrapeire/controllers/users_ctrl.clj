(ns pelrapeire.controllers.users-ctrl
  (:use pelrapeire.app.validators
	pelrapeire.app.specification.user
	clojure.contrib.trace)
  (:require [clojure.contrib.str-utils2 :as s]))

(def error-msgs
     {:email-required "an email login name is required"
      :email-invalid "you entered an invalid email"
      :password-required "a password is required"
      :password-mismatch "the password and confirm password values should be the same"
      :login-exists "the email login name you selected already exists in the database"
      })

(def existance-checks
     [(fn [p] (if (s/blank? (p "email"))
	       (:email-required error-msgs) nil)), 
      (fn [p] (if (s/blank? (p "password"))
	       (:password-required error-msgs) nil))])

(def data-integrity-checks
     [(fn [p] (if (not (email? (p "email")))
		(:email-invalid error-msgs) nil)),
      (fn [p] (if (not (= (p "password") (p "confirmPassword")))
		(:password-mismatch error-msgs) nil))])

(defn check-errors [fn-get-users-by-email params]
  (let [existance-errors (filter #(not (nil? %)) (for [fn existance-checks] (fn params)))]
    (if (not-empty existance-errors)
      existance-errors
      (let [data-integrity-errors (filter #(not (nil? %)) (for [fn data-integrity-checks] (fn params)))]
	(if (not-empty data-integrity-errors)
	  data-integrity-errors
	  (let [user-array ((fn-get-users-by-email (params "email")) "rows")]
	    (if (not-empty user-array)
	      [(:login-exists error-msgs)]
	      [])))))))
	
(deftrace create-new-user [fn-create fn-contributes-to params]
  (let [contributes-to (trace (map #(% "id") ((fn-contributes-to (params "email")) "rows")))
	new-user (trace (create-user (assoc params "projectsImContributingTo" contributes-to)))
	rsp-ok-error (fn-create new-user)]
    (assoc new-user "_id" (rsp-ok-error "id") "_rev" (rsp-ok-error "rev"))))

(defn run [{:keys [fn-get fn-create]} fn-get-users-by-email fn-contributes-to {params :params}]
  (let [errors (check-errors fn-get-users-by-email params)]
    (if (not-empty errors)
      {:view :users.new
       :layout :minimallayout
       :errors errors}
      (let [user (create-new-user fn-create fn-contributes-to params)]
	{:view :redirect
	 :layout nil
	 :url (str "/users/" (user "_id") "/projects")}))))
