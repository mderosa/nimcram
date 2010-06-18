
(ns pelrapeire.repository.tasks
  (:use pelrapeire.repository.uuid
	clojure.contrib.json.read
	clojure.contrib.json.write)
  (:import org.apache.http.HttpEntity
	   (org.apache.http.client HttpClient ResponseHandler)
	   (org.apache.http.client.methods HttpGet HttpPut HttpDelete)
	   org.apache.http.entity.StringEntity
	   (org.apache.http.impl.client BasicResponseHandler DefaultHttpClient)
	   org.apache.http.client.methods.HttpGet))

(defn 
  #^{:tag String
     :doc "Gets an existing document from the database"
     :throws "throws an HttpResponseException for any https response code >= 400"
     }
  get-task [#^String id cfg]
    (let [http-client (DefaultHttpClient.)
	  get (doto (HttpGet. (str (:url cfg) id))
		(.addHeader "Accept" "application/json"))
	  rsp-handler (BasicResponseHandler.)]
    (. http-client execute get rsp-handler)))

(defn 
  #^{:tag String
     :db-resp-ok "{'ok':true,'id':'11','rev:'1-55fa92c1a80a923f12ca22e79eefded1'}"
     :doc "create-task :: String -> Cfg -> Either JsonOk JsonError. Note this
function does not do any translation of json strings to clojure maps"
     :throws "throws an HttpResponseException for response codes >= 400"
     }
  create-task [#^String json cfg]
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. json)
	put (doto (HttpPut. (str (:url cfg) (uuid)))
	      (.addHeader "Content-Type" "application/json")
	      (.addHeader "Accept" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute put rsp-handler)))

(defn 
  #^{:doc "for the delete request we need the the task number in the url and also the revision 
number as a parameter."}
  delete-task [id rev cfg]
  (let [http-client (DefaultHttpClient.)
	del (doto (HttpDelete. (str (:url cfg) id "?rev=" rev))
	      (.addHeader "Accept" "application/json"))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute del rsp-handler)))

(defn 
  #^{:tag String
     :doc "This function makes a put request that overwrites an existing record.  For the overwrite to
be successful the request json must contain a '_rev' parameter that matches the current revision number
of the document
The parameters are a map containing a partail or complete task and a mode which is either :overwrite or 
:append"
     }
  update-task [#^String json cfg mode]
  {:pre [(and (. json contains "id") (. json contains "rev"))
	 (or (= mode :append) (= mode :write))
	 (not (nil? (:url cfg)))]}
  (if (= mode :append)
    (let [json-upd (read-json-string json)
	  json-cur (read-json-string (get-task (json-upd "id") cfg))
	  req (assoc json-cur json-upd)]
      (create-task (json-str req) cfg))
    (create-task json cfg)))

