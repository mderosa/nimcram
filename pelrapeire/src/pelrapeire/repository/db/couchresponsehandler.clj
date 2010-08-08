(ns pelrapeire.repository.db.couchresponsehandler
  (:use clojure.contrib.json.read
	pelrapeire.app.errormapping)
  (:import org.apache.http.client.ResponseHandler
	   org.apache.http.util.EntityUtils
	   org.apache.http.client.HttpResponseException))

(def couch-rsp-handler
     (proxy [ResponseHandler] []
       (#^String handleResponse [rsp]
		 (let [statusCode (.. rsp getStatusLine getStatusCode)
		       entity (. rsp getEntity)]
		   (if (>= statusCode 300)
		     (let [error-info (read-json (EntityUtils/toString entity))
			   reason (error-info "reason")
			   code (error-to-code (error-info "error"))]
		       (throw (HttpResponseException. code reason)))
		     (and entity (EntityUtils/toString entity)))))))