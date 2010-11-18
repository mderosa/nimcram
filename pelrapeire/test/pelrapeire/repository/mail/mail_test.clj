(ns pelrapeire.repository.mail.mail-test
  (:use clojure.test
	clojure.contrib.trace
	pelrapeire.repository.mail.invitationmessage
	pelrapeire.repository.mail.mail)
  (:import (javax.mail Address SendFailedException)
	   pelrapeire.repository.mail.invitationmessage.InvitationData	   
	   (javax.mail.internet InternetAddress)))

(defn mk-exception-proxy []
  (let [arr (make-array InternetAddress 1)]
    (do 
      (aset arr 0 (InternetAddress. "ms@email.com"))
      (proxy [SendFailedException] []
	(getInvalidAddresses [] arr)
	(getValidSentAddresses [] arr)
	(getValidUnsentAddresses [] arr)))))

(deftest test-sent-emails
  (testing "we should be able to aggregate emails from errors"
    (let [actual (sent-emails ["one@email.com"] (mk-exception-proxy))
	  actual-strings (filter #(= java.lang.String (type %)) actual)]
      (is (= 2 (count actual)))
      (is (= (count actual) (count actual-strings))))))

(def invitation (InvitationData. "from@email.com" ["to@email.com"] "hello" "AProject" "localhost:8080"))

(def test-config {:activate.mail true
		  :mail.smtp.host "smtp.server.com"
		  :mail.smtp.auth "true"
		  :mail.smtp.startls.enable, "true"
		  :port 587
		  :username "na"
		  :password "na"})

(deftest test-send-mail
  (testing "we should get a workable error message if we can not send an email"
    (let [actual (trace (send-mail invitation test-config))]
      (is (not= nil (:error-msg actual)))
      (is (= "No provider for smpt" (:error-msg actual)))
      (is (= "to@email.com" (first (:unsent actual)))))))
