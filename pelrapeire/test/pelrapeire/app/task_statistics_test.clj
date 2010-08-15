(ns pelrapeire.app.task-statistics-test
  (:use pelrapeire.app.task-statistics
	clojure.test))

(deftest test-days-in-progress1
  (testing "for tasks that are in-progress the days of activity are measured 
from the start date to today"
    (is (< 5 (days-in-progress {"progress" "in-progress"
				"task-start-date" [2010,7,7,10,23,1]
				"task-complete-date" nil})))))

(deftest test-days-of-activity-waiting
  (testing "if a task is waiting then it should always have 0 for its days in progress"
    (is (= 0 (days-in-progress {"progress" "proposed"
				"task-start-date" nil
				"task-complete-date" nil})))))

(deftest test-days-of-activity-delivered
  (testing "a task that has been delivered will have a days in progress that is 
equal to the number of days between start date and end date"
    (is (= 0 (days-in-progress {"progress" "delivered"
				"task-start-date" [2010,7,7,13,12,11]
				"task-complete-date" [2010,7,8,9,8,7]})))
    (is (= 0 (days-in-progress {"progress" "delivered"
				"task-start-date" [2010,7,7,13,12,11]
				"task-complete-date" [2010,7,7,14,7,6]})))))
