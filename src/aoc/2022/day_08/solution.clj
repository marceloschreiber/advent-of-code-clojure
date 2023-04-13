(ns aoc.2022.day-08.solution
  (:require [aoc.utils :as utils]
            [criterium.core :as criterium]))


(def input (->> "2022/day_08.txt"
                utils/read-resources-file-by-line
                (mapv #(mapv utils/char->int %))))

(defn take-up-to
  [pred coll]
  (lazy-seq
   (when-let [s (seq coll)]
     (if (pred (first s))
       (cons (first s) nil)
       (cons (first s) (take-up-to pred (rest s)))))))

(defn visible?
  [node neighbors]
  (->> (vals neighbors)
       (some (fn [path]
               (every? #(< % node) path)))))

(defn scenic-score
  [node neighbords]
  (->> (vals neighbords)
       (map (fn [path]
              (-> (take-up-to #(>= % node) path)
                  count)))
       (apply *)))

(defn get-neighbors
  [tree col line]
  {:top (->> (subvec tree 0 line)
             (map #(nth % col))
             reverse)
   :bottom (->> (subvec tree (inc line))
                (map #(nth % col)))
   :left  (-> (nth tree line)
              (subvec 0 col)
              reverse)
   :right (-> (nth tree line)
              (subvec (inc col)))})

(defn part-1
  [input]
  (->> (map-indexed (fn [line lines]
                      (map-indexed (fn [col node]
                                     (visible? node (get-neighbors input col line))) lines)) input)
       flatten
       (filter true?)
       count))

(defn part-2
  [input]
  (->> input
       (map-indexed (fn [line lines]
                      (map-indexed (fn [col node]
                                     (scenic-score node (get-neighbors input col line))) lines)))
       flatten
       (apply max)))

(comment
  ;; Slow solution:
  (criterium/with-progress-reporting (criterium/quick-bench (part-1 input)))
  ;; => Execution time mean: 111ms
  (criterium/with-progress-reporting (criterium/quick-bench (part-2 input)))
  ;; => Execution time mean: 120ms
  )
