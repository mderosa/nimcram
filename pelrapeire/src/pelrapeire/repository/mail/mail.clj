(ns pelrapeire.repository.mail.mail
  (:use pelrapeire.repository.mailconfig
	clojure.contrib.trace
	pelrapeire.app.validators)
  (:import (javax.mail Message Message$RecipientType Session Transport SendFailedException MessagingException)
	   (javax.mail.internet MimeMessage InternetAddress)
	   (java.util Properties)))

(defprotocol MailData
  (to [md])
  (message [md session]))

(defn ^Session create-session [^Properties props]
  (Session/getInstance props))

(defn sent-emails 
  ([es] es)
  ([es ^SendFailedException ex] 
     (trace (concat es 
		    (map #(. % toString) (. ex getValidSentAddresses))))))

(defn unsent-emails
  ([es] es)
  ([es ^SendFailedException ex] 
     (concat es 
	     (map #(. % toString) (. ex getInvalidAddresses)) 
	     (map #(. % toString) (. ex getValidUnsentAddresses)))))

(defn 
  ^{:doc "sends off a email returning a map of the form {:sent [], :unsent [] :error-msg xxx}"}
  send-mail [mailData config]
  (let [emails (filter email? (to mailData))
	not-emails (filter #(not (email? %)) (to mailData))
	to-addresses (to-array (map #(InternetAddress. %) emails))]
    (try 
     (let [^Session session (create-session (mail-properties config))
	   ^Message msg (message mailData session)
	   ^Transport tprt (. session getTransport "smpt")]
       (do
	 (. msg setRecipients Message$RecipientType/TO to-addresses)
	 (. tprt connect (:mail.smtp.host mail-config) (:port mail-config) (:username mail-config) (:password mail-config))
	 (. tprt send msg)))
     (catch SendFailedException sfe "NI")
     (catch MessagingException me "ni"))))

