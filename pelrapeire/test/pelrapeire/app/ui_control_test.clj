(ns pelrapeire.app.ui-control-test
  (:use pelrapeire.app.ui-control
	clojure.test))

(deftest test-error-list2
  (testing "with errors we should get the errors in a list"
    (is (= [:ul {:class "errors"}
	[:li "some error"]] (error-list {:errors '("some error")})))))

(deftest test-error-list-tagged-errors
  (testing "we should be able to display error list of the form {:tag [] :tag []}"
    (is (= [:ul {:class "errors"}
	    [:li "some error"]] (error-list :loc1 {:errors {:loc1 ["some error"]}})))))
		 
(deftest test-no-errors
  (testing "for the {:errors []} form we should have no print out if there are no errors"
    (is (= [:span] (error-list {})))))

(deftest test-no-errors-for-tagged-form
  (testing "for the {:errors {:tag []}} we should have not print out if there are not errors"
        (is (= [:span] (error-list :loc {})))))