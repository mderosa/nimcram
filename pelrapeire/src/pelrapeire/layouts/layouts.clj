(ns pelrapeire.layouts.layouts
  (:require pelrapeire.layouts.projectlayout
	    pelrapeire.layouts.nulllayout))

(def layouts 
     {:projectlayout pelrapeire.layouts.projectlayout/render
      :nulllayout pelrapeire.layouts.nulllayout/render})