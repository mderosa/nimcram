(ns pelrapeire.controllers.users.uid.projects-ctrl-test
  (:import org.apache.http.client.HttpResponseException)
  (:use pelrapeire.controllers.users.uid.projects-ctrl
	pelrapeire.app.exception
	clojure.contrib.trace
	clojure.test))

(deftest test-validate-params1
  (testing "parameters that supply a 'to' and 'project' attribute should pass"
    (let [actual (with-exception-translation (validate-params {"to" "marc@comp.com" "project" "some project"}))]
    (is (nil? actual)))))

(deftest test-validate-params2
  (testing "we should be able to submit multiple emails"
    (let [actual (with-exception-translation (validate-params {"to" "" "project" "a project"}))]
      (is (= 1 (count (:errors actual)))))))

(deftest test-validate-params3
  (testing "an empty project name should be flagged as a error"
    (let [actual (with-exception-translation (validate-params {"to" "marc@comp.com" "project" ""}))]
      (is (= 1 (count (:errors actual))))
      (is (= "a project name must be selected" (first (:errors actual)))))))

(deftest test-validate-params4
  (testing "we should be able to submit multiple emails"
    (let [actual (with-exception-translation (validate-params {"to" "marc@email.com, joe@email.com" "project" "a project"}))]
      (is (nil? actual)))))

(deftest test-validate-params5
  (testing "invalid emails should be flagged as errors"
        (let [actual (with-exception-translation (validate-params {"to" "marc, minmin" "project" "a project"}))]
	  (is (= 1 (count (:errors actual)))))))

(deftest test-add-recipients-project-if-not-contributor
  (testing "if the project can not be found then we should get an error message back"
    (let [params {"to" "someone@comp.com" "project" "notexist"}
	  fn-get (fn [x] (throw (HttpResponseException. 302 "resources not found")))
	  fn-update (fn [x y] {"ok" true})
	  actual (with-exception-translation (add-recipients-to-project-if-not-contributor fn-get fn-update params))
	  ]
      (is (thrown? HttpResponseException (add-recipients-to-project-if-not-contributor fn-get fn-update params)))
      (is (not (nil? (:errors actual))))
      (is (= "resources not found" (first (:errors actual))))
)))


