(ns pelrapeire.pages.index
  (:use pelrapeire.app.uicontrol))

(defn login-form []
  [:form {:method "POST" :action "/login"}
   [:label {:for "login-name"} "login:"]
   [:input {:type "text" :id "login-name" :name "login-name"}]
   [:label {:for "password"} "password:"]
   [:input {:type "text" :id "password" :name "password"}]
   [:input {:type "submit" :name "go"}]
   [:a {:href "/users/new"} "I want to create an account"]])

(defn show [map-data]
  (let [js nil
	css nil
	title "welcome"
	content 
	[:div
	 [:div {:id "headline"}
	  [:h2 "Its what counts"]
	  [:p 
	   [:strong "The law of the loop"] " - If a group is working well 
its loop time, the time from feature wanted to 
feature delivered, is first consistent and second fast and always getting
faster. If ones group cant close the loop it cant successfully produce
the features people want. Can you loop with the big boys -- join us and see 
where you stand."]
	  [:p 
	   [:strong "Better practices"] " - As a
tool our purpose is to remove work load and allow you to do things
you couldnt easily do before.  We
are also a forum to help teams and individuals find out
what works, and to guide them to better practices.  Are you ready to work
better?  Its time."]]
	 [:div {:id "login"}
	  (rounded-corner-crown)
	  (rounded-corner-base "sign in now")
	  (login-form)]
	 [:div {:id "steps"}]]
	]
    {:js js :css css :title title :content content}))

