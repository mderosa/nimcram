(ns pelrapeire.views.users.uid.projects-view
  (:use clojure.contrib.trace
	pelrapeire.app.ui-control))

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
  [:form {:method "POST" :action (str "/users/" (:user-uid (:context map-data)) "/projects")}
   [:input {:type "hidden" :name "action" :value "invite"}]
   [:label {:for "project"} "project:"]
   [:select {:name "project"}
    [:option {:value "" :selected "selected"} "(select a project)"]
    (for [p ((:object map-data) "projectsImContributingTo")]
      [:option {:value p} p])
    ]
   [:label {:for "to"} "to: (separated by commas)"]
   [:input {:type "text" :id "to" :name "to" :class "fill"}]
   [:label {:for "message"} "message: (optional)"]
   [:textarea {:id "message" :name "message" :class "fill"}]
   [:input {:type "submit" :class "submitting" :name "submit" :value "invite"}]
   ])

(defn make-actions [map-data]
  [:div {:id "actions" :class "yui3-u-1-2"}
   [:div {:id "newproject" :class "outlined std-margin"}
    [:h2 "Create a new project"]
    (error-list :newproject map-data)
    (new-project-form)]
   [:div {:id "invite" :class "outlined std-margin"}
    [:h2 "Send email invites to join a project"]
    (error-list :invite map-data)
    (invitation-form map-data)]
   ])

(defn show [map-data]
      {:js nil :css nil :title "projects" :content 
       [:div {:class "yui3-g"}
	(make-project-navigation map-data)
	(make-actions map-data)]})
