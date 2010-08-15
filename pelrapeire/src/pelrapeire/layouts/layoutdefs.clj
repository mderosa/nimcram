(ns pelrapeire.layouts.layoutdefs
  (:require pelrapeire.layouts.project-layt
	    pelrapeire.layouts.minimal-layt
	    pelrapeire.layouts.json-layt))

(def layouts 
     {:projectlayout pelrapeire.layouts.project-layt/render
      :minimallayout pelrapeire.layouts.minimal-layt/render
      :nulllayout pelrapeire.layouts.json-layt/render})
