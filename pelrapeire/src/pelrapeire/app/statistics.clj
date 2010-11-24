(ns pelrapeire.app.statistics)

(defn 
  ^{:doc "calculates the average of a sequence of numbers"}
  average [ls]
  (if (empty? ls)
    nil
    (/ (apply + ls) (count ls))))

(defn 
  ^{:doc "calculates the standard deviation of a seqence of numbers"}
  std-deviation [ls]
  (let [avg (/ (apply + ls) (count ls))
	sos (apply + (map #(Math/pow (- % avg) 2) ls))]
    (Math/sqrt (/ sos (- (count ls) 1)))))

(defn half-factorial [n]
  {:pre [(>= n -0.5)]}
  (if (< n 0) 
    (Math/sqrt Math/PI)
    (* n (half-factorial (- n 1)))))

(defn factorial [n]
  {:pre [(>= n 0) (= 0 (mod n 1))]}
  (if (or (= n 1) (= n 0))
    1
    (* n (factorial (- n 1)))))

(defn 
  ^{:doc "a generic factorial function that can calculate factorials for either whole numbers or half fractions"}
  factorial+ [n]
  (if (= 0 (mod n 1))
    (factorial n)
    (half-factorial n)))

(defn 
  ^{:doc "calculates the a c4 factor"}
  c4-factor [n]
  (let [a (factorial+ (- (/ n 2) 1))
	b (factorial+ (- (/ (- n 1) 2) 1))
	c (Math/sqrt (/ 2 (- n 1)))]
    (* c (/ a b))))
