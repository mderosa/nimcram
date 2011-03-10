(ns pelrapeire.core
  (:use compojure.core 
	pelrapeire.jetty 
	hiccup.core
	pelrapeire.app.context
	pelrapeire.controllerdefs
	pelrapeire.views.viewdefs
	pelrapeire.layouts.layoutdefs
	ring.middleware.reload
	clojure.contrib.debug
	clojure.contrib.trace)
  (:import (org.mortbay.jetty.handler ResourceHandler HandlerList)
	   (org.mortbay.jetty Server)))

(defn direct-to [fn-controller req]
  (let [context (build-context req)
	cntr-ret-data (fn-controller (merge req {:context context}))
	fn-view (let [pages-key (:view cntr-ret-data)
		      pages-fn (do (assert pages-key)
				   (pages-key pages))]
		  (do (assert pages-fn)
		      pages-fn))
	fn-layout (let [layout-key (:layout cntr-ret-data)]
		    (if (nil? layout-key)
		      nil
		      (layout-key layouts)))
	vw-ret-data (fn-view (merge cntr-ret-data {:context context}))]
    (if (nil? fn-layout)
      vw-ret-data
      (do 
	(assert (. vw-ret-data containsKey :js))
	(assert (. vw-ret-data containsKey :css))
	(assert (. vw-ret-data containsKey :title))
	(assert (. vw-ret-data containsKey :content))
	(fn-layout (merge vw-ret-data {:context context}))))))

;;below we have defined a set of handlers functions formed by get / post whatever.  We will pass these
;;functions a request map which is formed from elements in the req. but we do not pass the request 
;;object itself nor does it seem that the handler has access to this function
;;strictly speaking we would like our handlers to return a response map as they take a request
;;map {:status ? :headers ? :body ?}
(defroutes main-routes
  (GET "/index" {params :params :as req}
       (direct-to (:index controllers) req))
  (GET "/about" req
       (direct-to (:about controllers) req))
  (GET "/contactus" req
       (direct-to (:contactus controllers) req))
  (POST "/login" req
	(direct-to (:login controllers) req))
  (POST "/mail/admin" req
	(direct-to (:mail-admin controllers) req))
  (POST "/projects/new" req
	(direct-to (:projects-new controllers) req))
  (GET "/projects/:project-uid/home" req
       (direct-to (:projects-uid-home controllers) req))
  (POST "/projects/:project-uid/tasks" req
	(direct-to (:projects-uid-tasks controllers) req))
  (GET "/projects/:project-uid/tasks/:task-uid" req
       (direct-to (:projects-uid-tasks-uid controllers) req))
  (POST "/projects/:project-uid/tasks/:task-uid" req
	(direct-to (:projects-uid-tasks-uid controllers) req))
  (DELETE "/projects/:project-uid/tasks/:task-uid" req
	  (direct-to (:projects-uid-tasks-uid controllers) req))
  (POST "/users" req
	(direct-to (:users controllers) req))
  (GET "/users/new" req
       (direct-to (:users-new controllers) req))
  (GET "/users/:user-id/projects" req
       (direct-to (:users-uid-projects controllers) req))
  (POST "/users/:user-id/projects" req
	(direct-to (:users-uid-projects controllers) req))
  (GET "/users/:user-uid/projects/:project-uid/home" req
	(direct-to (:users-uid-projects-uid-home controllers) req))
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
       #^Server s (create-server (dissoc {:port 8081} :configurator))]
    (doto s
      (.setHandler handler-list)
      (.start)
      (.join))))
