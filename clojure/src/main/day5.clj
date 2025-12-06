(ns main.day5
  (:require
   [main.io :refer [read-file]]
   [clojure.string :as str]
   [clojure.edn :as edn]))

(defn parse-input [example]
  (let [input   (read-file 5 example)
        ranges  (->> (take-while (fn [e] (not= "" e)) input)
                     (mapv (fn [e] (->>
                                    (str/split e (re-pattern "-"))
                                    (mapv edn/read-string)))))
        n       (count input)
        range-n (count ranges)
        indices (->>
                 (take-last (dec (- n range-n)) input)
                 (mapv edn/read-string))]

    {:ranges      ranges
     :indices     indices}))

(defn is-fresh? [ranges index]
  (some true?
        (for [current-range ranges]
          (let [prvi    (get current-range 0)
                drugi   (get current-range 1)]
            (and
             (>= index prvi)
             (<= index drugi))))))

(defn add-ranges [ranges]
  (let [new-ranges
        (filterv (fn [e] (not (nil? e)))
                 (for [i (range (count ranges))
                       j (range (inc i) (count ranges))]
                   (let [range1   (get ranges i)
                         range2   (get ranges j)
                         min1     (get range1 0)
                         min2     (get range2 0)
                         max1     (get range1 1)
                         max2     (get range2 1)]
                     (if
                      (or
                       (and (<= min2 max1) (>= min2 min1))
                       (and (<= min1 max2) (>= min1 min2))
                       (and (>= max1 min2) (<= max1 max2))
                       (and (>= max2 min1) (<= max2 max1)))
                       [(min min1 min2) (max max1 max2)]
                       nil))))]
    (apply conj ranges new-ranges)))

(defn remove-redundant [ranges]
  (filterv (fn [e1] (not (reduce (fn [acc e2]
                                   (if (and
                                        (not= e1 e2)
                                        (>= (get e1 0) (get e2 0))
                                        (<= (get e1 1) (get e2 1)))
                                     true
                                     acc))

                                 false
                                 ranges)))
           ranges))

(defn update-once [ranges]
  (->
   ranges
   (add-ranges)
   (remove-redundant)))

(defn update-ranges [ranges limit]
  (let [new-ranges  (update-once ranges)]
    (if (or (= 10 limit )(= (count ranges) (count new-ranges)))
      ranges

      (update-ranges new-ranges (inc limit)))))

(defn solve1 [example]
  (let [input     (parse-input example)
        ranges    (:ranges input)
        indices   (:indices input)]
    (reduce (fn [acc e]
              (if (is-fresh? ranges e)
                (inc acc)
                acc))
            0
            indices)))

(defn solve2 [example]
  (let [input     (parse-input example)
        ranges    (:ranges input)]
    (let [updated-ranges (update-ranges ranges 0)]
      (->>
       (map (fn [e] (let [manji (get e 0)
                          veci  (get e 1)]
                      (inc (- veci manji))))
            updated-ranges)
       (reduce +)))))
