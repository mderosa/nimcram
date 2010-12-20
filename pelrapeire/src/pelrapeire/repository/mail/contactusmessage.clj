(ns pelrapeire.repository.mail.contactusmessage
  (:use pelrapeire.repository.mail.mail)
  (:import (javax.mail Message Message$RecipientType Session)
	   (javax.mail.internet MimeMessage InternetAddress)))

(deftype ContactUsData [^String fromActual
			^String fromNominal
			^String to
			^String message]
  MailData
  (to [md] [to])
  (message [md s]
	   (let [adminAddress (InternetAddress. to)
		 from (if (nil? fromActual) "non member" fromActual)]
	     (doto (MimeMessage. s)
	       (.setSubject (str "comments from - " from))
	       (.setText (str message
			      "\nrespond to: " fromNominal))
	       (.setFrom adminAddress)))))
