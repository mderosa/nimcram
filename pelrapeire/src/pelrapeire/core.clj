(ns pelrapeire.core
  (:use compojure.core 
	ring.adapter.jetty 
	hiccup.core
	pelrapeire.controllerdefs
	pelrapeire.pages.pages
	pelrapeire.layouts.layouts
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

(defn result []
    (html-doc "Result"
	      [:div "got here"]))

(defn direct-to [fn-controller any-data]
  (let [view-data (fn-controller any-data)
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
      (let [layout-data (fn-view view-data)]
	(assert (. layout-data containsKey :js))
	(assert (. layout-data containsKey :css))
	(assert (. layout-data containsKey :title))
	(assert (. layout-data containsKey :content))
	(fn-layout layout-data)))))

(defn maps-to [pageDef]
  ((pageDef :layout) pageDef))

(defroutes main-routes
  (GET "/projects/:project-name/home" [project-name]
       (direct-to (:projects-n-home @controllers) project-name))
  (POST "/doit" []
	(result))
  (GET "/testhello" []
       (direct-to (:test @controllers) "hello"))
  (GET "/testbye" []
       "<div>bye</div>")
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
