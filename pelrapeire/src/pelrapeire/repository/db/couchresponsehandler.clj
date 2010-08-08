(ns pelrapeire.repository.db.couchresponsehandler
  (:use clojure.contrib.json.read)
  (:import org.apache.http.client.ResponseHandler
	   org.apache.http.util.EntityUtils
	   org.apache.http.client.HttpResponseException))

(def rsp-handler
     (proxy [ResponseHandler] []
       (#^String handleResponse [rsp]
		 (let [statusLine (. rsp getStatusLine)
		       entity (. rsp getEntity)]
		   (if (>= statusLine 300)
		     (throw (HttpResponseException. 1 "test"))
		     (and entity (EntityUtils/toString entity)))))))