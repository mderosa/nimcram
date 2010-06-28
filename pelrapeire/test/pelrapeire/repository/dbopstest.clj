
(ns pelrapeire.repository.dbopstest
  (:use clojure.test

	pelrapeire.repository.dbapi
	pelrapeire.repository.dbops))

(defn mock-get [id cfg]
  "{\"_id\":\"23dkdie3\", \"_rev\":\"1-3edic3er\", \"name\":12, \"active\":false}")

(deftest test-get-operation
  (testing "this function should input a string id and recieve a clojure map containing a 
document content"
    (let [data (repo-get "23" mock-get)]
      (is (not (nil? data)))
      (is (= 12 (data "name"))))))

