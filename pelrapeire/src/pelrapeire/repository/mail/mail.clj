(ns pelrapeire.repository.mail.mail
  (:import (javax.mail Message)))

(defprotocol MailData
  (to [md])
  (message [md session]))

(defn create-session [props])

(defn 
  ^{:doc "sends off a email returning a map of the form {:sent [], :unsent []}"}
  send-mail [mailData config])