(ns pelrapeire.views.users.uid.projects-view
  (:use clojure.contrib.trace))

(defn make-project-links [ls]
  (map (fn [x] 
	 [:a {:href (str "/projects/" x "/home")} x]) ls))

(defn make-project-navigation [map-data]
  [:div {:id "projects" :class "yui3-u-1-2"}
   [:div {:id "contributing-to" :class "outlined std-margin"}
    (into
     [:h2 "Navigate to projects"]
     (make-project-links ((:object map-data) "projectsImContributingTo")))]])
   
(defn new-project-form []
  [:form {:method "POST" :action "/projects/new"}
   [:label {:for "project"} "project name:"]
   [:input {:type "text" :name "project"}]
   [:input {:type "submit" :class "submitting" :name "submit" :value "create"}]])

(defn invitation-form [map-data]
  [:form {:method "POST"}
   [:input {:type "hidden" :name "action" :value "invite"}]
   [:label {:for "project"} "project:"]
   [:select {:name "project"}
    [:option {:value "" :selected "selected"} "(select a project)"]
    (for [p ((:object map-data) "projectsImContributingTo")]
      [:option {:value p} p])
    ]])

(defn make-actions [map-data]
  [:div {:id "actions" :class "yui3-u-1-2"}
   [:div {:id "newproject" :class "outlined std-margin"}
    [:h2 "Create a new project"]
    (new-project-form)]
   [:div {:id "invite" :class "outlined std-margin"}
    [:h2 "Invite others to a project"]
    (invitation-form map-data)]
   ])

(defn show [map-data]
      {:js nil :css nil :title "projects" :content 
       [:div {:class "yui3-g"}
	(make-project-navigation map-data)
	(make-actions map-data)]})
