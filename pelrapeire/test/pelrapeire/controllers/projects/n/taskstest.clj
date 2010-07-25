(ns pelrapeire.controllers.projects.n.taskstest
  (:use clojure.test
	pelrapeire.controllers.projects.n.tasks))

(deftest test-run-create-task []
  (testing "inbound data should be augmented with type and progress info"
    (let [rslt (run-create-task (fn [x] x) 
				{"title" "a title" 
				 "specification" "a specification"
				 "delivers-user-functionality" "false"})]
      (is (not (nil? (rslt "type"))))
      (is (not (nil? (rslt "progress"))))
      (is (= false (rslt "delivers-user-functionality"))))))

