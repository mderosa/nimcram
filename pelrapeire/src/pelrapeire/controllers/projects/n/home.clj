
(ns pelrapeire.controllers.projects.n.home)

(defn 
  #^{:doc "get active-tasks + tasks completed within the last two weeks and send
them back in a map structure to clients {:active map1 :completed map2}"}
  run [fn-get-active-tasks fn-get-completed-tasks req])