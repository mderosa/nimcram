(ns pelrapeire.views.users.uid.projects.uid.home-view
  (:use pelrapeire.app.statistics.functions
	clojure.contrib.json
	pelrapeire.app.statistics.xbarchart))

(defn graph-description [xbar-data]
  (if (< 0 (count (:subgroupavgs xbar-data)))
    [:div {:id "xbardescription"}
     [:p (str "average work in-progress time (hours): " (double (/ (Math/round (double (* 10 (:xbarbar xbar-data)))) 10)))]
     [:p (str "work in-progress warning limit (hours): " (double (/ (Math/round (double (* 10 (:xbarucl xbar-data)))) 10)))]]
    [:div {:id "xbardescription"}
     [:p "currently there is not enough data for a data display"]])
)

(defn show [map-data]
  (let [hours-subgroups (:collection map-data)
	xs (calculate-subgroup-averages hours-subgroups)
	ss (calculate-subgroup-std-deviations hours-subgroups)
	xbar-data {:xbarbar (average xs)
		   :xbarucl (xbar-ucl xs ss 3)
		   :xbarlcl (xbar-lcl xs ss 3)
		   :subgroupavgs xs
		   }]
  {:js ["/js/protovis/protovis-d3.2.js"] :css nil :title "your home" :content 
   [:div 
    [:div {:id "xbar"}
     [:script (str "var xbardata=" (json-str xbar-data) ";")]
     [:script {:src "/js/users/uid/projects/uid/home.js" :type "text/javascript"}]
     ]
    (graph-description xbar-data) 
    ]}))