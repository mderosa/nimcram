(ns pelrapeire.controllerdefs
  (:use pelrapeire.repository.dbpelrapeire)
  (:require pelrapeire.controllers.index-ctrl
	    pelrapeire.controllers.login-ctrl
	    pelrapeire.controllers.projects.uid.home-ctrl
	    pelrapeire.controllers.projects.uid.tasks-ctrl
	    pelrapeire.controllers.users.uid.projects-ctrl))

(def 
 #^{:doc "this map configures the controllers in the project by passing them
in a database access function. controllers will only have one function, 
run(), which will take any number of parameters the last of which will be a
request data object"} 
 controllers 
 {:index pelrapeire.controllers.index-ctrl/run
  :login 
  (partial pelrapeire.controllers.login-ctrl/run users-by-email)
  :projects-n-home 
  (partial pelrapeire.controllers.projects.uid.home-ctrl/run 
	   proposed-project-tasks
	   wip-project-tasks
	   completed-project-tasks)
  :projects-n-tasks
  (partial pelrapeire.controllers.projects.uid.tasks-ctrl/run
	   pel-create pel-update pel-get)
  :users-n-projects
  (partial pelrapeire.controllers.users.uid.projects-ctrl/run pel-get)})
