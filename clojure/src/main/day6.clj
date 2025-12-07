(ns main.day6
  (:require
   [main.io :refer [read-file]]
   [clojure.string :as str]
   [clojure.edn :as edn]))

(defn parse-input [example]
  (let [input       (read-file 6 example)
        n           (count input)
        numbers     (->>
                     (take (dec n) input)
                     (mapv (fn [e]
                             (->>
                              (vec (str/split e (re-pattern "\\s+")))
                              (filterv (comp not empty?))
                              (mapv edn/read-string)))))
        numbers-str  (->>
                      (take (dec n) input)
                      (mapv (fn [e]

                              (->>
                               (vec (str/split e (re-pattern "\\s+")))
                               (filterv (comp not empty?))))))

        ops          (->
                      (get input (dec n))
                      (str/split (re-pattern "\\s+")))]
    {:numbers        numbers
     :numbers-str    numbers-str
     :ops            ops
     :row-count      (count numbers)
     :col-count      (count (get numbers 0))}))

(defn solve1 [example]
  (let [input   (parse-input example)]
    (reduce +
            (for [i (range (:col-count input))]
              ; iterate thru operators
              (let [op  (get (:ops input) i)
                    op (cond
                         (= op "+") +
                         (= op "*") *
                         :else       (throw (ex-info "Unrecognized operator" {:op op})))]
                (reduce op
                        (for [j (range (:row-count input))]
                          (get-in (:numbers input) [j i]))))))))

(defn solve2 [example]
  (let [input         (parse-input example)
        numbers-str   (:numbers-str input)
        numbers-str   (vec (for [j (range (:col-count input))]
                             (vec (for [i (range (:row-count input))]
                                    (get-in numbers-str [i j])))))
        max-num-col   (mapv (fn [col] (reduce max (map count col)))
                            numbers-str)]
    (let [numbers-str   (vec (for [i (range (:col-count input))]
                               (vec (for [j (range (:row-count input))]
                                      (let [curr     (get-in numbers-str [i j])
                                            curr-max (get max-num-col i)
                                            curr-len (count curr)
                                            curr-pad (- curr-max curr-len)]
                                        (str curr (apply str (repeat curr-pad "0"))))))))]
      (reduce +
              (for [i (range (:col-count input))] ; i is the col index
                (let [op      (get (:ops input) i)
                      op      (cond
                                (= op "+") +
                                (= op "*") *
                                :else (throw (ex-info "Unrecognized operator" {:op op})))
                      col     (get numbers-str i)
                      num-len (count (get col 0))]
                  (reduce (fn [acc e] (println "col is " col e) (op acc e))
                          (for [j (range num-len)] ; j is the current number (right to left)
                            (reduce (fn [acc e] (+ (* 10 acc) e))
                                    0
                                    (for [k (range (count col))] ; k is the row
                                      ; i is the col index, j the index of current num in col
                                      ; k is the row
                                      (->
                                       (get-in col [k (- num-len j 1)])
                                       (int)
                                       (- 48))))))))))))
