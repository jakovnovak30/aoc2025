(ns main.day2
  (:require [main.io :refer [read-file]]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(defn parse-input [example]
  (let [input (read-file 2 example ",")]
    (mapv (fn [e] (str/split
                   (str/trim e) (re-pattern "-")))
          input)))

(defn invalid1 [number-str]
  (let [length  (count number-str)
        string1 (subs number-str 0 (/ length 2))
        string2 (subs number-str (/ length 2))]

    (= string1 string2)))

(defn invalid2 [number-str]
  (let [length        (count number-str)
        subseq-lens   (range 1 length)]

    (some true?
          (map
           (fn [sublen]
             (if-not (= 0 (mod length sublen))
               false

               (let [substrings
                     (partition sublen number-str)
                     substrings (map (fn [e] (apply str e)) substrings)]
                 (apply = substrings))))

           subseq-lens))))

(defn solve1
  ([example]
   (solve1 example invalid1))

  ([example invalid-fn]
   (let [input (parse-input example)
         input (map (fn [e]
                      (range
                       (edn/read-string (get e 0))
                       (inc (edn/read-string (get e 1)))))
                    input)
         input (flatten input)]
     (reduce
      (fn [acc e]
        (if (invalid-fn (str e))
          (do
           ; (println e)
            (+ acc e))
          acc))
      0
      input))))

(defn solve2 [example]
  (solve1 example invalid2))
