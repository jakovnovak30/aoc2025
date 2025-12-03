(ns main.day3
  (:require
   [clojure.edn :as edn]
   [main.io :refer [read-file]]))

(defn max-joltage [batteries]
  (let [n (count batteries)]
    (reduce max
            (for [i (range n)
                  j (range (inc i) n)]
              (let [num1    (get batteries i)
                    num2    (get batteries j)
                    number  (str num1 num2)
                    number  (edn/read-string number)]
                number)))))

(def remove-smallest
  (memoize
   (fn [batteries]
     (->
      (reduce max
              (for [i (range (count batteries))]
                (let [current (str (subs batteries 0 i) (subs batteries (inc i)))
                      number  (edn/read-string current)]
                  number)))
      (str)))))

(defn max-joltage2
  ([batteries]
   (max-joltage2 batteries 0))
  ([batteries remove-ctr]
   (if (= remove-ctr (- 100 12))
     (edn/read-string batteries)

     (max-joltage2 (remove-smallest batteries) (inc remove-ctr)))))

(defn solve1 [example]
  (let [input (read-file 3 example)]
    (reduce (fn [acc e]
              (let [joltage (max-joltage e)]
                ;(println joltage)
                (+ acc joltage)))
            0
            input)))

(defn solve2 [example]
  (let [input (read-file 3 example)]
    (reduce (fn [acc e]
              (let [joltage (max-joltage2 e)]
                ;(println joltage)
                (+ acc joltage)))
            0
            input)))
