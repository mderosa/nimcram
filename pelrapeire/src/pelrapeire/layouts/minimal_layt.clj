(ns pelrapeire.layouts.minimal-layt
  (:use hiccup.core
	clojure.contrib.trace
	clojure.contrib.json))

(defn header [map-data]
  [:div
   [:a {:href "/index"} [:img {:src "/img/logoHokulea.png"}]]
   [:div {:class "gh-col"} 
    [:b {:class "gh-c1"}]
    [:b {:class "gh-c2"}]
    [:b {:class "gh-c3"}]
    [:b {:class "gh-c4"}]
    [:b {:class "gh-c5"}]
    [:b {:class "gh-c6"}]
    [:b {:class "gh-c7"}]
    [:div {:class "gh-clr"}]]])

(defn footer []
  [:div
   [:a {:href "/about"} "Whats and Whys"]
   [:a {:href "/contactus"} "Contact Us"]])

(defn
  #^{:doc "takes a map of the form {:content Node :js String :css String :title String} and
surrounds the node with a layout"}
  render [map-data] 
  (html 
   [:html
    [:head
     [:meta {:content "text/html;charset=UTF-8" :http-equiv "Content Type"}]
     [:link {:href "/js/yui/build/cssreset/reset.css" :type "text/css" :rel "stylesheet"}]     
     [:link {:href "/js/yui/build/cssfonts/fonts.css" :type "text/css" :rel "stylesheet"}]
     [:link {:href "/js/yui/build/cssgrids/grids.css" :type "text/css" :rel "stylesheet"}]
     [:link {:href "/css/pelrapeire.css" :type "text/css" :rel "stylesheet"}]
     [:script {:src "/js/yui/build/yui/yui-debug.js"}]
     [:script {:src (:js map-data)}]
     [:title (:title map-data)]]
    [:body
     [:div {:class "header"} (header map-data)]
     [:div {:class "content"} (:content map-data)]
     [:div {:class "footer"} (footer)]]]))
