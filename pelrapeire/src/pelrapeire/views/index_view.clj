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
     " - Hokulea gives you feedback on your loop time, the time from feature wanted to 
feature delivered. If your working effectively your loop time is, first, consistent and,
second, accelerating over time. If groups cant improve their loop, long term, they're not
learning to produce the features people want. Can you loop with the best -- join 
us and see where you stand.")

(def txt-better-practices
     " - Hokulea removes work load and allow you to do things
you couldnt easily do before.  We
are also a forum to help teams and individuals find out
what works, and to guide them to better practices.  Are you ready to work
better?  Its time.")

(defn show [map-data]
  (let [js nil
	css nil
	title "welcome"
	content 
	[:div
	 [:div {:id "top"}
	  [:div {:id "headline"}
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

