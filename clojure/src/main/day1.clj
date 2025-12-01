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

(defn counter2 [diff old-number _]
  (let [diff-range (if (> diff 0)
                     (range 1 (inc diff))
                     (range diff 0))]
    (reduce (fn [acc e]
              (let [curr (+ old-number e)
                    curr (mod curr 100)]
                ; (println (format "curr: %d" curr))
                (if (= curr 0)
                  (inc acc)
                  acc)))
            0
            diff-range)))

(defn solve1
  ([example]
   (solve1 example counter1))
  ([example counter-fn]
   (let [input (parse-input example)]

     (->
      (reduce (fn [acc e]
                (let [direction     (get e 0)
                      number        (get e 1)
                      number        (if (= direction \L)
                                      (- number) number)
                      old-number    (get acc 0)
                      counter       (get acc 1)
                      new-number    (+ old-number number)
                      number-mod    (mod new-number 100)
                      counter-inc   (counter-fn number old-number number-mod)]

                  [number-mod (+ counter counter-inc)]))

              [50 0]
              input)
      (get 1)))))

(defn solve2 [example]
  (solve1 example counter2))
