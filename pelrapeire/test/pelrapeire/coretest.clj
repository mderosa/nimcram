(ns pelrapeire.coretest
  (:use pelrapeire.core
	pelrapeire.pages.pages
	pelrapeire.layouts.layouts
	hiccup.core
	clojure.test))

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
      (is (= "<div>null</div>" (html (direct-to mock-run "-")))))))

(deftest test-direct-to-controller-asserts
  (testing "we should throw asserts if the controller returns without 
specifying a :view or :layout"
    (binding [pages (assoc pages :mock-show mock-show) 
	      layouts (assoc layouts :mock-render mock-render)]
      (is (thrown? AssertionError (html (direct-to bad-mock-run "-")))))))

(deftest test-direct-to-page-asserts
  (testing "a page should send back information on :title :js :css :content. these
are required fields"
    (binding [pages (assoc pages :mock-show bad-mock-show) 
	      layouts (assoc layouts :mock-render mock-render)]
      (is (thrown? AssertionError (html (direct-to mock-run "-")))))))
