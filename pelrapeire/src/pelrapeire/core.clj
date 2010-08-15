(ns pelrapeire.core
  (:use compojure.core 
	ring.adapter.jetty 
	hiccup.core
	pelrapeire.controllerdefs
	pelrapeire.views.viewdefs
	pelrapeire.layouts.layoutdefs
	ring.middleware.reload
	clojure.contrib.debug
	clojure.contrib.trace)
  (:import (org.mortbay.jetty.handler ResourceHandler HandlerList)
	   (org.mortbay.jetty Server)))

(defn direct-to [fn-controller any-data]
  (let [cntr-ret-data (trace (fn-controller any-data))
	fn-view (let [pages-key (:view cntr-ret-data)
		      pages-fn (do (assert pages-key)
				   (trace (pages-key pages)))]
		  (do (assert pages-fn)
		      pages-fn))
	fn-layout (let [layout-key (:layout cntr-ret-data)]
		    (if (nil? layout-key)
		      nil
		      (layout-key layouts)))
	vw-ret-data (fn-view cntr-ret-data)]
    (if (nil? fn-layout)
      vw-ret-data
      (do 
	(let [vw-ret-data (fn-view cntr-ret-data)]
	  (assert (. vw-ret-data containsKey :js))
	  (assert (. vw-ret-data containsKey :css))
	  (assert (. vw-ret-data containsKey :title))
	  (assert (. vw-ret-data containsKey :content))
	  (fn-layout vw-ret-data))))))

;;below we have defined a set of handlers functions formed by get / post whatever.  We will pass these
;;functions a request map which is formed from elements in the req. but we do not pass the request 
;;object itself nor does it seem that the handler has access to this function
;;strictly speaking we would like our handlers to return a response map as they take a request
;;map {:status ? :headers ? :body ?}
(defroutes main-routes
  (GET "/index" {params :params :as req}
	(direct-to (:index controllers) params))
  (POST "/login" {params :params :as req}
	(direct-to (:login controllers) params))
  (GET "/projects/:project-name/home" {params :params :as req}
       (direct-to (:projects-uid-home controllers) params))
  (POST "/projects/:project/tasks" {params :params :as req}
	(direct-to (:projects-uid-tasks controllers) params))
  (GET "/projects/:project-uid/tasks/:task-uid" {params :params :as req}
       (direct-to (:projects-uid-tasks-uid controllers) params))
  (GET "/users/:user-id/projects" {params :params}
	 (trace (direct-to (:users-uid-projects controllers) params)))
  (ANY "*" []
       {:status 404 :body "<h1>page not found</h1>"}))

(def app
     (-> #'main-routes
	 (wrap-reload '[pelrapeire.core])))

(defn start [] 
  (let [resource-handler (doto (ResourceHandler.) 
			 (.setResourceBase "."))
	request-handler (proxy-handler (var app))
	handler-list (doto (HandlerList.)
		      (.addHandler resource-handler)
		      (.addHandler request-handler))
       #^Server s (create-server (dissoc {:port 8080} :configurator))]
    (doto s
      (.setHandler handler-list)
      (.start)
      (.join))))
