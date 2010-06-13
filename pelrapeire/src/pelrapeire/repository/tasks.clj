
(ns pelrapeire.repository.tasks
  (:use pelrapeire.repository.uuid
	pelrapeire.repository.dbconfig)
  (:import org.apache.http.HttpEntity
	   (org.apache.http.client HttpClient ResponseHandler)
	   (org.apache.http.client.methods HttpGet HttpPut)
	   org.apache.http.entity.StringEntity
	   (org.apache.http.impl.client BasicResponseHandler DefaultHttpClient)
	   org.apache.http.client.methods.HttpGet))

(defn 
  #^{:tag String}
  get-task [map]
    (let [http-client (DefaultHttpClient.)
	  get (HttpGet. (str db-url "338e33077b1c67b3bddbaf38e8252c2b"))
	  rsp-handler (BasicResponseHandler.)]
    (. http-client execute get rsp-handler)))

(defn 
  #^{:tag String
     :doc "For the input map we are going to need data of the form:
For the output string we are expecting something like {'ok':true,'id':'11','rev:'1-55fa92c1a80a923f12ca22e79eefded1'}
where I have replaced double quotes with single quotes"}
  create-task [map]
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. "{\"test\":true}" "UTF-8")
	put (doto (HttpPut. (str db-url "12"))
	      (.addHeader "Content-Type" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute put rsp-handler)))

(defn update-task [map]
  )

(defn delete-task [map]
  )