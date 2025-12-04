(ns main.day4
  (:require [main.io :refer [read-file]]))

(defn count-neighbours [input i j n m]
  (let [dx    [0  0 1 -1 1  1 -1 -1]
        dy    [1 -1 0  0 1 -1  1 -1]
        dirs  (map vector dx dy)]
    (->
     (reduce +
             (for [dir dirs]
               (let [x (+ (get dir 0) i)
                     y (+ (get dir 1) j)]
                 (cond
                   (or (< x 0) (>= x n)) 0
                   (or (< y 0) (>= y m)) 0
                   (= (get-in input [x y]) \@) 1
                   :else 0))))
     (< 4)
     (if 1 0))))

(defn solve1 [example]
  (let [input (read-file 4 example)
        n     (count input)
        m     (count (get input 0))]
    (reduce +
            (for [i (range n)
                  j (range m)]
              (if (= (get-in input [i j]) \@)
                (count-neighbours input i j n m)
                (do
                  ;(println (get-in input [n]))
                  0))))))

(defn update-state [state n m]
  (let [not-done (some true?
                       (for [i (range n)
                             j (range m)]
                         (if (and (= \@ (get-in state [i j]))
                                  (= 1 (count-neighbours state i j n m)))
                           true false)))]
    (if-not not-done
      state

      (let [new-state
            (vec
             (map-indexed
              (fn [i row]
                (vec
                 (map-indexed
                  (fn [j e]

                    (cond
                      (not= \@ e) \.
                      (= 1 (count-neighbours state i j n m)) \.
                      :else \@))
                  row)))
              state))]
        (update-state new-state n m)))))

(defn solve2 [example]
  (let [input       (read-file 4 example)
        n           (count input)
        m           (count (get input 0))
        end-state   (update-state input n m)]
    (reduce +
            (for [i (range n)
                  j (range m)]

              (let [prvi  (get-in input [i j])
                    drugi (get-in end-state [i j])]
                (if (not= prvi drugi)
                  1
                  0))))))
