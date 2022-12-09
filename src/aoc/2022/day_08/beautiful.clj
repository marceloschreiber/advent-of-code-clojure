(ns aoc.2022.day-08.beautiful
  (:require [clojure.string :as str]
            [criterium.core :as criterium]))

;; Picked from the Clojurians's Slack by Felipe Cortez
(def input (-> (slurp "resources/2022/day_08.txt")
               str/split-lines))

(defn upto
  [pred coll]
  (lazy-seq
   (when-let [s (seq coll)]
     (if (pred (first s))
       (cons (first s) nil)
       (cons (first s) (upto pred (rest s)))))))

(defn solve
  [input]
  ((juxt #(count (filter :visible-from-outside? %))
         #(apply max (map :scenic-score %)))
   (let [grid (->> input
                   (mapv #(mapv (comp parse-long str) %)))]
     (for [i (range (count grid))
           j (range (count grid))
           :let [height-at #(get-in grid %)
                 height    (height-at [j i])
                 all-along [(map height-at (map #(do [j %]) (range (dec i) -1 -1)))
                            (map height-at (map #(do [j %]) (range (inc i) (count grid))))
                            (map height-at (map #(do [% i]) (range (dec j) -1 -1)))
                            (map height-at (map #(do [% i]) (range (inc j) (count grid))))]]]
       {:visible-from-outside?
        (some (fn [along] (every? #(< % height) along)) all-along)

        :scenic-score
        (reduce * (map (fn [along] (count (upto #(>= % height) along))) all-along))}))))

(def result (solve input))

(println "Part 1: " (first result))
(println "Part 2: " (second result))


(comment
  (criterium/with-progress-reporting (criterium/quick-bench (solve input)))
  ;; Execution time mean : 145.770879 ms
  )
