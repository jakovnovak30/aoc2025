(ns main.day1
  (:require [main.io :refer [read-file]]
            [clojure.edn :as edn]
            [clojure.math :refer [floor
                                  ceil]]))

(defn parse-input [example]
  (let [input (read-file 1 example)]
    (map (fn [e] [(get e 0)
                  (edn/read-string (subs e 1))])
         input)))

(defn counter1 [_ _ number-mod]
  (if (= number-mod 0)
    1 0))
(defn counter2 [old-number number number-mod]
  (let [starting-0 (if (= old-number 0) 1 0)
        ending-0   (if (= number-mod 0) 1 0)]
    (if (not= number number-mod)
      (->
       number
       (/ 100)
       (floor)
       (int)
       (abs)
       (- starting-0)
       (+ number-mod))

      0)))

(defn solve1
  ([example]
   (solve1 example counter1))
  ([example counter-fn]
   (let [input (parse-input example)]

     (->
      (reduce (fn [acc e]
                (let [direction     (get e 0)
                      number        (get e 1)
                      old-number    (get acc 0)
                      counter       (get acc 1)
                      new-number    (if (= direction \L)
                                      (- old-number number)
                                      (+ old-number number))
                      number-mod    (mod new-number 100)
                      counter-inc   (counter-fn old-number new-number number-mod)]

                  (println number e (+ counter counter-inc))
                  [number-mod (+ counter counter-inc)]))

              [50 0]
              input)))))

(defn solve2 [example]
  (solve1 example counter2))
