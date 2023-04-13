(ns aoc.2022.day-03.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.set :as set]
            [clojure.string :as str]))


(def input (-> "2022/day_03.txt"
               read-resources-file-by-line))
(defn char-range [start end]
  (map char (range (int start) (inc (int end)))))

(def priority-list (zipmap (concat (char-range \a \z)
                                   (char-range \A \Z))
                           (range 1 ##Inf)))

(defn partition-equally
  [coll n]
  (partition (/ (count coll) n) coll))

(defn get-common-item [rucksacks]
  (->> (map set rucksacks)
       (apply set/intersection)
       first))

(defn part-1
  [input]
  (->> input
       (map (fn [items]
              (->> (partition-equally items 2)
                   (map str/join))))
       (map get-common-item)
       (map #(get priority-list %))
       (apply +)))

(defn part-2
  [input]
  (->> input
       (partition 3)
       (map get-common-item)
       (map #(get priority-list %))
       (apply +)))
