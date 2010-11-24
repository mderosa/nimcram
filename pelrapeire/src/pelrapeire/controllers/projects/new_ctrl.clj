(ns pelrapeire.controllers.projects.new-ctrl
  (:use clojure.contrib.trace
	pelrapeire.app.specification.project)
  (:import org.apache.http.client.HttpResponseException)
  (:require [clojure.contrib.str-utils2 :as s]))

(defn project-exists? [fn-get id]
  (try 
   (let [project (fn-get id)]
     true)
   (catch HttpResponseException e
     false)))

(defn add-user-project [^String id user]
  (let [contributing-to (conj (user "projectsImContributingTo") id)]
    (assoc user "projectsImContributingTo" contributing-to)))

(defn run [{:keys [fn-get fn-create fn-update]} {params :params :as req}]
  {:pre [(not (nil? (params "project")))
	 (not (s/blank? (params "project")))]}
  (let [user (fn-get (:user-uid (:context req)))]
    (if (project-exists? fn-get (params "project"))
      {:view :users.n.projects
       :layout :minimallayout
       :object user
       :errors {:newproject ["a project with this name already exists"]}}
      (let [new-project (create-project {"contributors" (user "email")})
	    rsp-ok-error1 (fn-create (. (params "project") trim) new-project)
	    modified-user (add-user-project (rsp-ok-error1 "id") user)
	    rsp-ok-error2 (fn-update modified-user :append)]
	{:view :users.n.projects
	 :layout :minimallayout
	 :object modified-user}))))