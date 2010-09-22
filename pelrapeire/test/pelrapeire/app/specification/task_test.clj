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

(deftest test-create7
  (testing "both title a project should be supplied and be non empty"
    (is (thrown? AssertionError (create-task {"title" "  "
			       "project" "   "
			       "specification" "some specification"
			       "deliversUserFunctionality" "true"})))))

(deftest test-create-with-namespace-info1
  (testing "namespace infomation will come in as '', 'something=something', or
as an array. when '' namespace should be an empty array"
    (let [actual (create-task {"title" "a title" 
			       "namespace" "" 
			       "project" "project name"
			       "deliversUserFunctionality" ""})]
      (is (= [] (actual "namespace")))
      (is (= false (actual "deliversUserFunctionality"))))))

(deftest test-create-with-namespace-info2
  (testing "namespace infomation will come in as '', 'something=something', or
as an array. when 'x=y' namespace should be [{x y}]"
    (let [actual (create-task {"title" "a title" 
			       "namespace" "project=level1.level2" 
			       "project" "project name"
			       "deliversUserFunctionality" ""})]
      (is (= [{"project" "level1.level2"}] (actual "namespace")))
      (is (= false (actual "deliversUserFunctionality"))))))

(deftest test-create-with-namespace-info3
  (testing "namespace infomation will come in as '', 'something=something', or
as an array. when 'xxxx' the namespace submission is invalid and should be []"
    (let [actual (create-task {"title" "a title" 
			       "namespace" "dkdkdkd" 
			       "project" "project name"
			       "deliversUserFunctionality" ""})]
      (is (= [] (actual "namespace")))
      (is (= false (actual "deliversUserFunctionality"))))))

(deftest test-create-with-namespace-info4
  (testing "namespace infomation will come in as '', 'something=something', or
as an array. when multiple namespaces come in the parameter list they will be 
sparated out and placed in maps"
    (let [actual (create-task {"title" "a title" 
			       "namespace" ["project=projectA" "release=e663"]
			       "project" "project name"
			       "deliversUserFunctionality" ""})]
      (is (= [{"project" "projectA"} {"release" "e663"}] (actual "namespace"))))))



