(ns pelrapeire.views.contactus-view
  (:use clojure.contrib.trace))

(defn show [map-data]
  (let [user-email ((:object map-data) "email")]
    {:js nil :css nil :title "send us you thoughts" :content
     [:div {:style "width:50em;margin:auto"}
      [:form {:method "POST" :action "/mail/admin"}
       [:input {:type "hidden" :name "returnurl" :value (:referer (:context map-data))}]
       [:p "Feel free to send us your thoughs on the application, what does / doesnt work for you, or any questions you have."]
       [:label {:for "email"} "your email:"]
       [:input {:name "email" :type "text" :value (if (nil? user-email)
						    ""
						    user-email)}]
       [:label {:for "message"} "message:"]
       [:textarea {:name "message" :style "width:100%"}]
       [:input {:type "submit" :name "submit" :value "send" :class "submitting"}]
       ]]}))

