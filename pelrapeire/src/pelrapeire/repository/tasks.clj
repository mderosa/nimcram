
(ns pelrapeire.repository.tasks
  (:use pelrapeire.repository.uuid
	pelrapeire.repository.dbconfig
	clojure.contrib.json.read :only read-json
	clojure.contrib.json.write :only json-str)
  (:import org.apache.http.HttpEntity
	   (org.apache.http.client HttpClient ResponseHandler)
	   (org.apache.http.client.methods HttpGet HttpPut)
	   org.apache.http.entity.StringEntity
	   (org.apache.http.impl.client BasicResponseHandler DefaultHttpClient)
	   org.apache.http.client.methods.HttpGet))

(defn 
  #^{:tag String
     :db-resp-error "{'error':'not_found','reason':'missing'}"
     :doc "Gets an existing document from the database"
     }
  get-task [map]
    (let [http-client (DefaultHttpClient.)
	  get (HttpGet. (str db-url "338e33077b1c67b3bddbaf38e8252c2b"))
	  rsp-handler (BasicResponseHandler.)]
    (. http-client execute get rsp-handler)))

(defn 
  #^{:tag String
     :input-fomat "{
	'_id': 'Uuid',
	'type': 'task',
	'title': 'String', 
	'specification': 'String',
	'project': 'Uuid',
	'solution-team': ['Uuid'],
	'delivers-user-functionality': 'Boolean'
	'task-start-date': 'Date',
	'task-complete-date': 'Date',
	'progress': 'proposed|analysis active|development active|qa active|deployed'}"
     :db-resp-ok "{'ok':true,'id':'11','rev:'1-55fa92c1a80a923f12ca22e79eefded1'}"
     :db-resp-error "{'error':'conflict','reason':'Document update conflict.'}"
     :doc "This function creates a new task"
     }
  create-task [map]
  (let [http-client (DefaultHttpClient.)
	entity (StringEntity. "{\"test\":true}" "UTF-8")
	put (doto (HttpPut. (str db-url "12"))
	      (.addHeader "Content-Type" "application/json")
	      (.setEntity entity))
	rsp-handler (BasicResponseHandler.)]
    (. http-client execute put rsp-handler)))

(defn 
  #^{:tag String
     :db-resp-error "{'error':'bad_content_type','reason':'Invalid Content-Type header for form upload'}"
     :doc "This function makes a put request that overwrites an existing record.  For the overwrite to
be successful the request json must contain a '_rev' parameter that matches the current revision number
of the document"
     }
  update-task [map]
  )

(defn delete-task [map]
  )