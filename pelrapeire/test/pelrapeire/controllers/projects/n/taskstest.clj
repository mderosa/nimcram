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

(deftest test-run-update-progress1
  (testing "that when we are sending _id _rev progress and task-start-date params to the db"
    (let [actual (run-update-progress (fn [x y] x) {"_id" "11" "_rev" "22" "progress" "in-progress"})]
      (is (actual "_id"))
      (is (actual "_rev"))
      (is (actual "progress"))
      (is (actual "task-start-date"))
      (is (= 6 (count (actual "task-start-date")))))))
	

(deftest test-run-update-progress2
  (testing "should have a precondition that _id _rev and progress enum exist and valid"
    (is (thrown? AssertionError (run-update-progress (fn [x y] x) {:one 1 :two 2})))))