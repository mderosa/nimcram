(ns pelrapeire.views.viewdefs
  (:require pelrapeire.views.projects.uid.home-view
	    pelrapeire.views.json-view
	    pelrapeire.views.index-view
	    pelrapeire.views.about-view
	    pelrapeire.views.redirect-view
	    pelrapeire.views.users.new-view
	    pelrapeire.views.users.uid.projects-view
	    pelrapeire.views.users.uid.projects.uid.home-view))

(def pages 
     {:index pelrapeire.views.index-view/show
      :about pelrapeire.views.about-view/show
      :redirect pelrapeire.views.redirect-view/show
      :projects.n.home pelrapeire.views.projects.uid.home-view/show
      :users.new pelrapeire.views.users.new-view/show
      :users.n.projects pelrapeire.views.users.uid.projects-view/show
      :users.uid.projects.uid.home pelrapeire.views.users.uid.projects.uid.home-view/show
      :json-view pelrapeire.views.json-view/show})
