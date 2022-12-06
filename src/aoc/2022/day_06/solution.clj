(ns aoc.2022.day-06.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.string :as str]))

(def input (-> "2022/day_06.txt"
               read-resources-file-by-line
               first))

(defn index-of [pred coll]
  (->> coll
       (keep-indexed (fn [idx element]
                       (when (pred (str/join element)) idx)))
       first))

(defn find-first-marker
  [input n pred]
  (->> input
       (partition n 1)
       (index-of pred)
       (+ n)))

(defn part-1
  [input]
  (find-first-marker input 4 #(apply distinct? %)))

(defn part-2
  [input]
  (find-first-marker input 14 #(apply distinct? %)))

(println "Part 1:" (part-1 input))
(println "Part 2:" (part-2 input))

(comment
  ;; Can find other types of markers by changing the pred
  (find-first-marker "abcxxxdef" 3 #(= "xxx" %))
  ;; => 6
)
