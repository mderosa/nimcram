(ns pelrapeire.tiles
  (:use hiccup.core))
      
(defn header []
  [:div
   [:img {:src "/img/logoEbay_x45.gif"}]
   [:span {:style "padding-bottom:5px;font-size:20px"} "PicoMinMin"]
   [:div {:class "gh-col"} 
    [:b {:class "gh-c1"}]
    [:b {:class "gh-c2"}]
    [:b {:class "gh-c3"}]
    [:b {:class "gh-c4"}]
    [:b {:class "gh-c5"}]
    [:b {:class "gh-c6"}]
    [:b {:class "gh-c7"}]
    [:div {:class "gh-clr"}]]
   [:div {:id "headerWrapper" :class "gh-hbw"}
    [:div {:class "gh-hb"}
     [:div {:class "gh-mn"}
      [:a {:id "BrowseCategories" :href "/projects/10/public/home"} "OUR SPACE"]
      [:a {:id "BrowseCategories" :href "/projects/10/private/home"} "MY SPACE"]
]]]])

(defn footer []
  [:div
   [:a {:href "http://pages.ebay.com/aboutebay.html"} "About eBay"]
   [:a {:href "http://www2.ebay.com/aw/marketing.shtml"} "Announcements"]
   [:a {:href "http://pages.ebay.com/sitemap.html"} "Site Map"]
   [:a {:href "http://pages.ebay.com/help/index.html"} "Help"]])

(defn project-tile [title content script]
  (html 
   [:html
    [:head
     [:meta {:content "text/html;charset=UTF-8" :http-equiv "Content Type"}]
     [:link {:href "/js/yui/build/cssreset/reset.css" :type "text/css" :rel "stylesheet"}]     
     [:link {:href "/js/yui/build/cssfonts/fonts.css" :type "text/css" :rel "stylesheet"}]
     [:link {:href "/js/yui/build/cssgrids/grids.css" :type "text/css" :rel "stylesheet"}]
     [:link {:href "/css/pelrapeire.css" :type "text/css" :rel "stylesheet"}]
     [:script {:src "/js/yui/build/yui/yui.js"}]
     [:script {:src script}]
     [:title title]]
    [:body
     [:div {:class "header"} (header)]
     [:div {:class "content"} (content)]
     [:div {:class "footer"} (footer)]]]))