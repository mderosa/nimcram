(ns pelrapeire.app.validators-test
  (:use pelrapeire.app.validators
	clojure.test))

(deftest test-revision?-false 
  (testing "for a non revision number we should get a false back"
    (is (false? (revision? "undefined")))))

(deftest test-revision?-true
  (testing "for a revision number we should get true"
    (is (revision? "6-11802b52c01f7462f4e6d0fce3fa85ac"))))

(deftest test-revision?-almostcorrect
  (testing "for a revision number we should get true"
    (is (false? (revision? "6-11802z52c01f7462g4e6d0kce3fa85mc")))))

(deftest test-revision?-nil
  (testing "if there is no revision 'nil' passed in then we return false"
    (is (false? (revision? nil)))))

