(ns pelrapeire.repository.db.couchresponsehandlertest
  (:use pelrapeire.repository.db.couchresponsehandler
	clojure.test)
  (:import org.apache.http.HttpResponse))

(def rsp-proxy
     (proxy [HttpResponse] []
       (getStatusLine [] 400)))

(deftest test-exception-mechanism
  (testing "we should be able to parse the 'error' and 'reason' components of a couch 
error response"
    (is (= 1 1))))