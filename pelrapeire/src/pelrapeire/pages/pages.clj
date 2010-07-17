(ns pelrapeire.pages.pages
  (:require pelrapeire.pages.projects.n.home
	    pelrapeire.pages.null))

(def pages 
     {:projects.n.home pelrapeire.pages.projects.n.home/show
      :null pelrapeire.pages.null/show})