(ns pelrapeire.pages.users.n.projectstest
  (:use pelrapeire.pages.users.n.projects
	clojure.test))

(deftest test-make-project-links 
  (testing "one input gives on one link output"
    (is (= '([:a {:href "/projects/test/home"} "test"])
	   (make-project-links ["test"])))))