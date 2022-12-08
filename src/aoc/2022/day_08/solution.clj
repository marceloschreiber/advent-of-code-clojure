(ns aoc.2022.day-08.solution
  (:require [aoc.utils :as utils]
            [criterium.core :as criterium]))


(def input (->> "2022/day_08.txt"
                utils/read-resources-file-by-line
                (mapv #(mapv utils/char->int %))))

(defn visible?
  [node neighbors]
  (let [tree-paths (vals neighbors)]
    (cond
      (some empty? tree-paths) true
      (some #(> node (apply max %)) tree-paths) true
      :else false)))

(defn scenic-score
  [node neighbords]
  (->> (vals neighbords)
       (map (fn [trees]
              (let [smaller-trees (take-while #(> node %) trees)]
                (if (= (count trees) (count smaller-trees))
                  (-> smaller-trees count)
                  (-> smaller-trees count inc)))))
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
                                     {:node node
                                      :visible? (visible? node (get-neighbors input col line))}) lines)) input)
       flatten
       (filter :visible?)
       count))

(defn part-2
  [input]
  (->> input
       (map-indexed (fn [line lines]
                      (map-indexed (fn [col node]
                                     {:node node
                                      :scenic-score (scenic-score node (get-neighbors input col line))}) lines)))
       flatten
       (map :scenic-score)
       (apply max)))

(println "Part 1:" (part-1 input)) ;; 1825
(println "Part 2:" (part-2 input)) ;; 235200

(comment
  ;; Slow solution:
  (criterium/with-progress-reporting (criterium/quick-bench (part-1 input)))
  ;; => Execution time mean: 216ms
  (criterium/with-progress-reporting (criterium/quick-bench (part-2 input)))
  ;; => Execution time mean: 161ms
  )
