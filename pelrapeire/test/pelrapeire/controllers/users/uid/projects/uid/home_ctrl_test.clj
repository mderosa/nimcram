(ns pelrapeire.controllers.users.uid.projects.uid.home-ctrl-test
 (:use pelrapeire.controllers.users.uid.projects.uid.home-ctrl
	clojure.contrib.trace
	clojure.test))

(deftest test-calculate-hours-in-process1
  (testing "we should caclulate the days expired when only taskStartDate and taskCompleteDate are present"
    (let [actual (calculate-hours-in-process {"taskStartDate" [2010, 10, 1, 13, 21, 11]
					     "taskCompleteDate" [2010, 10, 2, 9, 11, 10]})]
	  (is (= 19 actual)))))

(deftest test-calculate-hours-in-process2
  (testing "we should calculate the days as 2X when taskStartDate and taskTerminateDate are present"
    (let [actual (calculate-hours-in-process {"taskStartDate" [2010, 10, 1, 8, 21, 11]
				     "taskTerminateDate" [2010, 10, 2, 9, 11, 10]})]
    (is (= 48 actual)))))

(defn fn-task-view [a b]
  {"total_rows" 92, "offset" 2, 
   "rows" [
	   {"id" "6516819d38bb97f45bc882d22d002d7d", "key" ["PicoMinMin" 2010 11 21 4 47 26], "value" {"specification" "all our data is correct but we currently dont send out email invites - implement email at some point", "priority" nil, "_rev" "3-2d16f3d9339f117cdfa8daba6887031e", "taskStartDate" [2010 11 18 5 41 35], "taskCompleteDate" [2010 11 21 4 47 26], "progress" "delivered", "taskCreateDate" [2010 10 15 6 16 16], "deliversUserFunctionality" true, "solutionTeam" [], "title" "implement mail for invites", "project" "PicoMinMin", "_id" "6516819d38bb97f45bc882d22d002d7d", "type" "task", "namespace" []}}
	   {"id" "ed09f6df49f86fb22c5ee3039400290a", "key" ["PicoMinMin" 2010 11 14 9 32 46], "value" {"specification" "the application should be able to be built / packaged /installed from the git repository.  We will rely on Lein for this.", "priority" nil, "_rev" "3-310b8763267e8682acfb2b75f4130c22", "taskStartDate" [2010 11 14 9 32 42], "taskCompleteDate" [2010 11 14 9 32 46], "progress" "delivered", "taskCreateDate" [2010 11 14 9 32 38], "deliversUserFunctionality" true, "solutionTeam" [], "title" "packaging", "project" "PicoMinMin", "_id" "ed09f6df49f86fb22c5ee3039400290a", "type" "task", "namespace" []}}
	   {"id" "29064048f7f43d073b8cb7efcc000ab2", "key" ["PicoMinMin" 2010 11 14 9 31 32], "value" {"specification" "we shouldnt expect people to connect to my laptop to get updates to views and to create a db from scratch", "priority" 2, "_rev" "4-09618653c6f9afd90d6eff81829100b8", "taskStartDate" [2010 11 14 9 29 4], "taskCompleteDate" [2010 11 14 9 31 32], "progress" "delivered", "taskCreateDate" [2010 10 10 4 54 25], "deliversUserFunctionality" true, "solutionTeam" [], "title" "db externalisation for build", "project" "PicoMinMin", "_id" "29064048f7f43d073b8cb7efcc000ab2", "type" "task", "namespace" []}} 
	   ]})

(deftest test-run1
  (testing "we can process data for three records returned"
    (let [actual (run {:fn-get (fn [] nil) :fn-update (fn [] nil)} 
			     fn-task-view 
			     {:params {:project-uid "PicoMinMin"}})
	  groups (:collection actual)]
      (is (= 1 (count groups)))
      (is (= 3 (count (first groups)))))))

(defn fn-task-view2 [a b]
  {"total_rows" 0, "offset" 2, 
   "rows" []})

(deftest test-run2
  (testing "we can process data for when no records are returned"
    (let [actual (run {:fn-get (fn [] nil) :fn-update (fn [] nil)} 
		      fn-task-view2
		      {:params {:project-uid "PicoMinMin"}})]
      (is (empty? (:collection actual))))))
