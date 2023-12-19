(ns aoc.2023.day-04.solution
  (:require [clojure.set :as set]
            [clojure.math :as math]
            [aoc.utils :refer [read-resources-file-by-line]]))

(defn- extract-digits
  [s]
  (->> s
       (re-seq #"\d+")
       (map parse-long)))

(defn- parse-input [input]
  (let [[_ card-number winning-numbers card-numbers] (re-find #".+?(\d+):(.+)\|(.+)" input)]
    {:card-number     (parse-long card-number)
     :winning-numbers (set (extract-digits winning-numbers))
     :card-numbers    (set (extract-digits card-numbers))
     :copies          1}))

(def cards (map parse-input (read-resources-file-by-line "2023/day_04.txt")))

(defn- calculate-points
  [numbers]
  (if (empty? numbers)
    0
    (/ (math/pow 2 (count numbers))
       2)))

(defn part-1 [input]
  (->> input
       (map #(set/intersection (:winning-numbers %) (:card-numbers %)))
       (map calculate-points)
       (reduce +)
       Math/round))

(defn- copy-cards
  [m card-number cards-to-copy number-of-copies]
  (loop [m m
         card-number card-number
         copies-number cards-to-copy]
    (let [next-card-number (inc card-number)
          max-card-number (->> m keys (apply max))]
      (if (or (zero? copies-number)
              (> next-card-number max-card-number))
        m
        (recur (update-in m [next-card-number 0 :copies] + number-of-copies)
               next-card-number
               (dec copies-number))))))

(defn part-2 [input]
  (let [m (->> input
               (map (fn [m]
                      (assoc m :matching-numbers (set/intersection (:winning-numbers m)
                                                                   (:card-numbers m)))))
               (group-by :card-number)
               (into (sorted-map)))]
    (->> m
         (reduce (fn [acc [card-number cards]]
                   (let [cards-to-copy     (-> cards first :matching-numbers count)
                         numbers-of-copies (get-in acc [card-number 0 :copies])]
                     (copy-cards acc card-number cards-to-copy numbers-of-copies)))
                 m)
         vals
         flatten
         (map :copies)
         (reduce +))))
