(ns pelrapeire.views.viewdefs
  (:require pelrapeire.views.projects.uid.home-view
	    pelrapeire.views.json-view
	    pelrapeire.views.index-view
	    pelrapeire.views.redirect-view
	    pelrapeire.views.users.new-view
	    pelrapeire.views.users.uid.projects-view))

(def pages 
     {:index pelrapeire.views.index-view/show
      :redirect pelrapeire.views.redirect-view/show
      :projects.n.home pelrapeire.views.projects.uid.home-view/show
      :users.new pelrapeire.views.users.new-view/show
      :users.n.projects pelrapeire.views.users.uid.projects-view/show
      :json-view pelrapeire.views.json-view/show})
