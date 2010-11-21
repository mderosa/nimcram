(ns pelrapeire.controllers.users.uid.projects-ctrl
  (:use clojure.contrib.trace
	pelrapeire.app.validators
	pelrapeire.app.exception
	pelrapeire.repository.mail.mail
	pelrapeire.repository.mail.invitationmessage
	pelrapeire.repository.mailconfig
	pelrapeire.app.specification.conditioners)
  (:import org.apache.http.client.HttpResponseException
	   pelrapeire.repository.mail.invitationmessage.InvitationData)
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
    (if (or to-error project-error)
      (throw (Exception. (str (vec (filter #(not (nil? %)) [to-error, project-error])))))
      nil)))

(defn append-no-duplicates [current additions]
  (vec (set (flatten (conj current additions)))))

(defn add-recipients-to-project-if-not-contributor [fn-get fn-update params]
  (do (validate-params params)
      (let [project (fn-get (params "project"))
	   updated-contributors (append-no-duplicates (project "contributors") (csv-string-to-vector (params "to")))
	   updated-project (assoc project "contributors" updated-contributors)]
	(do (fn-update updated-project :write)))))

(defn add-project-to-recipient-if-registered-user [fn-update fn-users-by-email params]
  (doseq [email (csv-string-to-vector (params "to"))]
    (if-let [user (first (map #(% "value") ((fn-users-by-email email) "rows")))]
      (let [projects-im-contributing-to (append-no-duplicates (user "projectsImContributingTo") (params "project"))]
	(fn-update (assoc user "projectsImContributingTo" projects-im-contributing-to) :write)))))

(defn mail-user-invitations [params req mail-config]
  {:pre [(string? (params "message"))]}
  (let [to (csv-string-to-vector (params "to"))
	invitation-data (InvitationData. (params "user-id") to (params "message") (params "project") ((:headers req) "host"))]
    (send-mail invitation-data mail-config)))

(defn run-post [fn-get fn-update fn-users-by-email {params :params :as req}]
  {:pre [(#{"invite"} (params "action"))]}
  (cond 
   (= "invite" (params "action"))
   (let [user (fn-get (params "user-id"))
	 rslt (with-exception-translation 
		(do (add-recipients-to-project-if-not-contributor fn-get fn-update params)
		    (add-project-to-recipient-if-registered-user fn-update fn-users-by-email params)
		    (mail-user-invitations params req mail-config)
		    {:view :users.n.projects :layout :minimallayout :object user}))
	 ]
     (if (:errors rslt)
       {:view :users.n.projects	:layout :minimallayout :object user :errors {:invite (:errors rslt)}}
       rslt))))

(defn run [{:keys [fn-get fn-update]} fn-users-by-email {params :params :as req}]
  {:pre [(not (nil? (params "user-id")))]}
  (cond
   (= (:request-method req) :get)
   (run-get fn-get req)

   (= (:request-method req) :post)
   (run-post fn-get fn-update fn-users-by-email req)

   true
   (throw (UnsupportedOperationException. "only GET and POST operations are permitted"))))
