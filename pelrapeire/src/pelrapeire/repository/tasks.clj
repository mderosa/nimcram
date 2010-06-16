
(ns pelrapeire.repository.tasks
  (:use pelrapeire.repository.uuid)
  (:import org.apache.http.HttpEntity
	   (org.apache.http.client HttpClient ResponseHandler)
	   (org.apache.http.client.methods HttpGet HttpPut)
	   org.apache.http.entity.StringEntity
	   (org.apache.http.impl.client BasicResponseHandler DefaultHttpClient)
	   org.apache.http.client.methods.HttpGet))

;(defn 
;  #^{:tag String
;     :db-resp-error "{'error':'not_found','reason':'missing'}"
;     :doc "Gets an existing document from the database"
;     }
;  get-task [map cfg]
;    (let [http-client (DefaultHttpClient.)
;	  get (doto (HttpGet. (str (:url cfg) "338e33077b1c67b3bddbaf38e8252c2b"))
;		(.addHeader "Accept" "application/json"))
;	  rsp-handler (BasicResponseHandler.)]
;    (. http-client execute get rsp-handler)))

(defn 
  #^{:tag String
     :db-resp-ok "{'ok':true,'id':'11','rev:'1-55fa92c1a80a923f12ca22e79eefded1'}"
     :db-resp-error "{'error':'conflict','reason':'Document update conflict.'}"
     :doc "create-task :: String -> Cfg -> Either JsonOk JsonError. Note this
function does not do any translation of json strings to clojure maps"
     :throws "throws an HttpResponseException for response codes >= 400"
     }
  create-task [json cfg]
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. json)
	put (doto (HttpPut. (str (:url cfg) (uuid)))
	      (.addHeader "Content-Type" "application/json")
	      (.addHeader "Accept" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute put rsp-handler)))

;(defn 
;  #^{:tag String
;     :db-resp-error "{'error':'bad_content_type','reason':'Invalid Content-Type header for form upload'}"
;     :doc "This function makes a put request that overwrites an existing record.  For the overwrite to
;be successful the request json must contain a '_rev' parameter that matches the current revision number
;of the document
;The parameters are a map containing a partail or complete task and a mode which is either :overwrite or 
;:append"
;     }
;  update-task [map cfg mode]
;  )

;(defn 
;  #^{:doc "all we need for the delete request is the task number, no revision 
;number is needed."}
;  delete-task [map cfg]
;  )