(ns pelrapeire.repository.mail.invitationmessage-test
  (:use clojure.test
	pelrapeire.repository.mail.invitationmessage
	pelrapeire.repository.mailconfig
	pelrapeire.repository.mail.mail)
  (:import pelrapeire.repository.mail.invitationmessage.InvitationData))

(def request {:headers {
		"cookie" "Hokulea=user-uid/bc5287c66bc3acf02b958d6681390f3f",
		"user-agent" "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10, keep-alive 115", 
		"accept-charset" "ISO-8859-1,utf-8;q=0.7,*;q=0.7", 
		"accept" "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", 
		"host" "localhost:8080", 
		"referer" "http://localhost:8080/index", 
		"content-type" "application/x-www-form-urlencoded", 
		"accept-encoding" "gzip,deflate", 
		"content-length" 58, 
		"accept-language" "en-us,en;q=0.5", 
		"connection" "keep-alive"}})

(deftest test-invitation-text
  (testing "we should not create messages for unknown senders"
    (is (thrown? AssertionError (invitation-text nil, "pico" "hello" request)))))


(deftest test-message
  (testing "we should be able to create a message from InvitationData"
    (let [data (InvitationData. "marc@email.com" ["dude@email.com"] "hello" "PicoMinMin" "localhost:8080")
	  session (create-session (mail-properties mail-config))
	  actual (message data session)]
      (is (not (nil? actual))))))