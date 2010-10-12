(ns pelrapeire.views.users.new-view)

(defn show [map-data]
  {:js nil :css nil :title "new user registration" :content
   [:form {:method "POST" :action "/users"}
    [:label {:for "email"} "email:"]
    [:input {:type "text" :id "email" :name "email"}]
    [:label {:for "password"} "password:"]
    [:input {:type "text" :id "password" :name "password"}]
    [:label {:for "confirmPassword"} "confirm password:"]
    [:input {:type "text" :id="confirmPasswordm" :name "confirmPasswordm"}]
    [:input {:type "submit" :name "submit" :value "create" :class "submitting"}]
    ]})