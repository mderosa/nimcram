(ns pelrapeire.pages.index
  (:use pelrapeire.app.uicontrol))

(defn login-form []
  [:form {:method "POST" :action "/login"}
   [:p {:style "text-align: center"}
    [:a {:href "/users/new"} "create a new account"]]
   [:label {:for "login-name"} "login:"]
   [:input {:type "text" :id "login-name" :name "login-name"}]
   [:label {:for "password"} "password:"]
   [:input {:type "text" :id "password" :name "password" :class "fill"}]
   [:p {:style "text-align: center"}
    [:input {:type "submit" :id "submit" :name "submit" :value "login"}]]])

(def txt-law-of-loop
     " - We want to give you feedback on your loop time, the time from feature wanted to 
feature delivered. If your working effectively your loop time is, first, consistent and,
second, accelerating over time. If ones group cant close the loop, long term, it wont
successfully produce the features people want. Can you loop with the big boys -- join 
us and see where you stand.")

(def txt-better-practices
     " - We want this tool to remove work load and allow you to do things
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
	   [:h2 "Its what counts"]
	   [:p 
	    [:strong "The law of the loop"] txt-law-of-loop]
	   [:p 
	    [:strong "Better practices"] txt-better-practices]]
	  [:div {:id "login"}
	   (rounded-corner-crown)
	   (rounded-corner-base "sign in now")
	   (login-form)]]
	 [:div {:id "steps"}]]]
    {:js js :css css :title title :content content}))

