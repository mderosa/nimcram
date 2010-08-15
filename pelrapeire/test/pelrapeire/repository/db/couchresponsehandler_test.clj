(ns pelrapeire.repository.db.couchresponsehandler-test
  (:use pelrapeire.repository.db.couchresponsehandler
	clojure.test)
  (:import org.apache.http.HttpResponse
	   org.apache.http.StatusLine
	   org.apache.http.entity.StringEntity
	   org.apache.http.client.HttpResponseException))

(def mock-status-line
     (proxy [StatusLine] []
       (getStatusCode [] 300)))

(def mock-rsp
     (proxy [HttpResponse] []
       (getStatusLine [] mock-status-line)
       (getEntity [] (StringEntity. "{\"error\":\"not_found\",\"reason\":\"missing\"}"))))

(deftest test-exception-mechanism
  (testing "we should be able to parse the 'error' and 'reason' components of a couch 
error response"
    (is (thrown? HttpResponseException (. couch-rsp-handler handleResponse mock-rsp)))))
