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

(deftest test-empty-namespace
  (testing "an empty namespace should be conditioned to nil"
    (let [actual (ns-string-to-map "")]
    (is (nil? actual)))))

(deftest test-malformed-namespace
  (testing "a namespace that is malformed should be ingnored"
    (let [actual (ns-string-to-map " = ")]
    (is (nil? actual)))))

(deftest test-condition-task-wsp
  (testing "we should remove any whitespace before when we condition task data"
    (let [actual (condition-task {"_id" "a5ce4479660b9334deeb288eb5166f60"
				  "_rev" "3-6db398380e045d2b6d208cddeca84979"
				  "title" " this is a title "
				  "deliversUserFunctionality" "false"
				  "specification" " a spec "
				  "project" " a project"})]
      (is (= (actual "_id") "a5ce4479660b9334deeb288eb5166f60"))
      (is (= (actual "_rev") "3-6db398380e045d2b6d208cddeca84979"))
      (is (= (actual "title") "this is a title"))
      (is (= (actual "specification") "a spec"))
      (is (= (actual "project" " a project"))))))

(deftest test-condition-ns-info
  (testing "test that we do namespace conversions properly"
    (let [actual (condition-task {"_id" "a5ce4479660b9334deeb288eb5166f60"
				  "_rev" "3-6db398380e045d2b6d208cddeca84979"
				  "title" "a title"
				  "project" "a project"
				  "namespace" ["project=build" "badnamespace"]
				  "deliversUserFunctionality" "true"})]
      (is (= (actual "namespace") [{"project" "build"}]))
      (is (= (actual "deliversUserFunctionality") true)))))

(deftest test-condition-task-unknown-keys
  (testing "in general inbound requests will probably have a bit of extra information
in them if we have something that is not part of the specification then make sure
that (condition-task) will ignore that key"
    (let [actual (condition-task {"_id" "a5ce4479660b9334deeb288eb5166f60"
				  "_rev" "3-6db398380e045d2b6d208cddeca84979"
				  "title" "a title"
				  "firefly" "yo baby"
				  "project" "a project"
				  "namespace" ["project=build" "badnamespace"]
				  "deliversUserFunctionality" "true"})]
      (is (nil? (actual "firefly"))))))

(deftest test-condition-task-priority-update
  (testing "we should be able to do a standard priority update"
    (let [actual (condition-task {"project-uid" "PicoMinMin", 
				  "task-uid" "f7085ddc838e5a8730b76d882cb70069", 
				  "priority" "1", 
				  "progress" "proposed",
				  "_rev" "1-81d6316a992f21cbc69a4714b46f9ac7", 
				  "_id" "f7085ddc838e5a8730b76d882cb70069"})]
      (is (= "proposed" (actual "progress")))
      (is (= 1 (actual "priority")))
      (is (nil? (actual "project-uid"))))))

(deftest test-condition-priority
  (testing "we should only accept the values of nil '1' '2' '3'"
    (is (thrown? AssertionError (condition-priority 5)))))

(deftest test-condition-priority-conversion
  (testing "priority comes in as a string as has be be converted to a number"
    (is (= 1 (condition-priority "1")))))

(deftest test-condition-progress
  (testing "we should only accept the values of 'proposed' 'in-progress' 'delivered'"
    (is (thrown? AssertionError (condition-progress "yo")))))

(deftest test-unknown-variable-filtering
  (testing "we should not write non schema variable into a task object"
    (let [actual (create-task {"title" "a title"
			       "project" "a project"
			       "aa" "a"
			       "bb" "b"
			       "project-uid" "a project"})]
      (is (nil? (actual "project-uid")))
      (is (nil? (actual "aa"))))))
      