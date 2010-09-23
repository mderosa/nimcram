(ns pelrapeire.controllers.projects.uid.tasks-ctrl-test
  (:use clojure.test
	pelrapeire.controllers.projects.uid.tasks-ctrl))

(deftest test-run-create-task []
  (testing "inbound data should be augmented with type and progress info"
    (let [rslt (run-create-task (fn [x] x) 
				{"title" "a title"
				 "project" "projectA"
				 "specification" "a specification"
				 "deliversUserFunctionality" "false"})]
      (is (not (nil? (rslt "type"))))
      (is (not (nil? (rslt "progress"))))
      (is (= false (rslt "deliversUserFunctionality"))))))
