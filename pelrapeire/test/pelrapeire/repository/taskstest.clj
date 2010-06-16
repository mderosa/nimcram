
(ns pelrapeire.repository.taskstest
  (:use clojure.test
	pelrapeire.dbconfig
	pelrapeire.repository.tasks))

(deftest test-create-task 
  (let [rst (create-task "{\"name\": \"marc\", \"age\": 2}" db-config-int)]
    (is (. rst contains "\"ok\":true"))
    (is (. rst contains "\"rev\""))))

(deftest test-equals
  (let [one 1
	two 2]
    (is (+ one 1) two)))
