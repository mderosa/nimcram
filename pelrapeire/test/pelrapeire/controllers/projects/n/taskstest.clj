(ns pelrapeire.controllers.projects.n.taskstest
  (:use clojure.test
	pelrapeire.controllers.projects.n.tasks))

(deftest test-run []
  (testing "inbound data should be augmented with type and progress info"
    (let [rslt (run (fn [x] x) 
		    {"title" "a title" 
		     "specification" "a specification"
		     "delivers-user-functionality" "false"})]
      (is (not (nil? ((:content rslt) "type"))))
      (is (not (nil? ((:content rslt) "progress"))))
      (is (= false ((:content rslt) "delivers-user-functionality"))))))