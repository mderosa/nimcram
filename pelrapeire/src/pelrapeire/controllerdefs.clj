(ns pelrapeire.controllerdefs
  (:use pelrapeire.repository.dbpelrapeire)
  (:require pelrapeire.controllers.index
	    pelrapeire.controllers.login
	    pelrapeire.controllers.projects.n.home
	    pelrapeire.controllers.projects.n.tasks
	    pelrapeire.controllers.test))

(def 
 #^{:doc "this map configures the controllers in the project by passing them
in a database access function. controllers will only have one function, 
run(), which will take any number of parameters the last of which will be a
request data object"} 
 controllers 
     (ref {:index pelrapeire.controllers.index/run
	   :login 
	   (partial pelrapeire.controllers.login/run pel-get)
	   :projects-n-home 
	   (partial pelrapeire.controllers.projects.n.home/run 
		    active-project-tasks
		    completed-project-tasks)
	   :projects-n-tasks
	   (partial pelrapeire.controllers.projects.n.tasks/run
		    pel-create pel-update pel-get)
	   :test pelrapeire.controllers.test/run}))