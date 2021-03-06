(ns pelrapeire.controllerdefs
  (:use pelrapeire.repository.dbpelrapeire)
  (:require pelrapeire.controllers.index-ctrl
	    pelrapeire.controllers.about-ctrl
	    pelrapeire.controllers.contactus-ctrl
	    pelrapeire.controllers.login-ctrl
	    pelrapeire.controllers.mail.admin-ctrl
	    pelrapeire.controllers.projects.new-ctrl
	    pelrapeire.controllers.projects.uid.home-ctrl
	    pelrapeire.controllers.projects.uid.tasks-ctrl
	    pelrapeire.controllers.projects.uid.tasks.uid-ctrl
	    pelrapeire.controllers.users-ctrl
	    pelrapeire.controllers.users.new-ctrl
	    pelrapeire.controllers.users.uid.projects-ctrl
	    pelrapeire.controllers.users.uid.projects.uid.home-ctrl))

(defstruct crud-struct :fn-create :fn-get :fn-update :fn-delete)
(def crud (struct crud-struct pel-create pel-get pel-update pel-delete))

(def 
 #^{:doc "this map configures the controllers in the project by passing them
in a database access function. controllers will only have one function, 
run(), which will take any number of parameters the last of which will be a
request data object"} 
 controllers 
 {:index pelrapeire.controllers.index-ctrl/run
  :about pelrapeire.controllers.about-ctrl/run
  :contactus 
  (partial pelrapeire.controllers.contactus-ctrl/run crud)
  :login 
  (partial pelrapeire.controllers.login-ctrl/run users-by-email)
  :mail-admin
  (partial pelrapeire.controllers.mail.admin-ctrl/run crud)
  :projects-new
  (partial pelrapeire.controllers.projects.new-ctrl/run crud)
  :projects-uid-home 
  (partial pelrapeire.controllers.projects.uid.home-ctrl/run 
	   proposed-project-tasks
	   wip-project-tasks
	   delivered-project-tasks)
  :projects-uid-tasks
  (partial pelrapeire.controllers.projects.uid.tasks-ctrl/run
	   pel-create pel-update pel-get)
  :projects-uid-tasks-uid
  (partial pelrapeire.controllers.projects.uid.tasks.uid-ctrl/run
	   pel-get pel-update pel-delete)
  :users
  (partial pelrapeire.controllers.users-ctrl/run crud users-by-email contributors-and-their-projects)
  :users-new
  (partial pelrapeire.controllers.users.new-ctrl/run crud)
  :users-uid-projects
  (partial pelrapeire.controllers.users.uid.projects-ctrl/run crud users-by-email)
  :users-uid-projects-uid-home
  (partial pelrapeire.controllers.users.uid.projects.uid.home-ctrl/run crud n-most-recent-delivered-project-tasks)

})
