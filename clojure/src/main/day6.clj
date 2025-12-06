(ns main.day6
  (:require
   [main.io :refer [read-file]]
   [clojure.string :as str]
   [clojure.edn :as edn]))

(defn parse-input [example]
  (let [input   (read-file 6 example)
        n       (count input)
        numbers (->>
                 (take (dec n) input)
                 (mapv (fn [e]
                         (->>
                          (vec (str/split e (re-pattern "\\s+")))
                          (filterv (comp not empty?))
                          (mapv edn/read-string)))))

        ops     (->
                 (get input (dec n))
                 (str/split (re-pattern "\\s+")))]
    {:numbers     numbers
     :ops         ops
     :row-count   (count numbers)
     :col-count   (count (get numbers 0))}))

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
