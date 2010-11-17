(ns pelrapeire.repository.mailconfig
  (:import (java.util Properties))
  (:use clojure.contrib.trace))

(def mail-config {:mail.smtp.host "smtp.gmail.com"
		  :mail.smtp.auth "true"
		  :mail.smtp.startls.enable, "true"
		  :port 587
		  :username "na"
		  :password "na"})

(defn mail-properties [config]
  (let [mail-elements (filter #(re-find #":mail" (str (key %))) config)]
    (loop [es mail-elements props (Properties.)]
      (if (empty? es)
	props
	(let [first (first es)
	      key (. (str (key first)) substring 1)
	      value (val first)]
	  (do (. props setProperty key value)
	      (recur (rest es) props )))))))

	  
