
(ns pelrapeire.repository.taskstest
  (:use clojure.test
	clojure.contrib.trace
	pelrapeire.dbconfig
	pelrapeire.repository.tasks)
  (:import org.apache.http.client.HttpResponseException))

(deftest test-get-task-doesnt-exist
  (testing "should throw exception when a doc doesnt exist"
    (is (thrown? HttpResponseException (get-task "doesntExist" db-config-int)))))

(deftest test-get-task-exists
  (testing "should find the test doc id = 100"
  (let [rsp (get-task "100" db-config-int)]
    (is (. rsp contains "a document for testing")))))

(deftest test-delete-not-exist-task
  (is (thrown? HttpResponseException (delete-task "doesntExist" "23" db-config-int))))

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
  (let [rsp (create-task "{\"name\":\"a record to delete\"}" db-config-int)
	id (extract-id rsp)
	rev (extract-rev rsp)]
    (delete-task id rev db-config-int)))

(deftest test-add-then-delete
  (is (. (add-then-delete) contains "ok")))

(deftest update
  (testing "obj'(rev=n-1) update(:append | write) obj(rev=n) -> exception"
    (is (true? false)))
  (testing "obj'(rev=n) update(:append) obj(rev=n) -> merged obj"
    (is (true? false)))
  (testing "obj'(rev=n) update(:write) obj(rev=n) -> obj'(rev=n+1)"
    (is (true? false))))

    

