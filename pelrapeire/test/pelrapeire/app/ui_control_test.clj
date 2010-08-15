(ns pelrapeire.app.ui-control-test
  (:use pelrapeire.app.ui-control
	clojure.test))

(deftest test-error-list
  (testing "with no errors we just get a simple ul back"
    (is(= [:ul {:class "errors"}] (error-list {:one 1 :two 2})))))

(deftest test-error-list2
  (testing "with errors we should get the errors in a list"
    (is (= [:ul {:class "errors"}
	[:li "some error"]] (error-list {:errors '("some error")})))))
