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

(defn 
  ^{:tag Transport 
    :doc "Creates a transport object in which all the methods are noops.  This function allows us
to turn off mail.  To turn off mail set the :activate.mail key in the mail configuration to false."}
  create-transport [^Session session config]
  (if (:activate.mail config)
    (. session getTransport "smpt")
    (proxy [Transport] [session nil]
      (connect [host port user pwd] nil)
      (send [msg] nil))))
  
(defn sent-emails 
  ([es] es)
  ([es ^SendFailedException ex] 
     (concat es 
	     (map #(. % toString) (. ex getValidSentAddresses)))))

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
	to-addresses (trace (into-array InternetAddress (map #(InternetAddress. %) emails)))]
    (try 
     (let [^Session session (create-session (mail-properties config))
	   ^Message msg (message mailData session)
	   ^Transport tprt (create-transport session config)]
       (do
	 (. msg setRecipients Message$RecipientType/TO to-addresses)
	 (. tprt connect (:mail.smtp.host mail-config) (:port mail-config) (:username mail-config) (:password mail-config))
	 (. tprt send msg)
	 {:sent emails :unsent not-emails :error-msg nil}))
     (catch SendFailedException sfe 
       {:sent (sent-emails [] sfe) :unsent (unsent-emails not-emails sfe) :error-msg (. sfe getMessage)})
     (catch MessagingException me 
       {:sent [] :unsent emails :error-msg (. me getMessage)}))))

