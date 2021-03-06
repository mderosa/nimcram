(ns 
    ^{:doc "this is the interface to the pelrapeire database"}
  pelrapeire.repository.dbpelrapeire
  (:use pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbops)
  (:require [clojure.contrib.str-utils2 :as s])
  (:import org.joda.time.DateTime))

(defn pel-get [id]
  (op-get id db-config))

(defn pel-create 
  ([map-data]
     {:pre [(not (nil? (map-data "type")))]}
     (op-create map-data db-config))
  ([id map-data]
     {:pre [(not (nil? (map-data "type")))]}
     (op-create id map-data db-config)))

(defn 
  #^{:doc "to use the update function just pass the document object into the 
function along with a mode, either :append or :write; the object should
contain a '_id' and a '_rev'"}
  pel-update [map-data mode]
  (op-update map-data mode db-config))

(defn pel-delete [map-data]
  (op-delete map-data db-config))

;;curl http://localhost:5984/picominmin/_design/picominmin/_view/project-tasks?key=%22PicoMinMin%22    
(defn 
  #^{:doc "this function gets all the tasks associated with a project. this could be
a lot of tasks so be careful what you wish for."}
  project-tasks [#^String project-name]
  {:pre [(not (s/blank? project-name))]}
  (let [loc (str "_design/picominmin/_view/project-tasks?key=%22" project-name "%22")]
    (op-get-view loc db-config)))

(defn
  #^{}
  users-by-email [#^String email]
  {:pre [(not (s/blank? email))]}
  (let [loc (str "_design/picominmin/_view/users?key=%22" email "%22")]
    (op-get-view loc db-config)))

(defn
  #^{:doc "This function is intended for use during the create account process for a new user. Since a
user may be registered as a contributor to an existing project. We will want to find any projects that
he has preexisting registrations for and set those in the 'projectsImContributingTo' attribute."}
  contributors-and-their-projects [^String email]
  {:pre [(not (s/blank? email))]}
  (let [loc (str "_design/picominmin/_view/contributors-and-their-projects?key=%22" email "%22")]
    (op-get-view loc db-config)))

(defn
  #^{:doc "returns a list of all the task associated with a project that are proposed
ordering them from 3 down to 0"}
  proposed-project-tasks [#^String project-name]
  {:pre [(not (s/blank? project-name))]}
  (let [loc (str "_design/picominmin/_view/proposed-tasks?"
		 "startkey=[%22" project-name "%22,3]&"
		 "endkey=[%22" project-name "%22]&"
		 "descending=true")]
    (op-get-view loc db-config)))

(defn
  #^{:doc "returns a list of all the task associated with a project that do
not have a taskCompleteDate"}
  wip-project-tasks [#^String project-name]
  {:pre [(not (s/blank? project-name))]}
  (let [loc (str "_design/picominmin/_view/work-in-progress-tasks?"
		 "startkey=[%22" project-name "%22]&"
		 "endkey=[%22" project-name "%22,9999]")]
    (op-get-view loc db-config)))

(defn
  #^{:doc "returns a list of all the task associated with a project that do
not have a taskCompleteDate"}
  active-project-tasks [#^String project-name]
  {:pre [(not (s/blank? project-name))]}
  (let [loc (str "_design/picominmin/_view/project-tasks?key=%22" project-name "%22")]
    (op-get-view loc db-config)))

;;http://localhost:5984/picominmin/_design/picominmin/_view/delivered-tasks?startkey=[%22PicoMinMin%22,2011]
(defn
  ^{:doc "returns a list of delivered tasks (not (nil? taskCompleteDate)).  if a
cut off date is supplied then projects where (< taskCompleteDate cut-off-date) are
not included in the returned list"}
  delivered-project-tasks 
  ([^String project-name]
     {:pre [(not (s/blank? project-name))]}
     (let [loc (str "_design/picominmin/_view/delivered-tasks?"
		    "descending=true&"
		    "startkey=[%22" project-name "%22,9999]&"
		    "endkey=[%22" project-name "%22]")]
       (op-get-view loc db-config)))
  ([^String project-name ^DateTime cut-off-date]
     {:pre [(not (s/blank? project-name))]}
     (let [loc (str "_design/picominmin/_view/delivered-tasks?"
		    "descending=true&"
		    "startkey=[%22" project-name "%22,9999]&"
		    "endkey=[%22" 
		    project-name "%22,"
		    (.. cut-off-date year get) ","
		    (.. cut-off-date monthOfYear get) ","
		    (.. cut-off-date dayOfMonth get) ","
		    (.. cut-off-date hourOfDay get) ","
		    (.. cut-off-date minuteOfHour get) ","
		    (.. cut-off-date secondOfMinute get) "]")]
       (op-get-view loc db-config))))

;;http://localhost:5984/picominmin/_design/picominmin/_view/delivered-tasks?descending=true&startkey=[%22PicoMinMin%22,9999]&endkey=[%22PicoMinMin%22]&limit=300
(defn
  ^{:doc "returns a list of delivered project task in order most recent to less recently deployed.  The
function takes a parameter 'limit' which specifies the maximum number of entries to return"}
  n-most-recent-delivered-project-tasks [^String project-name, limit]
     {:pre [(not (s/blank? project-name)) (not (nil? limit))]}
     (let [loc (str "_design/picominmin/_view/delivered-tasks?"
		    "descending=true&"
		    "startkey=[%22" project-name "%22,9999]&"
		    "endkey=[%22" project-name "%22]&"
		    "limit=" limit)]
       (op-get-view loc db-config)))