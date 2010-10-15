(ns pelrapeire.app.specification.project-test
  (:use pelrapeire.app.specification.project
	clojure.test))

(deftest create-project-test 
  (testing "a non empty contributors value should be available, as a project always has at least
one contributor"
    (is (thrown? AssertionError (create-project {})))
    (is (thrown? AssertionError (create-project {"contributors" []})))))

(deftest create-project-test2
  (testing "we can handle a create for a single contributor"
    (let [actual (create-project {"contributors" "marc@comp.com"})]
      (is (vector? (actual "contributors")))
      (is (= 1 (count (actual "contributors"))))
      (is (= "marc@comp.com" (first (actual "contributors")))))))

(deftest create-project-test3
  (testing "we can handle multiple contributors passed as an array"
    (let [actual (create-project {"contributors" ["marc@comp.com" "minmin@comp.com"]})]
      (is (vector? (actual "contributors")))
      (is (= 2 (count (actual "contributors")))))))