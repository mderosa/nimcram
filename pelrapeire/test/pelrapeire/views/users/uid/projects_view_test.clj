(ns pelrapeire.views.users.uid.projects-view-test
  (:use pelrapeire.views.users.uid.projects-view
	clojure.test))

(deftest test-make-project-links 
  (testing "one input gives on one link output"
    (is (= '([:a {:href "/projects/test/home"} "test"])
	   (make-project-links ["test"])))))
