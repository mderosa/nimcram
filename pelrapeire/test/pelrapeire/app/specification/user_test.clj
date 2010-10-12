(ns pelrapeire.app.specification.user-test
  (:use pelrapeire.app.specification.user
	clojure.test))

(deftest test-create-user
  (testing "we should be able to create a user given a password and email"
    (let [actual (create-user {"password" "pswd" "email" "marc@test.com" "confirmPassword" "pswd"})]
      (is (= "user" (actual "type")))
      (is (nil? (actual "confirmPassword")))
      (is (= [] (actual "projectsImContributingTo"))))))

(deftest test-create-user-bad-email
  (testing "we should throw an error if the user submits a bad email"
    (is (thrown? AssertionError (create-user {"password" "xxx" "email" "nothing"})))))

(deftest test-following-attribute
  (testing "string fields should be transformed into vectors"
    (let [actual (create-user {"password" "xd" "email" "ddkd@dkdu.com" "projectsImFollowing" "dude"})]
      (is (= "user" (actual "type")))
      (is (= ["dude"] (actual "projectsImFollowing"))))))

(deftest test-condition-user 
  (testing "a valid object sent in should give us a valid object out"
    (let [actual (condition-user {"_id" "2f7674fed2ebd1cb0b85b8427e63a2cf"
				  "_rev" "4-0617cdf115ad7d4d8f43ebdeadbc6396" 
				  "projectsImFollowing" [] 
				  "projectsImContributingTo" ["PicoMinMin"] 
				  "type" "user" 
				  "email" "micai@ebay.com" 
				  "password" "e7e02754901f31e9dea60841c9f263d5ef16b2"})]
      (is "user" (actual "type"))
      (is [] (actual "projectsImFollowing")))))

(deftest test-condition-user2
  (testing "we convert a logical array value from string to array"
    (let [actual (condition-user {"_id" "2f7674fed2ebd1cb0b85b8427e63a2cf"
				  "_rev" "4-0617cdf115ad7d4d8f43ebdeadbc6396" 
				  "projectsImFollowing" "PicoMinMin"
				  "projectsImContributingTo" ["PicoMinMin"] 
				  "type" "user" 
				  "email" "micai@ebay.com" 
				  "password" "e7e02754901f31e9dea60841c9f263d5ef16b2"})]
      (is ["PicoMinMin"] (actual "projectsImFollowing")))))

(deftest test-conversion-of-pwd
  (testing "we should convert user passwords to encrypted form when we create a user object"
    (let [actual (create-user {"password" "xxx" "email" "marc@email.com"})]
      (is (re-find #"^[0-9a-f]{32,}$" "e7e02754901f31e9dea60841c9f263d5ef16b2"))
      (is (re-find #"^[0-9a-f]{32,}$" (actual "password"))))))

