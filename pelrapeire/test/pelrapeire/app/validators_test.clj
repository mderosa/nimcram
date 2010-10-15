(ns pelrapeire.app.validators-test
  (:use pelrapeire.app.validators
	clojure.test))

(deftest test-revision?-false 
  (testing "for a non revision number we should get a false back"
    (is (false? (revision? "undefined")))))

(deftest test-revision?-true
  (testing "for a revision number we should get true"
    (is (revision? "16-11802b52c01f7462f4e6d0fce3fa85ac"))))

(deftest test-revision?-almostcorrect
  (testing "for a revision number we should get true"
    (is (false? (revision? "62-11802z52c01f7462g4e6d0kce3fa85mc")))))

(deftest test-revision?-nil
  (testing "if there is no revision 'nil' passed in then we return false"
    (is (false? (revision? nil)))))

(deftest test-id?-true 
  (testing "just a quick test for a good id"
    (is (true? (id? "64dca73821060386b52188b6cc8e5a89")))))

(deftest test-id?-false
  (testing "just a quick test for a good id"
    (is (false? (id? "64d1060386b52188b6cc8e5a89")))))

(deftest test-email?-true
  (testing "a normal email should pass"
    (is (true? (email? "marc.derosa@gmail.com")))))

(deftest test-email?-false
  (testing "non emails should fail"
    (is (false? (email? "xxx")))))

(deftest test-email?-collection
  (testing "we have a lot documents that contain collections of emails so as a convience the email
function should also be able to validate a collection of emails"
    (is (email? ["marc@comp.com" "minmin@comp.com"]))))

(deftest test-progress?-true
  (testing "test that a valid progress returns true"
    (is (progress? "in-progress"))))

(deftest test-progress?-false
  (testing "a non valid progress returns false"
    (is (not (progress? "inprogress")))))