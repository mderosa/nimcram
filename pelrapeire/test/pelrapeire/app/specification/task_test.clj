(ns pelrapeire.app.specification.task-test
  (:use pelrapeire.app.specification.task
	clojure.test))

(deftest test-create1
  (testing "we should not accept data without a title at a minimum"
    (is (thrown? AssertionError (create-task {"one" 1 "two" 2})))))

(deftest test-create2
  (testing "specification may come through as a '' so we should change this to nil"
    (let [actual (create-task {"title" "a title" "specification" "" "project" "a project"})]
      (is (nil? (actual "specification"))))))

(deftest test-create3
  (testing "project should always be a string of len > 0"
      (is (thrown? AssertionError (create-task {"title" "a title" 
			       "specification" "some notes" 
			       "project" nil})))))

(deftest test-create4
  (testing "project should always be a string of len > 0"
      (is (thrown? AssertionError (create-task {"title" "a title" 
			       "specification" "some notes"})))))

(deftest test-create5
  (testing "a task should always be created with a creation date by default"
    (let [actual (create-task {"title" "a title" 
			       "specification" "some notes" 
			       "project" "project name"})]
      (is (= "project name" (actual "project")))
      (is (= "some notes" (actual "specification")))
      (is (= 6 (count (actual "taskCreateDate")))))))

(deftest test-create6
  (testing "deliversUserFunctionality can come through as a string we would
like for it to be converted to a boolean"
    (let [actual (create-task {"title" "a title" 
			       "specification" "some notes" 
			       "project" "project name"
			       "deliversUserFunctionality" "true"})]
      (is (= true (actual "deliversUserFunctionality"))))))


