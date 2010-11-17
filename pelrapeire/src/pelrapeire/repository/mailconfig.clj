(ns pelrapeire.repository.mailconfig
  (:import (java.util Properties))
  (:use clojure.contrib.trace))

(def mail-config {:mail.smtp.host "smtp.gmail.com"
		  :mail.smtp.auth "true"
		  :mail.smtp.startls.enable, "true"
		  :username "na"
		  :password "na"})

(defn mail-properties []
  (let [mail-elements (filter #(re-find #":mail" (str (key %))) mail-config)]
    (loop [es mail-elements props (Properties.)]
      (if (empty? es)
	props
	(let [first (first es)
	      key (. (str (key first)) substring 1)
	      value (val first)]
	  (do (. props setProperty key value)
	      (recur (rest es) props )))))))

	  
