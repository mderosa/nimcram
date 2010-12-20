(ns pelrapeire.repository.mail.invitationmessage
  (:use pelrapeire.repository.mail.mail)
  (:import (javax.mail Message Message$RecipientType Session)
	   (javax.mail.internet MimeMessage InternetAddress)))

(defn invitation-text [^String sender 
		       ^String project 
		       ^String user-msg 
		       ^String host]
  {:pre [sender project user-msg host]}
  (str "Hello,\n" 
       sender " has invited you to the project, " project " at http://" 
       host "/index.\n\n" user-msg "\n\n"
       "Set up an account using this email adrees and you can begin "
       "contributing to the project right away.\nBest,\n\tHokulea"))

(deftype InvitationData [^String frm 
			 ^clojure.lang.PersistentVector to 
			 ^String msg ^String prj 
			 ^String host]
  MailData
  (to [md] to)
  (message [md s]
	   (let [fromAddress (InternetAddress. frm)]
	     (doto (MimeMessage. s)
	       (.setSubject "join our project")
	       (.setText (invitation-text frm prj msg host))
	       (.setFrom fromAddress)))))
