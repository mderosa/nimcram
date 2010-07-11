(ns pelrapeire.controllers.projects.n.hometest
  (:use pelrapeire.controllers.projects.n.home
	clojure.test))

(def example-task-view-result
     {"rows" [{"value" {"task-start-date" [2010 7 6 21 10 0], "specification" "should be queryable so that give an project name we can obtain all the tasks for that project", "_rev" "3-f0c26eae64651811fffd4a1bf534ea23", "delivers-user-functionality" true, "progress" "deployed", "solution-team" ["marc.derosa@gmail.com"], "title" "create project task view", "project" "PicoMinMin", "task-complete-date" [2010 7 6 22 21 0], "_id" "70ab59880b829b4ad4fbd56d6a068da1", "type" "task"}, "key" ["PicoMinMin" 2010 7 6 22 21 0], "id" "70ab59880b829b4ad4fbd56d6a068da1"}], "offset" 0, "total_rows" 1})

(defn mock-active-tasks [project-name]
  example-task-view-result)

(defn mock-completed-tasks 
  ([project-name]
     example-task-view-result)
  ([project-name cut-off-date]
     example-task-view-result))

(deftest test-run [] 
  (testing "in the standard situation the run function should return a map of the
form {:active (tasks) :completed (tasks)}"
    (let [actual (run mock-active-tasks mock-completed-tasks "PicoMinMin")]
      (is (not (nil? actual)))
      (is (not (nil? (:active actual))))
      (is (not (nil? (:completed actual))))
      (is (> (.size (:active actual)) 0))
      (is (> (.size (:completed actual)) 0))
      (is (= "task" ((first (:active actual)) "type")))
      (is (= "task" ((first (:completed actual)) "type")))
      )))