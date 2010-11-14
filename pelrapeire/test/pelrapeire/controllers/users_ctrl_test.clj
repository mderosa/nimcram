(ns pelrapeire.controllers.users-ctrl-test
  (:use pelrapeire.controllers.users-ctrl
	clojure.test))

(deftest check-errors-test1
  (testing "the email, password, and confirmPassword parameters should all exist"
    (= [(:email-required error-msgs)]
       (check-errors 
	(fn [x] [])
	{"email" "" "password" "xxx" "confirmPassword" "xxx"}))))

(deftest check-errors-test2
  (testing "the email parameter should be a valid email"
    (= [(:email-invalid error-msgs)]
       (check-errors
	(fn [x] [])
	{"email" "xxxx" "password" "xxx" "confirmPassword" "xxx"}))))

(deftest check-errors-test3
  (testing "the password parameter should not be empty"
    (= [(:password-required error-msgs)]
       (check-errors
	(fn [x] [])
	{"email" "team@comp.com" "password" "" "confirmPassword" ""}))))

(deftest check-errors-test4
  (testing "the password and confirmPassword parameters should be the same"
    (= [(:password-mismatch error-msgs)] 
       (check-errors
	(fn [x] [])
	{"email" "team@comp.com" "password" "11111" "confirmPassword" "22222"}))))

(deftest check-errors-test5
  (testing "the user should not already exist in the database"
    (= [(:login-exists error-msgs)] 
       (check-errors
	(fn [x] {"rows" ["user@comp.com"]})
	{"email" "user@comp.com" "password" "xxxxx" "confirmPassword" "xxxxx"}))))

(deftest check-errors-test6
  (testing "multiple missing parameters lead to error messages"
    (= [(:email-required error-msgs) (:password-required error-msgs)]
       (check-errors
	(fn [x] nil)
	{"email" nil "password" nil "confirmPassword" nil}))))

(deftest test-create-new-user
  (testing "we should get a normal user object back"
    (let [fn-create (fn [user]
		    {"ok" true "id" "bc5287c66bc3acf02b958d6681390f3f" "rev" ""})
	  fn-contributes-to (fn [email] {"rows" []})
	  actual (create-new-user fn-create fn-contributes-to
		  {"email" "marc@comp.com" "password" "shazam" "confirmPassword" "shazam"})]
      (is (= "bc5287c66bc3acf02b958d6681390f3f" (actual "_id")))
      (is (= "marc@comp.com" (actual "email"))))))

