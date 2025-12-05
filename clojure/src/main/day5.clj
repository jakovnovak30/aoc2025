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

(defn update-once [ranges]
  ())

(defn update-ranges [ranges]
  (let [new-ranges  (update-once ranges)]
    (if (= (count ranges) (count new-ranges))
      ranges

      (update-ranges new-ranges))))

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
    (->
     (reduce (fn [acc e]
               (let [prvi    (get e 0)
                     drugi   (get e 1)
                     vektor  (range prvi (inc drugi))]
                 (apply conj acc vektor)))
             #{}
             ranges)
     (count))))
