(ns pelrapeire.tiles
  (:use hiccup.core))
      
(defn header []
  [:img {:src "/img/logoEbay_x45.gif"}])

(defn footer []
  [:div
   [:a {:href "http://pages.ebay.com/aboutebay.html"} "About eBay"]
   [:a {:href "http://www2.ebay.com/aw/marketing.shtml"} "Announcements"]
   [:a {:href "http://hub.shop.ebay.com/"} "Buy Hub"]
   [:a {:href "http://pages.ebay.com/securitycenter/index.html"} "Security Center"]
   [:a {:href "http://resolutioncenter.ebay.com"} "Resolution Center"]
   [:a {:href "http://pages.ebay.com/buy/toolsf.html"} "Buyer Tools"]
   [:a {:href "http://pages.ebay.com/help/policies/overview.html" :rel "nofollow"} "Policies"]
   [:a {:href "http://www.ebaymainstreet.com"} "Government Relations"]
   [:a {:href "http://pages.ebay.com/sitemap.html"} "Site Map"]
   [:a {:href "http://pages.ebay.com/help/index.html"} "Help"]])

(defn project-tile [title content]
  (html 
   [:html
    [:head
     [:title title]]
    [:body
     [:div {:class "header"} (header)]
     [:div {:class "content"} (content)]
     [:div {:class "footer"} (footer)]]]))