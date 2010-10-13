(ns pelrapeire.app.context)

(defn pull [search-string ls] 
  (if (empty? ls)
    nil
    (if (. (first ls) startsWith search-string)
      (. (first ls) substring (+ 1 (. (first ls) indexOf "/")))
      (pull (rest ls)))))

(defn extract-user-uid [^String cookie-info] 
  (if (nil? cookie-info) 
    nil
    (let [start-index (+ 1 (. cookie-info indexOf "="))
	  base-string (. (. cookie-info substring start-index) trim)
	  key-vals (. base-string split ",")]
      (pull "user-uid/" key-vals))))

(defn 
  ^{:doc "takes a compojure request object and tries extracts application specific information"}
  build-context [req]
  (let [cookie-info ((:headers req) "cookie")]
    {:project-uid ((:params req) "project-uid")
     :user-uid (extract-user-uid cookie-info)}))

