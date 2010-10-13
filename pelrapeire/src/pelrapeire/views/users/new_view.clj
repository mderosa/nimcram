(ns pelrapeire.views.users.new-view)

(defn show [map-data]
  {:js nil :css nil :title "new user registration" :content
   [:div 
    (if (:errors map-data)
      [:ul {:class "errors"}
       (for [e (:errors map-data)] [:li e])])
    [:form {:method "POST" :action "/users"}
     [:label {:for "email"} "email:"]
     [:input {:type "text" :id "email" :name "email"}]
     [:label {:for "password"} "password:"]
     [:input {:type "password" :id "password" :name "password"}]
     [:label {:for "confirmPassword"} "confirm password:"]
     [:input {:type "password" :id="confirmPassword" :name "confirmPassword"}]
     [:input {:type "submit" :name "submit" :value "create" :class "submitting"}]
     ]]})