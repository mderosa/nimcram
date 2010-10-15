(ns pelrapeire.controllers.users.uid.projects-ctrl-test
  (:import org.apache.http.client.HttpResponseException)
  (:use pelrapeire.controllers.users.uid.projects-ctrl
	clojure.contrib.trace
	clojure.test))

(deftest test-validate-params1
  (testing "parameters that supply a 'to' and 'project' attribute should pass"
    (is (= [] (validate-params {"to" "marc@comp.com" "project" "some project"})))))

(deftest test-validate-params2
  (testing "an empty email value should be flagged as a error"
    (is (= 1 (count (validate-params {"to" "" "project" "some project"}))))))

(deftest test-validate-params3
  (testing "an empty project name should be flagged as a error"
    (is (= 1 (count (validate-params {"to" "marc@comp.com" "project" ""}))))))

(deftest test-validate-params4
  (testing "we should be able to submit multiple emails"
    (is (= [] (validate-params {"to" "marc@email.com, joe@email.com" "project" "a project"})))))

(deftest test-validate-params5
  (testing "invalid emails should be flagged as errors"
    (is (= 1 (count (validate-params {"to" "marc, minmin" "project" "a project"}))))))

(deftest test-add-recipients-project-not-found
  (testing "if the project can not be found then we should get an error message back"
    (let [params {"to" "someone@comp.com" "project" "notexist"}
	  fn-get (fn [x] (throw (HttpResponseException. 302 "")))
	  fn-update (fn [x y] {"ok" true})
	  actual (add-recipients-to-project-if-not-contributor fn-get fn-update params)]
    (is (not (nil? (:errors actual)))))))


	    