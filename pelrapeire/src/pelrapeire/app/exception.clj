(ns pelrapeire.app.exception
  (:use clojure.contrib.trace))

(defmacro 
  ^{:doc "When an exception happens in expr this macro catches the exception and translates it into a map
of the form {:errors [\"info\" ...]}. If no errors occur it returns normally"}
  with-exception-translation [expr] 
  `(try 
    ~expr
    (catch java.lang.Exception e#
      (let [msg# (if (.startsWith (.getMessage e#) "[")
		     (read-string (.getMessage e#))
		     (.getMessage e#))]
	(cond
	 (string? msg#) {:errors [(str msg#)]}
	 (vector? msg#) {:errors msg#}
	 true (IllegalStateException. "with-exception-translation can not read the exception message"))))))1


   


