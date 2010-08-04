(ns pelrapeire.pages.pages
  (:require pelrapeire.pages.projects.n.home
	    pelrapeire.pages.null
	    pelrapeire.pages.index))

(def pages 
     {:index pelrapeire.pages.index/show
      :projects.n.home pelrapeire.pages.projects.n.home/show
      :null pelrapeire.pages.null/show})