(ns pelrapeire.views.projects.uid.home-view-test
  (:use pelrapeire.views.projects.uid.home-view
	clojure.contrib.trace
	clojure.test))

(def test-task 
     {"task-start-date" [2010 7 6 21 10 0], 
      "specification" "should be queryable so that give an project name we can obtain all the tasks for that project", 
      "_rev" "3-f0c26eae64651811fffd4a1bf534ea23", 
      "deliversUserFunctionality" true, 
      "progress" "delivered", 
      "solutionTeam" ["marc.derosa@gmail.com"], 
      "title" "create project task view", 
      "project" "PicoMinMin", 
      "task-complete-date" [2010 7 6 22 21 0], 
      "_id" "70ab59880b829b4ad4fbd56d6a068da1", 
      "type" "task"})

(deftest test-make-tasks-none
  (testing "we should return an empty vector when there are no tasks in the 
task list that satisfy the progress designation"
    (let [actual (make-tasks [test-task] "proposed")]
      (is (= [] actual)))))

(deftest test-make-tasks-one
  (testing "we should return an non empty vector of tables when there are tasks in the 
task list that satisfy the progress designation"
    (let [actual (make-tasks [test-task] "delivered")]
      (is (= 1 (.size actual)))
      (is (= :table (first (first actual)))))))

(deftest test-make-priority-display1
  (testing "a nil value of 'priority' should show 3 off stars"
    (let [actual (trace (make-priority-display {"_id" "3dkiee33"}))]
      (is (= :td (first actual)))
      (is (= "/img/star-off.gif" (:src (nth (nth actual 2) 1))))
      (is (= 5 (count actual))))))

(deftest test-make-priority-display2
  (testing "a priority value of 2 should display 2 on and 1 off star, in that order"
    (let [actual (trace (make-priority-display {"priority" 2}))]
      (is (= "/img/star-on.gif" (:src (nth (nth actual 3) 1))))
      (is (= "/img/star-off.gif" (:src (nth (nth actual 4) 1)))))))

(deftest test-make-priority-display3
  (testing "values less than 1 and greater than 3 are not allowed"
    (is (thrown? IllegalArgumentException (trace (make-priority-display {"priority" 5}))))))

