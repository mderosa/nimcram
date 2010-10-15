(ns pelrapeire.app.specification.conditioners-test
  (:use pelrapeire.app.specification.conditioners
	clojure.test))

(deftest test-csv-string-to-vector1
  (testing "in the simple case we should split the string into n elements"
    (let [actual (csv-string-to-vector "one,two,three")]
      (is (= 3 (count actual))))))

(deftest test-csv-string-to-vector2
  (testing "a empty string should return an empty vector"
    (let [actual (csv-string-to-vector "")]
      (is (= 0 (count actual))))))

(deftest test-csv-string-to-vector3
  (testing "a nil input should be handled by returning an empty vector"
    (is (= [] (csv-string-to-vector nil)))))