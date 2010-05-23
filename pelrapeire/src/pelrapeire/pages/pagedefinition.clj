
(ns pelrapeire.pages.pagedefinition
  (:use pelrapeire.pages.tiles
	pelrapeire.pages.projects.n.home))

(defstruct page-def 
  :layout
  :js
  :css
  :title
  :entry-point)

(def project-home 
     (struct page-def
	     pelrapeire.pages.tiles/project-tile
	     "/js/projects/n/home.js"
	     nil
	     pelrapeire.pages.projects.n.home/title
	     pelrapeire.pages.projects.n.home/show-page))

