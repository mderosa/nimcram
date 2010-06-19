
(ns 
    #^{:doc "a low level string based api to couch db"}
  pelrapeire.repository.dbapi
  (:use pelrapeire.repository.uuid
	clojure.contrib.json.read
	clojure.contrib.json.write)
  (:import org.apache.http.HttpEntity
	   (org.apache.http.client HttpClient ResponseHandler)
	   (org.apache.http.client.methods HttpGet HttpPut HttpDelete HttpPost)
	   org.apache.http.entity.StringEntity
	   (org.apache.http.impl.client BasicResponseHandler DefaultHttpClient)
	   org.apache.http.client.methods.HttpGet))

(defn 
  #^{:tag String
     :doc "gets an existing document from the database"
     :throws "throws an HttpResponseException for any https response code >= 400"
     }
  get-doc [#^String id cfg]
  {:pre [(not (nil? id))]}
    (let [http-client (DefaultHttpClient.)
	  get (doto (HttpGet. (str (:url cfg) id))
		(.addHeader "Accept" "application/json"))
	  rsp-handler (BasicResponseHandler.)]
    (. http-client execute get rsp-handler)))

(defn 
  #^{:tag String
     :doc "db -> db + obj(system generated id)"
     :throws "throws an HttpResponseException for response codes >= 400"
     }
  create-doc [#^String json cfg]
  {:pre [(not (. json contains "_id")) (not (. json contains "_rev"))]}
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. json)
	post (doto (HttpPost. (:url cfg))
	      (.addHeader "Content-Type" "application/json")
	      (.addHeader "Accept" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute post rsp-handler)))

(defn 
  #^{:doc "deletion from the database by revision number"}
  delete-doc [id rev cfg]
  {:pre [(not (nil? id))
	 (not (nil? rev))]}
  (let [http-client (DefaultHttpClient.)
	del (doto (HttpDelete. (str (:url cfg) id "?rev=" rev))
	      (.addHeader "Accept" "application/json"))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute del rsp-handler)))

(defn 
  #^{:tag String
     :doc "db + obj -> db + obj'"
     :throws "throws an HttpResponseException for response codes >= 400"
     }
  overwrite-doc [#^String id json cfg]
  {:pre [(not (. json contains "_id")) (. json contains "_rev")]}
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. json)
	put (doto (HttpPut. (str (:url cfg) id))
	      (.addHeader "Content-Type" "application/json")
	      (.addHeader "Accept" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute put rsp-handler)))