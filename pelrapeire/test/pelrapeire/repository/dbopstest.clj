
(ns pelrapeire.repository.dbopstest
  (:use clojure.test

	pelrapeire.repository.dbapi
	pelrapeire.repository.dbops))

(defn fn-return-json [id cfg]
  "{\"_id\":\"23dkdie3\", \"_rev\":\"1-3edic3er\", \"name\":12, \"active\":false}")
(defn fn-return-json3 [id json cfg]
    "{\"_id\":\"23dkdie3\", \"_rev\":\"1-3edic3er\", \"name\":12, \"active\":false}")

(deftest test-get-operation
  (testing "this function should input a string id and recieve a clojure map containing a 
document content"
    (let [data (repo-get "23" fn-return-json)]
      (is (not (nil? data)))
      (is (= 12 (data "name"))))))

(deftest test-create-operation-post
  (testing "a post request that creates should submit a map of values and return a map of 
those same values back only with additional id and rev values"
    (let [data (repo-create {"one" 1 "two" 2} fn-return-json)]
      (is (data "_id"))
      (is (data "_rev")))))

(deftest test-create-operation-put
  (testing "at a high level a put request should be converted to a from a map"
    (let [data (repo-create "anid" {"one" 1 "two" 2} fn-return-json3)]
      (is (data "_id"))
      (is (data "_rev")))))

(deftest test-initial-rev?
  (is (not (initial-rev? {"one" 1})))
  (is (initial-rev? {"_id" "abcd" "_rev" "1-234dkdiidkje3w2"}))
  (is (not (initial-rev? {"_rev" "2-3kdi3k3ifkedkkd"})))
  (is (not (initial-rev? {"_rev" "14-eikde33rkkkek"})))
  (is (not (initial-rev? {"_rev" "-di3k3kdkdkeki3"}))))

