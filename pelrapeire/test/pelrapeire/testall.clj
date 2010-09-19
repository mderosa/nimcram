(ns pelrapeire.testall
  (:use clojure.test)
  (:require pelrapeire.core-test
	    pelrapeire.controllers.projects.uid.home-ctrl-test
	    pelrapeire.controllers.projects.uid.tasks-ctrl-test
	    pelrapeire.controllers.projects.uid.tasks.uid-ctrl-test
	    pelrapeire.views.projects.uid.home-view-test
	    pelrapeire.views.users.uid.projects-view-test
	    pelrapeire.repository.dbpelrapeire-test
	    pelrapeire.repository.db.couchresponsehandler-test
	    pelrapeire.repository.db.dbapi-test
	    pelrapeire.repository.db.dbapiwrapper-test
	    pelrapeire.app.task-statistics-test
	    pelrapeire.app.ui-control-test
	    pelrapeire.app.validators-test
	    pelrapeire.app.convert-test))

(run-tests 'pelrapeire.core-test)
(run-tests 'pelrapeire.controllers.projects.uid.home-ctrl-test)
(run-tests 'pelrapeire.controllers.projects.uid.tasks-ctrl-test)
(run-tests 'pelrapeire.controllers.projects.uid.tasks.uid-ctrl-test)
(run-tests 'pelrapeire.views.projects.uid.home-view-test)
(run-tests 'pelrapeire.views.users.uid.projects-view-test)
(run-tests 'pelrapeire.repository.dbpelrapeire-test)
(run-tests 'pelrapeire.repository.db.couchresponsehandler-test)
(run-tests 'pelrapeire.repository.db.dbapi-test)
(run-tests 'pelrapeire.repository.db.dbapiwrapper-test)
(run-tests 'pelrapeire.app.task-statistics-test)
(run-tests 'pelrapeire.app.ui-control-test)
(run-tests 'pelrapeire.app.validators-test)
(run-tests 'pelrapeire.app.convert-test)
