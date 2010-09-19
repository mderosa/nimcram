(ns pelrapeire.controllers.projects.uid.tasks.uid-ctrl-test
  (:use clojure.test
	pelrapeire.controllers.projects.uid.tasks.uid-ctrl))

(deftest test-run-update-progress1
  (testing "that when we are sending _id _rev progress and taskStartDate params to the db"
    (let [actual (run-update-progress (fn [x y] x) {"_id" "688e77149276420e36d6206d142e71a2" 
						    "_rev" "6-0653ce615f141ebff9cccca7fd5df1db" 
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
