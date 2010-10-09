(ns pelrapeire.views.users.uid.projects-view
  (:use clojure.contrib.trace))

(defn make-project-links [ls]
  (map (fn [x] 
	 [:a {:href (str "/projects/" x "/home")} x]) ls))

(defn show [map-data]
      {:js nil :css nil :title "projects" :content 
       [:div {:id "projects"}
	[:div {:id "contributing-to"}
	 (into
	  [:h2 "Projects I'm contributing to"]
	  (make-project-links ((:object map-data) "projectsImContributingTo")))]
	 
	[:div {:id "following"}
	 (into
	  [:h2 "Projects I'm following"]
	  (make-project-links ((:object map-data) "projectsImFollowing")))]]})
