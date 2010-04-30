(ns pelrapeire.core
  (:use compojure.core 
	ring.adapter.jetty 
	hiccup.core
	pelrapeire.tiles
	pelrapeire.pages.project.activity
	clojure.contrib.debug)
  (:import (org.mortbay.jetty.handler ResourceHandler HandlerList)
	   (org.mortbay.jetty Server)))

(defn html-doc [title & body]
  (html
   [:html
    [:head
     [:title title]]
    [:body
     [:div
      [:h2
       [:a {:href "/init"} "Home"]]]
     body]]))

(def sum-form
     (html-doc "Sum"
	       [:form {:method "post" :action "/doit"}
			[:input {:type "text" :size 3 :name "x"}]
			"+"
			[:input {:type "text" :size 3 :name "y"}]
			[:input {:type "submit" :value "="}]]))

(defn result []
    (html-doc "Result"
	      [:div "got here"]))

(defroutes main-routes
  (GET "/projects/10/activity" []
;;       (debug-repl)
       (project-tile "current project activity" show-current-activity))
  (POST "/doit" []
	(result))
  (ANY "*" []
       {:status 404 :body "<h1>page not found</h1>"}))

(defn start [] 
  (let [resource-handler (doto (ResourceHandler.) 
			 (.setResourceBase "."))
	request-handler (proxy-handler main-routes)
	handler-list (doto (HandlerList.)
		      (.addHandler resource-handler)
		      (.addHandler request-handler))
       #^Server s (create-server (dissoc {:port 8080} :configurator))]
    (doto s
      (.setHandler handler-list)
      (.start)
      (.join))))
