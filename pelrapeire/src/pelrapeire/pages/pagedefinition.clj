
(ns pelrapeire.pages.pagedefinition
  (:use pelrapeire.pages.tiles)
  (:require pelrapeire.pages.projects.n.home
	    pelrapeire.pages.projects.n.tasks))

(defstruct page-def 
  :layout
  :js
  :css
  :title
  :entry-point)

(def projects-n-home 
     (struct page-def
	     pelrapeire.pages.tiles/projects-tile
	     "/js/projects/n/home.js"
	     nil
	     pelrapeire.pages.projects.n.home/title
	     pelrapeire.pages.projects.n.home/show))

(def projects-n-tasks
     (struct page-def
	     nil
	     nil
	     nil
	     nil
	     pelrapeire.pages.projects.n.tasks/show))

