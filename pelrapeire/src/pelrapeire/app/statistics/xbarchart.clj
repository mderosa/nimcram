(ns pelrapeire.app.statistics.xbarchart
  (:use pelrapeire.app.statistics.functions))

(defn 
  ^{:doc "takes a sequence of the form (a b c d e f) and starting at the beginning returns every nth element.
For example (a d)"} 
  take-first-of-each-n [ls n]
  (let [frst (first ls)]
    (if (nil? frst) 
     '()
     (cons (first ls) (take-first-of-each-n (drop n ls) n)))))

(defn 
  ^{:doc "takes a sequence of elements and reworks those elements into a sequence of vectors of size = group-size.  
Any elements at the end of the list that are insufficient to make a full group are dropped"}
  make-rational-subgroups [ls group-size]
  (let [subgroup (map (fn [a b c] [a b c]) ls (drop 1 ls) (drop 2 ls))]
    (take-first-of-each-n subgroup group-size)))

(defn 
  ^{:doc "takes a sequence of the form ([a1 a2 a3] [b1 b2 b3]..) and creates a sequence of vector averages - ie
(a b ...)"}
  calculate-subgroup-averages [ls]
  (map #(average %) ls))

(defn calculate-subgroup-std-deviations [ls]
  (map #(std-deviation %) ls))

(defn 
  ^{:doc "calculates the upper control limits of a xbar chart given a list of the logical subgroup
averages and the logical subgroup standard deviations"}
  xbar-ucl [xbars ss group-size]
  {:pre [(= (count xbars) (count ss))]}
  (if (= 0 (count xbars))
    nil
    (let [a (* 3 (average ss))
	  b (* (c4-factor group-size) (Math/sqrt group-size))]
      (+ (average xbars) (/ a b)))))

(defn 
  xbar-lcl [xbars ss group-size]
  {:pre [(= (count xbars) (count ss))]}
  (if (= 0 (count xbars))
    nil
    (let [a (* 3 (average ss))
	  b (* (c4-factor group-size) (Math/sqrt group-size))]
      (- (average xbars) (/ a b)))))

