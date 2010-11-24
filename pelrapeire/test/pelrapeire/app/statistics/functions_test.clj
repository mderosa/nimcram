(ns pelrapeire.app.statistics.functions-test
  (:use clojure.test
	pelrapeire.app.statistics.functions))

(deftest test-std-deviation
  (testing "test the standard deviation of 2 numbers"
    (let [s (std-deviation [1 2])]
      (is (< s 0.708))
      (is (> s 0.707)))))

(deftest test-half-factorial1
  (testing "test simple imput"
    (let [r (half-factorial 3.5)]
      (is (> r 11.63))
      (is (< r 11.64)))))

(deftest test-half-factorial2
  (testing "test that we only accept halfs"
    (is (thrown? AssertionError (half-factorial 3.2)))))

(deftest test-c4-factor
  (testing "test we can get correct results for a sample number"
    (let [r (c4-factor 10)]
      (is (> r 0.972))
      (is (< r 0.973)))))

(deftest test-factorial-zero
  (testing "0! should be equal to 1 by definition"
    (is (= 1 (factorial 0)))))

(deftest test-average
  (testing "an empty list should return nil"
    (is (nil? (average '())))))