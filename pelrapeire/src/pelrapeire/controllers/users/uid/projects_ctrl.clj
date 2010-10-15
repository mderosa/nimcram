(ns pelrapeire.controllers.users.uid.projects-ctrl
  (:use clojure.contrib.trace
	pelrapeire.app.validators
	pelrapeire.app.specification.conditioners)
  (:import org.apache.http.client.HttpResponseException)
  (:require [clojure.string :as cs]))

(defn run-get [fn-get {params :params}]
  (let [user (fn-get (params "user-id"))] 
  {:view :users.n.projects
   :layout :minimallayout
   :object user}))

(defn validate-params [params]
  (let [to-error (let [emails (csv-string-to-vector (params "to"))] 
		   (if (= [] emails)
		     "a list of email recipients should be supplied"
		     (if (not (email? emails))
		       "the list of emails does not have the expected form")))
	project-error (if (cs/blank? (params "project"))
			"a project name must be selected")]
    (filter #(not (nil? %)) [to-error, project-error])))

(defn append-no-duplicates [current additions]
  (vec (set (flatten (conj current additions)))))

(defn add-recipients-to-project-if-not-contributor [fn-get fn-update params]
  (let [param-errors (validate-params params)]
    (if (empty? param-errors)
      (if-let [project (try 
			(fn-get (params "project"))
			(catch HttpResponseException e nil))]
	(let [updated-contributors (append-no-duplicates (project "contributors") (csv-string-to-vector (params "to")))
	      updated-project (assoc project "contributors" updated-contributors)
	      rsp-ok-error (fn-update updated-project :write)]
	  (if (rsp-ok-error "ok")
	    rsp-ok-error
	    {:errors [(rsp-ok-error "description")]}))
	{:errors ["the project was not found"]})
      {:errors param-errors})))

(defn add-project-to-recipient-if-registered-user [fn-update fn-users-by-email params]
  (doseq [email (csv-string-to-vector (params "to"))]
    (if-let [user (first (map #(% "value") ((fn-users-by-email email) "rows")))]
      (let [projects-im-contributing-to (append-no-duplicates (user "projectsImContributingTo") (params "project"))]
	(fn-update (assoc user "projectsImContributingTo" projects-im-contributing-to) :write)))))

(defn run-post [fn-get fn-update fn-users-by-email {params :params}]
  {:pre [(#{"invite"} (params "action"))]}
  (cond 
   (= "invite" (params "action"))
   (let [rslt-ok-error1 (add-recipients-to-project-if-not-contributor fn-get fn-update params)
	 user (fn-get (params "user-id"))]
     (if (rslt-ok-error1 "ok")
       (let [rslt-ok-error2 (add-project-to-recipient-if-registered-user fn-update fn-users-by-email params)
	     ]
	 {:view :users.n.projects
	  :layout :minimallayout
	  :object user})
       {:view :users.n.projects
	:layout :minimallayout
	:object user
	:errors (:errors rslt-ok-error1)}))))

(defn run [{:keys [fn-get fn-update]} fn-users-by-email {params :params :as req}]
  {:pre [(not (nil? (params "user-id")))]}
  (cond
   (= (:request-method req) :get)
   (run-get fn-get req)

   (= (:request-method req) :post)
   (run-post fn-get fn-update fn-users-by-email req)

   true
   (throw (UnsupportedOperationException. "only GET and POST operations are permitted"))))
