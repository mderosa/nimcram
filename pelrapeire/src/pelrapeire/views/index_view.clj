(ns pelrapeire.views.index-view
  (:use pelrapeire.app.ui-control
	clojure.contrib.trace))

(defn login-form [map-data]
  [:form {:method "POST" :action "/login"}
   (error-list map-data)
   [:p {:style "text-align: center"}
    [:a {:href "/users/new"} "create a new account"]]
   [:label {:for "login-name"} "email:"]
   [:input {:type "text" :id "email" :name "email"}]
   [:label {:for "password"} "password:"]
   [:input {:type "password" :id "password" :name "password" :class "fill"}]
   [:p {:style "text-align: center"}
    [:input {:type "submit" :id "submit" :name "submit" :value "login"}]]])

(def txt-law-of-loop
     " - Hokulea is a project management application that gives teams feedback on their loop time, 
the time from the start of feature planning to 
feature delivered. Effective teams have loops that are, first, consistent and,
second, that accelerate over time. If your team is going to be effective you will want your loops
to have those characteristics as well.  Join us; you too can loop with the best.")

(def txt-better-practices
     " - Hokulea encourages teams to continuously deliver small units of functionality that over time
accumulate into major functionality.  Organizing work into small, quick units doesnt come 
naturally, but its power and effectiveness is astonishingly strong once it becomes a habit. Its a simple idea
with a big impact.  Try it for yourself.")

(defn show [map-data]
  (let [js nil
	css nil
	title "welcome"
	content 
	[:div
	 [:div {:id "top"}
	  [:div {:id "headline" :class "infoblock"}
	   [:h2 "Navigate well"]
	   [:p 
	    [:strong "The law of the loop"] txt-law-of-loop]
	   [:p 
	    [:strong "Better practices"] txt-better-practices]]
	  [:div {:id "login"}
	   (rounded-corner-crown)
	   (rounded-corner-base "sign in now")
	   (login-form map-data)]]
	 [:div {:id "steps"}]]]
    {:js js :css css :title title :content content}))

