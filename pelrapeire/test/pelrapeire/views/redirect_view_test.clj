(ns pelrapeire.views.redirect-view-test
  (:use pelrapeire.views.redirect-view
	clojure.test))

(deftest test-cookie-redirect
  (testing "if the input data to the redirect call contains cookie information then we should send this to the response"
    (let [actual (show {:url "/projects" :cookie "foo=bar"})
	  header ((:headers actual) "Set-Cookie")]
      (is (. header contains "foo=bar")))))
