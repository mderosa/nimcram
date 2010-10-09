(ns pelrapeire.app.specification.user
  ^{:doc "this module precoditions user data prior to being sent to the back end"}
  (:use pelrapeire.app.validators
	pelrapeire.app.encrypt
	pelrapeire.app.specification.conditioners)
  (:require [clojure.contrib.str-utils2 :as s]))

(def template-user
     {"type" "user"
      "password" nil
      "projectsImContributingTo" []
      "projectsImFollowing" []
      "email" nil})

(def condition-fns
  {"_id" (fn [s] {:pre (id? s)} s)
   "_rev" (fn [s] {:pre (revision? s)} s)
   "projectsImContributingTo" string-param-to-vector
   "projectsImFollowing" string-param-to-vector})

(defn
  ^{:doc "creates user objects that meet specifications"}
  create-user [map-data]
  {:pre [(not (s/blank? (map-data "password"))) (email? (map-data "email"))]}
  (let [contrib ((condition-fns "projectsImContributingTo") (map-data "projectsImContributingTo"))
	following ((condition-fns "projectsImFollowing") (map-data "projectsImFollowing"))
	conditioned-data (assoc map-data "projectsImContributingTo" contrib 
				"projectsImFollowing" following
				"password" (shaHash (map-data "password")))]
    (conj template-user conditioned-data)))

(defn 
  #^{:doc "this function is responsible for preprocessing task data before that task data
is sent to the backend for update. Conditioning should only be done for data that already
exists in the database and is presently undergoing a update"}
  condition-user [map-data]
  (condition-obj condition-fns map-data))

