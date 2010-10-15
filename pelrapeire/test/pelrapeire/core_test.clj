(ns pelrapeire.core-test
  (:use pelrapeire.core
	pelrapeire.views.viewdefs
	pelrapeire.layouts.layoutdefs
	hiccup.core
	clojure.test))

(def req 
     {
	:remote-addr "0:0:0:0:0:0:0:1", 
	:scheme :http, 
	:query-params {}, 
	:form-params {"submit" "login", "password" "shazam", "email" "marc.derosa@shazam.com"}, 
	:request-method :post, 
	:query-string nil, 
	:route-params {}, 
	:content-type "application/x-www-form-urlencoded", 
	:cookies {"Hokulea" {:value "user"}}, 
	:uri "/login", 
	:server-name "localhost", 
	:params {"submit" "login", "password" "shazam", "email" "marc.derosa@shazam.com"}, 
	:headers {
		"cookie" "Hokulea=user-uid/bc5287c66bc3acf02b958d6681390f3f",
		"user-agent" "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10, keep-alive 115", 
		"accept-charset" "ISO-8859-1,utf-8;q=0.7,*;q=0.7", 
		"accept" "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", 
		"host" "localhost:8080", 
		"referer" "http://localhost:8080/index", 
		"content-type" "application/x-www-form-urlencoded", 
		"accept-encoding" "gzip,deflate", 
		"content-length" 58, 
		"accept-language" "en-us,en;q=0.5", 
		"connection" "keep-alive"}, 
	:content-length 58, 
	:server-port 8080, 
	:character-encoding nil, 
	:body nil})

(defn 
  #^{:doc "a mock controller run function"}
  mock-run [#^String input]
  {:view :mock-show :layout :mock-render})
(defn bad-mock-run [input]
  {:layout :mock-render})

(defn 
  #^{:doc "a mock layout render function"}
  mock-render [map-data]
  (:content map-data))

(defn 
  #^{:doc "a mock view show function"}
  mock-show [map-data]
  {:title "null page" :js nil :css nil :content [:div "null"]})
(defn 
  #^{:doc "a mock view show function"}
  bad-mock-show [map-data]
  {:title "null page" :js nil :content [:div "null"]})

(deftest test-direct-to
  (testing "direct-to() should coordinate the the threading of information through
the controller, view, layout returning a node"
    (binding [pages (assoc pages :mock-show mock-show) 
	      layouts (assoc layouts :mock-render mock-render)]
      (is (= "<div>null</div>" (html (direct-to mock-run req)))))))

(deftest test-direct-to-controller-asserts
  (testing "we should throw asserts if the controller returns without 
specifying a :view or :layout"
    (binding [pages (assoc pages :mock-show mock-show) 
	      layouts (assoc layouts :mock-render mock-render)]
      (is (thrown? AssertionError (html (direct-to bad-mock-run req)))))))

(deftest test-direct-to-page-asserts
  (testing "a page should send back information on :title :js :css :content. these
are required fields"
    (binding [pages (assoc pages :mock-show bad-mock-show) 
	      layouts (assoc layouts :mock-render mock-render)]
      (is (thrown? AssertionError (html (direct-to mock-run req)))))))


(defn mock-ajax-controller [params] 
  {:view :json-view
     :layout :json-layout
     :content {"id" "1" "rev" "1_1" "title" "a title" }})
(deftest test-direct-to-for-ajax
  (testing "an ajax call should return an ajax string"
    (let [rslt (direct-to mock-ajax-controller req)]
      (is (string? rslt))
      (is (= "{\"rev\":\"1_1\",\"title\":\"a title\",\"id\":\"1\"}" rslt)))))
