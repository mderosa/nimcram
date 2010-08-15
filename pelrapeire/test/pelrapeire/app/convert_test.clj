(ns pelrapeire.app.convert-test
  (:import org.joda.time.DateTime)
  (:use clojure.test
	pelrapeire.app.convert))

(deftest test-datetime-to-vector
  (let [actual (datetime-to-vector (DateTime. 2010 3 3 4 5 6 0))]
    (is (= 2010 (get actual 0)))
    (is (= 3 (get actual 2)))
    (is (= 5 (get actual 4)))))
