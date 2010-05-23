(ns pelrapeire.core
  (:use compojure.core 
	ring.adapter.jetty 
	hiccup.core
	pelrapeire.pages.pagedefinition
	clojure.contrib.debug)
  (:import (org.mortbay.jetty.handler ResourceHandler HandlerList)
	   (org.mortbay.jetty Server)))

(defn result []
    (html-doc "Result"
	      [:div "got here"]))

(defn maps-to [pageDef]
  ((pageDef :layout) pageDef))

(defroutes main-routes
  (GET "/projects/10/home" []
       (maps-to pelrapeire.pages.pagedefinition/project-home))
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
