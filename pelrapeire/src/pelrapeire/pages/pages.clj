(ns pelrapeire.pages.pages
  (:require pelrapeire.pages.projects.n.home
	    pelrapeire.pages.null
	    pelrapeire.pages.index
	    pelrapeire.pages.redirect
	    pelrapeire.pages.users.n.projects))

(def pages 
     {:index pelrapeire.pages.index/show
      :redirect pelrapeire.pages.redirect/show
      :projects.n.home pelrapeire.pages.projects.n.home/show
      :users.n.projects pelrapeire.pages.users.n.projects/show
      :null pelrapeire.pages.null/show})