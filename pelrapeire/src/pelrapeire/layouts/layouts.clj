(ns pelrapeire.layouts.layouts
  (:require pelrapeire.layouts.projectlayout
	    pelrapeire.layouts.minimallayout
	    pelrapeire.layouts.nulllayout))

(def layouts 
     {:projectlayout pelrapeire.layouts.projectlayout/render
      :minimallayout pelrapeire.layouts.minimallayout/render
      :nulllayout pelrapeire.layouts.nulllayout/render})