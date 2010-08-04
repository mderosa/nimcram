(ns pelrapeire.core
  (:use compojure.core 
	ring.adapter.jetty 
	hiccup.core
	pelrapeire.controllerdefs
	pelrapeire.pages.pages
	pelrapeire.layouts.layouts
	clojure.contrib.debug
	clojure.contrib.trace)
  (:import (org.mortbay.jetty.handler ResourceHandler HandlerList)
	   (org.mortbay.jetty Server)))

(deftrace direct-to [fn-controller any-data]
  (let [view-data (trace (fn-controller any-data))
	fn-view (let [pages-key (:view view-data)
		      pages-fn (do (assert pages-key)
				   (pages-key pages))]
		  (do (assert pages-fn)
		      pages-fn))
	fn-layout (let [layout-key (:layout view-data)
			layout-fn (do (assert layout-key)
				      (layout-key layouts))]
		    (do	(assert layout-fn)
			layout-fn))]
    (do 
      (let [layout-data (trace (fn-view view-data))]
	(assert (. layout-data containsKey :js))
	(assert (. layout-data containsKey :css))
	(assert (. layout-data containsKey :title))
	(assert (. layout-data containsKey :content))
	(trace (fn-layout layout-data))))))

(defroutes main-routes
  (GET "/index" {params :params :as req}
       (direct-to (:index @controllers) params))
  (GET "/projects/:project-name/home" {params :params :as req}
       (direct-to (:projects-n-home @controllers) params))
  (POST "/projects/:project/tasks" {params :params :as req}
	(direct-to (:projects-n-tasks @controllers) params))
  (ANY "*" []
       {:status 404 :body "<h1>page not found</h1>"}))


(defn start [] 
  (let [resource-handler (doto (ResourceHandler.) 
			 (.setResourceBase "."))
	request-handler (proxy-handler (var main-routes))
	handler-list (doto (HandlerList.)
		      (.addHandler resource-handler)
		      (.addHandler request-handler))
       #^Server s (create-server (dissoc {:port 8080} :configurator))]
    (doto s
      (.setHandler handler-list)
      (.start)
      (.join))))
