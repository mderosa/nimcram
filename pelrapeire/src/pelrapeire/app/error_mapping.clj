(ns pelrapeire.app.error-mapping
  (:use clojure.contrib.trace))

(def errors {"not_found" 1})
(def codes {1 "not_found"})

(defn error-to-code [strError]
  {:pre [(> (.. strError trim length) 0)]
   :post [(> % 0)]}
  (let [code (errors strError)]
    (if (nil? code)
      (throw IllegalArgumentException 
	     (str "the error, " strError " is not a recognized error"))
      code)))

(defn code-to-error [code]
     {:pre [(> code 0)]
      :post [[(> (.. % trim length) 0)]]}
     (let [error (codes code)]
       (if (nil? error)
	 (throw (IllegalArgumentException. 
		 (str "the code, " code ", is not a recognized error code")))
	 error)))
