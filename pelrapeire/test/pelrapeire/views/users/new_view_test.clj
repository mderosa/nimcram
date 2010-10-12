(ns pelrapeire.views.users.new-view-test
  (:use pelrapeire.views.users.new-view
	clojure.contrib.trace
	clojure.test))

(deftest test-view-error-reporting
  (testing "if there are any registration errors they should be reported by the view"
    (let [rslt (trace (show {:errors ["this is one error"]}))]
      (is (= :ul (first (second (:content rslt))))))))
      
  