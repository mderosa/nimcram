(ns pelrapeire.layouts.minimallayout
  (:use hiccup.core
	clojure.contrib.json.write))

(defn header []
  [:div
   [:img {:src "/img/logoEbay_x45.gif"}]
   [:span {:style "padding-bottom:5px;font-size:20px"} "Hokulea"
    [:sup "beta"]]
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
   [:a {:href "http://pages.ebay.com/aboutebay.html"} "About eBay"]
   [:a {:href "http://www2.ebay.com/aw/marketing.shtml"} "Announcements"]
   [:a {:href "http://pages.ebay.com/sitemap.html"} "Site Map"]
   [:a {:href "http://pages.ebay.com/help/index.html"} "Help"]])

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
     [:div {:class "header"} (header)]
     [:div {:class "content"} (:content map-data)]
     [:div {:class "footer"} (footer)]]]))