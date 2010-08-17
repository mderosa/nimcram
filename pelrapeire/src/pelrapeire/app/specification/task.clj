(ns pelrapeire.app.specification.task)

(defn create-task []
  {"type" "task"
   "title" nil
   "specification" nil
   "project" nil
   "solutionTeam" []
   "deliversUserFunctionality" false
   "taskStartDate" nil
   "taskCompleteDate" nil
   "progress" "proposed"
   "priority" nil
   "namespace" []})