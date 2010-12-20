(ns pelrapeire.views.contactus-view
  (:use clojure.contrib.trace))

(defn show [map-data]
  (let [tr (trace map-data)]
  {:js nil :css nil :title "send us you thoughts" :content
   [:div {:style "width:50em;margin:auto"}
    [:form {:method "POST" :action "/mail/admin"}
     [:input {:type "hidden" :name "returnurl"}]
     [:p "Feel free to send us your thoughs on the application, what does / doesnt work for you, or any questions you have."]
     [:label {:for "email"} "your email"]
     [:input {:name "email" :type "text"}]
     [:label {:for "message"} "message:"]
     [:textarea {:name "message"}]
     [:input {:type "submit" :name "submit" :value "send" :class "submitting"}]
     ]]}))

