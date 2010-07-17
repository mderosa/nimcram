(ns pelrapeire.pages.projects.n.hometest
  (:use pelrapeire.pages.projects.n.home
	clojure.test))

(def test-task 
     {"task-start-date" [2010 7 6 21 10 0], 
      "specification" "should be queryable so that give an project name we can obtain all the tasks for that project", 
      "_rev" "3-f0c26eae64651811fffd4a1bf534ea23", 
      "delivers-user-functionality" true, 
      "progress" "delivered", 
      "solution-team" ["marc.derosa@gmail.com"], 
      "title" "create project task view", 
      "project" "PicoMinMin", 
      "task-complete-date" [2010 7 6 22 21 0], 
      "_id" "70ab59880b829b4ad4fbd56d6a068da1", 
      "type" "task"})

(deftest test-make-tasks-none
  (testing "we should return an empty vectory when there are no tasks in the 
task list that satisfy the progress designation"
    (let [actual (make-tasks [test-task] "waiting")]
      (is (= [] actual)))))

(deftest test-make-tasks-one
  (testing "we should return an non empty vector of tables when there are tasks in the 
task list that satisfy the progress designation"
    (let [actual (make-tasks [test-task] "delivered")]
      (is (= 1 (.size actual)))
      (is (= :table (first (first actual)))))))



