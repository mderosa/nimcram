
(ns pelrapeire.repository.dbapitest
  (:use clojure.test
	clojure.contrib.trace
	clojure.contrib.json.read
	clojure.contrib.json.write
	pelrapeire.dbconfig
	pelrapeire.repository.dbapi)
  (:import org.apache.http.client.HttpResponseException))

(deftest test-get-doc-doesnt-exist
  (testing "should throw exception when a doc doesnt exist"
    (is (thrown? HttpResponseException (get-doc "doesntExist" db-config-int)))))

(deftest test-get-doc-exists
  (testing "should find the test doc id = 100"
  (let [rsp (get-doc "100" db-config-int)]
    (is (. rsp contains "a document for testing")))))

(deftest test-delete-not-exist-task
  (is (thrown? HttpResponseException (delete-doc "doesntExist" "23" db-config-int))))

(defn extract-id [s] 
  (let [regex-id #"\"id\":\"([a-zA-Z0-9]+)\""]
    (get (re-find regex-id s) 1)))
(defn extract-rev [s] 
  (let [regex-id #"\"rev\":\"([a-zA-Z0-9-]+)\""]
    (get (re-find regex-id s) 1)))

(deftest test-extract-id 
  (is (= "1abZdi3" (extract-id "{\"id\":\"1abZdi3\", \"actual\":true}"))))
(deftest test-extract-rev
  (is (= "2-2f80c392bf0d5f4897178567f56689c9" (extract-rev "{\"ok\":true,\"id\":\"017d482a95684ce799f7d3309e058d62\",\"rev\":\"2-2f80c392bf0d5f4897178567f56689c9\"}"))))

(defn add-then-delete []
  (let [rsp (create-doc "{\"name\":\"a record to delete\"}" db-config-int)
	id (extract-id rsp)
	rev (extract-rev rsp)]
    (delete-doc id rev db-config-int)))

(deftest test-add-then-delete
  (is (. (add-then-delete) contains "ok")))

(deftest test-overwrite
  (testing "we should be able to overwrite an existing record"
    (let [original (read-json-string (get-doc "101" db-config-int))
	  org-mod (dissoc original "_id")
	  rsp (overwrite-doc (original "_id") (json-str org-mod) db-config-int)]
      (is (not (= (original "_rev") (extract-rev rsp)))))))

(deftest test-create-named-doc
  (testing "we should be able to create a new doc by name"
    (let [rsp1 (create-named-doc "deleteme" "{\"note\": \"this should not exists in the database if so it is an error in the testing code\"}" db-config-int)
	  rev (extract-rev rsp1)
	  rsp2 (delete-doc "deleteme" rev db-config-int)]
      (is (. rsp1 contains "ok"))
      (is (not (nil? rev))))))
