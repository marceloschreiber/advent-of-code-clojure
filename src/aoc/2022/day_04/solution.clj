(ns aoc.2022.day-04.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn str-pair->set
  "Converts a string in the format \"43-45\" to #{43 44 45}."
  [str-pair]
  (let [[start end] (as-> str-pair r
                      (str/split r #"-")
                      (map parse-long r))]
    (set (range start (inc end)))))

(def input (->> "2022/day_04.txt"
                read-resources-file-by-line
                (map #(->> (str/split % #",")
                           (map str-pair->set)))))

(defn part-1
  [input]
  (->> input
       (map (fn [[first-pairs second-pairs]]
              (or (set/subset? first-pairs second-pairs)
                  (set/subset? second-pairs first-pairs))))
       (filter true?)
       count))

(defn part-2
  [input]
  (->> input
       (map #(apply set/intersection %))
       (filter seq)
       count))
