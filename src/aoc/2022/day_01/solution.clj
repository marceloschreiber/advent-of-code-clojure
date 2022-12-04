(ns aoc.2022.day-01.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]))

(def elfs-calories (->> "2022/day_01.txt"
                        read-resources-file-by-line
                        (map parse-long)
                        (partition-by nil?)
                        (take-nth 2)))

(defn part-1
  [input]
  (->> input
       (map #(apply + %))
       (apply max)))

(defn part-2
  [input]
  (->> input
       (map #(apply + %))
       sort
       (take-last 3)
       (apply +)))

(println "Part 1:" (part-1 elfs-calories))
(println "Part 2:" (part-2 elfs-calories))
