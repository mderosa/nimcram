(ns pelrapeire.repository.mailconfig-test
  (:use clojure.test
	pelrapeire.repository.mailconfig))

(deftest test-mail-properties
  (testing "we should currently have 3 mail properties"
    (let [props (mail-properties)]
      (is (not (nil? (. props getProperty "mail.smtp.host"))))
      (is (= (. props size) 3)))))




