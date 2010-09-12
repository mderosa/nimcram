
(use 'clojure.contrib.duck-streams)
(use 'clojure.contrib.trace)

(defstruct node :contents :yes :no)

(def *nodes* {})

(defn defnode2
  ([name contents]
     (def *nodes* (assoc *nodes* name (fn []
					(println contents)))))
  ([name contents yes no]
     (def *nodes* (assoc *nodes* name (fn []
					(do 
					  (println (str contents "(y/n): "))
					  (let [line (.readLine (reader *in*))]
					    (if (= line "y")
					      ((yes *nodes*))
					      ((no *nodes*))))))))))
     	 
(defnode2 'people "is this person a man?" 'male 'female)
(defnode2 'male "is he living?" 'liveman 'deadman)
(defnode2 'deadman "was he american?" 'us 'them)
(defnode2 'us "is he on a coin?" 'coin 'cidence)
(defnode2 'coin "is the coin a penny?" 'penny 'coins)
(defnode2 'penny 'lincoln)

(('people *nodes*))

