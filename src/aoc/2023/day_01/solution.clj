(ns aoc.2023.day-01.solution
  (:require [aoc.utils :refer [read-resources-file-by-line char->int]]
            [clojure.string :as str]))


(def calibrations (read-resources-file-by-line "2023/day_01.txt"))

(defn- extract-first-digit
  "Given a string it returns the first digit it finds.
   a5b -> 5
   32 -> 3"
  [string]
  (loop [input-str string]
    (let [character (first input-str)]
      (if (Character/isDigit character)
        (char->int character)
        (recur (rest input-str))))))

(defn- extract-calibration
  "Given a string it takes its first digital and last digit.
   Then combine them: {first}{last}."
  [string]
  (let [first-digit (extract-first-digit string)
        last-digit  (extract-first-digit (reverse string))]
    (-> (str first-digit last-digit)
        parse-long)))

(def spelled->number {"one" 1
                      "two" 2
                      "three" 3
                      "four" 4
                      "five" 5
                      "six" 6
                      "seven" 7
                      "eight" 8
                      "nine" 9})

(defn- extract-calibration-enhanced
  "Given a string it takes its first digital and last digit.
     Then combine them: {first}{last}.
   The catch is that these numbers may be spelled out with letters.
   eightwothree = 83"
  [string]
  (->> string
       (re-seq #"(?=(\d|one|two|three|four|five|six|seven|eight|nine))")
       ((juxt first last))
       (map (fn [[_ digit]]
              (cond
                (Character/isDigit (first digit)) (parse-long digit)
                (string? digit) (spelled->number digit))))
       (str/join "")
       parse-long))

(defn part-1 [calibrations]
  (->> calibrations
       (map extract-calibration)
       (reduce +)))

(defn part-2 [calibrations]
  (->> calibrations
       (map extract-calibration-enhanced)
       (reduce +)))

(comment
  ;; Transducer version for fun and learning
  (defn- stop-on-first [pred finalize]
    (fn [reducing-fn]
      (fn
        ([] (reducing-fn))
        ([result] (reducing-fn (finalize result)))
        ([result item]
         (if (pred item)
           (reducing-fn (reduced item))
           (reducing-fn result item))))))

  (def first-digit-transducer (stop-on-first #(Character/isDigit %) char->int))

  (transduce first-digit-transducer conj "a5b6")

  (transduce (map extract-calibration) + calibrations)
  #_())
