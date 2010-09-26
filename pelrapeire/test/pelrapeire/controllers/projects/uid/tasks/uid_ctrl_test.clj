(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl-test
  (:use clojure.test
	pelrapeire.controllers.projects.uid.tasks.uid-ctrl))

(deftest test-run-update-progress1
  (testing "that when we are sending _id _rev progress and taskStartDate params to the db"
    (let [actual (run-update-progress (fn [x y] x) {"_id" "688e77149276420e36d6206d142e71a2" 
						    "_rev" "6-0653ce615f141ebff9cccca7fd5df1db" 
						    "originalProgress" "proposed"
						    "progress" "in-progress"})]
      (is (actual "_id"))
      (is (actual "_rev"))
      (is (actual "progress"))
      (is (actual "taskStartDate"))
      (is (= 6 (count (actual "taskStartDate")))))))

(deftest test-run-update-progress2
  (testing "should have a precondition that _id _rev and progress enum exist and valid"
    (is (thrown? AssertionError (run-update-progress (fn [x y] x) {:one 1 :two 2})))))

(deftest test-unknow-actions-throw-errors
  (testing "any action parameter that is not expected will cause a exception to be thrown"
    (is (thrown? IllegalArgumentException (run 1 1 1 {"action" "unknown-req"})))))

(deftest test-run-update-all 
  (testing "just run a normal input"
    (let [actual (run-update-all (fn [x y] x) {"_id" "96f8f20ae415eab09b962332c188a8c0"
					     "_rev" "2-77d0f3aca83127bfc2b7a76d7858dff4"
					     "project-uid" "PicoMinMin"
					     "task-uid" "96f8f20ae415eab09b962332c188a8c0"
					     "title" "a title"
					     "deliversUserFunctionality" "true"
					     "specification" "spec"})]
	  (is (not (nil? (actual "_id"))))
	  (is (nil? (actual "project-uid")))
	  (is (nil? (actual "task-uid")))
	  (is (actual "deliversUserFunctionality")))))

(deftest test-update-all-progress
  (testing "test that one can not update progress unless one adds an action parameter"
    (let [actual (run-update-all (fn [x y] x) {"_id" "96f8f20ae415eab09b962332c188a8c0"
					     "_rev" "2-77d0f3aca83127bfc2b7a76d7858dff4"
					     "project-uid" "PicoMinMin"
					     "task-uid" "96f8f20ae415eab09b962332c188a8c0"
					     "progress" "delivered"
					     "title" "a title"
					     "deliversUserFunctionality" "true"
					     "specification" "spec"})]
      (is (nil? (actual "progress"))))))

(deftest test-run-update-progress-proposed-to-delivered
  (testing "we should be able to move tasks directly from proposed to delivered"
    (let [actual (run-update-progress (fn [x y] x) {"_id" "96f8f20ae415eab09b962332c188a8c0"
						    "_rev" "2-77d0f3aca83127bfc2b7a76d7858dff4"
						    "project-uid" "PicoMinMin"
						    "task-uid" "96f8f20ae415eab09b962332c188a8c0"
						    "originalProgress" "proposed"
						    "progress" "delivered"})]
      (is (= "delivered" (actual "progress")))
      (is (not (nil? (actual "taskStartDate"))))
      (is (not (nil? (actual "taskCompleteDate")))))))

(deftest test-run-update-progress-delivered-to-proposed
  (testing "we should be able to move stuff from delivered to proposed but I wont reset the start date"
    (let [actual (run-update-progress (fn [x y] x) {"_id" "96f8f20ae415eab09b962332c188a8c0"
						    "_rev" "2-77d0f3aca83127bfc2b7a76d7858dff4"
						    "project-uid" "PicoMinMin"
						    "task-uid" "96f8f20ae415eab09b962332c188a8c0"
						    "originalProgress" "delivered"
						    "progress" "proposed"})]
      (is (nil? actual)))))
