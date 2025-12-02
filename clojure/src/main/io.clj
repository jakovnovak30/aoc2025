(ns main.io
  (:require [clojure.string :as str]))

(defn read-file
  ([day]
   (read-file day false))

  ([day example]
   (read-file day example "\n"))

  ([day example delimiter]
   (let [file-path
         (if example
           (format "resources/example%d.txt" day)
           (format "resources/input%d.txt" day))]

     (->
      (slurp file-path)
      (str/split (re-pattern delimiter))))))
