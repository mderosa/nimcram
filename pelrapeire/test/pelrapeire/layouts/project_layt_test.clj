(ns pelrapeire.layouts.project-layt-test
  (:use pelrapeire.layouts.project-layt
	clojure.test))

(deftest test-home-url1
  (testing "we should be able to match the home url"
    (is (home-url? "/users/bc5287c66bc3acf02b958d6681390f3f/projects"))))

(deftest test-home-url2
  (testing "the home url should not the common stats url"
    (is (not (home-url? "/users/bc5287c66bc3acf02b958d6681390f3f/projects/PicoMinMin/home")))))

(deftest test-common-space-url1
  (testing "should match the common space url"
    (is (common-space-url? "/projects/PicoMinMin/home"))))

(deftest test-common-space-url2
  (testing "should not match the common status url"
    (is (not (common-space-url? "/users/bc5287c66bc3acf02b958d6681390f3f/projects/PicoMinMin/home")))))

(deftest test-common-stats-url
  (testing "should match the common stats url"
    (is (common-stats-url? "/users/bc5287c66bc3acf02b958d6681390f3f/projects/PicoMinMin/home"))))

(deftest test-menu-class
  (testing "should return the gh-hb class on a match"
    (is (= "gh-hs" (menu-class "HOME" {:uri "/users/83883/projects"})))))

