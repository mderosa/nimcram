(ns pelrapeire.app.exception
  (:use clojure.contrib.trace))

(defmacro 
  ^{:doc "When an exception happens in expr this macro catches the exception and translates it into a map
of the form {:errors []}. If no errors occur it returns normally"}
  with-exception-translation [expr] 
  `(try 
    ~expr
    (catch java.lang.Exception e#
      (let [msg# (read-string (.getMessage e#))]
	(cond
	 (symbol? msg#) {:errors [(str msg#)]}
	 (vector? msg#) {:errors msg#}
	 true (IllegalStateException. "with-exception-translation can not read the exception message"))))))1


   


