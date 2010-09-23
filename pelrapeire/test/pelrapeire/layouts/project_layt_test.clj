(ns pelrapeire.layouts.project-layt-test
  (:use pelrapeire.layouts.project-layt
	clojure.test))

(deftest test-linkin-scripts1
  (testing "we should create several links if the js for a page is an array"
    (is (= 1 2))))

(deftest test-linkin-scripts2
  (testing "we should create one link on the page is the page js is a string"
    (let [actual (linkin-scripts "/home.js")]
      (is (= [:script {:src "/home.js"}] actual)))))