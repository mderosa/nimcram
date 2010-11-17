(ns pelrapeire.repository.mail.mail
  (:use pelrapeire.repository.mailconfig)
  (:import (javax.mail Message Message$RecipientType Session)
	   (javax.mail.internet MimeMessage InternetAddress)
	   (java.util Properties)))

(defprotocol MailData
  (to [md])
  (message [md session]))

(defn ^Session create-session [^Properties props]
  (Session/getInstance props))

(defn 
  ^{:doc "sends off a email returning a map of the form {:sent [], :unsent []}"}
  send-mail [mailData config]
  (let [session (create-session (mail-properties config))
	msg (message mailData session)
        ]
    (do
      )))