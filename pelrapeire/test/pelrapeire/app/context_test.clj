(ns pelrapeire.app.context-test
  (:use pelrapeire.app.context
	clojure.test))

(def req-with-project-uid
     {
      :params {"submit" "login", "password" "shazam", "project-uid" "AProject"}
      :headers {}
      })

(def req-with-user-uid
     {
      :params {"submit" "login", "password" "shazam"}
      :headers {"cookie" "Hokulea=user-uid/bc5287c66bc3acf02b958d6681390f3f"}
      })

(deftest test-build-context-project
  (testing "we should be able to get a project-uid when available"
    (let [actual (build-context req-with-project-uid)]
      (is (= "AProject" (actual :project-uid))))))

(deftest test-build-context-user
  (testing "we should be able to get a user-uid when available"
    (let [actual (build-context req-with-user-uid)]
      (is (= "bc5287c66bc3acf02b958d6681390f3f" (actual :user-uid))))))