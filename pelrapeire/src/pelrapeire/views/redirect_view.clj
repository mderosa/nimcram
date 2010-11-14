(ns pelrapeire.views.redirect-view
  (:use ring.util.response
	clojure.contrib.trace))

(defn show [map-data]
  {:pre [(:url map-data)]}
  (let [headers (if (:cookie map-data)
		  {"Location" (:url map-data) "Set-Cookie" (:cookie map-data)}
		  {"Location" (:url map-data)})]
    {:status 302
     :headers headers
     :body ""}))
      
