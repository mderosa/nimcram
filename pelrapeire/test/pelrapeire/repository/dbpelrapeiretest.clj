
(ns pelrapeire.repository.dbpelrapeiretest
  (:use pelrapeire.repository.dbpelrapeire
	clojure.test))

(deftest test-active-project-tasks-precondition1
  (testing "a null input should fail the precondition assertion"
    (is (thrown? AssertionError (active-project-tasks "")))))

(deftest test-active-project-tasks-precondition2
  (testing "a null input should fail the precondition assertion"
    (is (thrown? AssertionError (active-project-tasks nil)))))