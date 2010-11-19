(ns pelrapeire.app.exception-test
  (:use clojure.test
	pelrapeire.app.exception))

(defn returns-string [] "just me")

(deftest test-with-translation1
  (testing "in the case where the enclosed expression completes normally, the return 
value of the expression should be returned"
    (is (= "just me" (with-exception-translation (returns-string))))))

(deftest test-with-translation2
  (testing "an exception that contains a simple message should be transformed to a single element vector"
    (is (= {:errors ["a"]} (with-exception-translation (throw (Exception. "a")))))))

(deftest test-with-translation3
  (testing "an exception with multiple messages should transform to a multiple element vector"
    (is (= {:errors ["a","b"]} (with-exception-translation (throw (Exception. (str ["a","b"]))))))))

(deftest test-with-translation4
  (testing "an exception with a single vector message transforms to an error with a single vector obj"
    (is (= {:errors ["a"]} (with-exception-translation (throw (Exception. (str ["a"]))))))))

