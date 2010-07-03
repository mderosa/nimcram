
(ns pelrapeire.repository.db.dbapiwrappertest
  (:use clojure.test
	pelrapeire.repository.dbconfig
	pelrapeire.repository.db.dbapi
	pelrapeire.repository.db.dbapiwrapper))

(defn fn-return-json [id cfg]
  "{\"_id\":\"23dkdie3\", \"_rev\":\"1-3edic3er\", \"name\":12, \"active\":false}")
(defn fn-return-json3 [id json cfg]
    "{\"_id\":\"23dkdie3\", \"_rev\":\"1-3edic3er\", \"name\":12, \"active\":false}")

(defn fn-mock-overwrite [id json cfg]
  json)

(deftest test-get-operation
  (testing "this function should input a string id and recieve a clojure map containing a 
document content"
    (let [data (wrapper-get "23" fn-return-json db-config)]
      (is (not (nil? data)))
      (is (= 12 (data "name"))))))

(deftest test-create-operation-post
  (testing "a post request that creates should submit a map of values and return a map of 
those same values back only with additional id and rev values"
    (let [data (wrapper-create {"one" 1 "two" 2} fn-return-json db-config)]
      (is (data "_id"))
      (is (data "_rev")))))

(deftest test-create-operation-put
  (testing "at a high level a put request should be converted to a from a map"
    (let [data (wrapper-create "anid" {"one" 1 "two" 2} fn-return-json3 db-config)]
      (is (data "_id"))
      (is (data "_rev")))))

(deftest test-initial-rev?
  (is (not (initial-rev? {"one" 1})))
  (is (initial-rev? {"_id" "abcd" "_rev" "1-234dkdiidkje3w2"}))
  (is (not (initial-rev? {"_rev" "2-3kdi3k3ifkedkkd"})))
  (is (not (initial-rev? {"_rev" "14-eikde33rkkkek"})))
  (is (not (initial-rev? {"_rev" "-di3k3kdkdkeki3"}))))

(deftest test-update-write
  (testing "should overwrite any existing data"
    (let [actual (wrapper-update {"_id" 1 "_rev" "1-ad" "one" 1 "two" 2} :write fn-return-json fn-mock-overwrite db-config)]
	 (is (= 1 (actual "one")))
	 (is (= 2 (actual "two")))
	 (is (nil? (actual "name"))))))

(deftest test-update-append
  (testing "should append initial keys are retained, new keys are added, existing keys are overwritten"
    (let [actual (wrapper-update {"_id" "1" "_rev" "1-ad" "one" 1 "two" 2} :append fn-return-json fn-mock-overwrite db-config)]
	 (is (= 1 (actual "one")))
	 (is (= 2 (actual "two")))
	 (is (= 12 (actual "name"))))))

(deftest test-delete
  (testing "we should get a success response back"
    (let [actual (wrapper-delete {"_id" "3kdieee" "_rev" "3-dkiie"} (fn [id rev cfg] "{\"ok\" true}") db-config)]
      (is (true? (actual "ok"))))))
