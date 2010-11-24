(ns pelrapeire.app.statistics.xbarchart-test
  (:use pelrapeire.app.statistics.xbarchart
	clojure.test))

(deftest test-take-first-of-each-n1
  (testing "for an empty seqence we should get an empty sequence back"
    (let [actual (take-first-of-each-n '() 2)]
    (is (= 0 (count actual))))))

(deftest test-take-first-of-each-n-list
  (testing "make sure that we take only what we need and also keep the correct order for lists"
    (let [actual (take-first-of-each-n '(1 1 1 2 2 2 3 3 3 4 4 4 5) 3)]
      (is (= 1 (first actual)))
      (is (= 5 (last actual))))))

(deftest test-take-first-of-each-n-vector
  (testing "make sure that we only take what we need and also keep the order unchanged"
    (let [actual (take-first-of-each-n [1 1 1 2 2 2 3 3 3 4 4 4 5 5] 3)]
      (is (= 1 (first actual)))
      (is (= 5 (last actual))))))

(deftest test-make-rational-subgroups1
  (testing "we should have zero subgroups when we have 1 observation"
    (let [actual (make-rational-subgroups '(1) 3)]
      (is (= 0 (count actual))))))

(deftest test-make-rational-subgroups2
  (testing "when count % group-size is not 0 then we should drop any observation later in the list
leaving more recent objservations until their groups are filled out"
    (let [actual (make-rational-subgroups '(1 2 1 3 7) 3)
	  head (first actual)]
      (is (= 1 (count actual)))
      (is (= 1 (first head)))
      (is (= 2 (second head)))
      (is (= 1 (nth head 2))))))

(deftest test-calculate-subgroup-averages 
  (testing "test that we can calculate averages from a sequence of the proper form"
    (let [actual (calculate-subgroup-averages '([1 2 3] [2 3 4] [4 5 6]))]
      (is (= '(2 3 5) actual)))))