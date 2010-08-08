(ns pelrapeire.testall
  (:use clojure.test)
  (:require pelrapeire.coretest
	    pelrapeire.controllers.projects.n.hometest
	    pelrapeire.controllers.projects.n.taskstest
	    pelrapeire.pages.projects.n.hometest
	    pelrapeire.repository.dbpelrapeiretest
	    pelrapeire.repository.db.couchresponsehandlertest
	    pelrapeire.repository.db.dbapitest
	    pelrapeire.repository.db.dbapiwrappertest
	    pelrapeire.app.taskstatisticstest))

(run-tests 'pelrapeire.coretest)
(run-tests 'pelrapeire.controllers.projects.n.hometest)
(run-tests 'pelrapeire.controllers.projects.n.taskstest)
(run-tests 'pelrapeire.pages.projects.n.hometest)
(run-tests 'pelrapeire.repository.dbpelrapeiretest)
(run-tests 'pelrapeire.repository.db.couchresponsehandlertest)
(run-tests 'pelrapeire.repository.db.dbapitest)
(run-tests 'pelrapeire.repository.db.dbapiwrappertest)
(run-tests 'pelrapeire.app.taskstatisticstest)
