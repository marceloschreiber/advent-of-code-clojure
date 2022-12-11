(ns aoc.2022.day-09.solution
  (:require [clojure.string :as str]
            [aoc.utils :refer [read-resources-file-by-line]]))

(defn parse-line [line]
  (let [[direction n-steps] (str/split line #" ")
        str->keyword       {"R" :right
                            "U" :up
                            "D" :down
                            "L" :left}]
    {:direction (get str->keyword direction)
     :n-steps (parse-long n-steps)}))

(def input (->> "2022/day_09.txt"
               read-resources-file-by-line))

(defn move
  [point direction]
  (merge-with + point (case direction
                        :up     {:x 0  :y 1}
                        :down   {:x 0  :y -1}
                        :left   {:x -1 :y 0}
                        :right  {:x 1  :y 0})))

(defn chebyshev-distance
  [{x1 :x y1 :y}
   {x2 :x y2 :y}]
  (max (abs (- x2 x1))
       (abs (- y2 y1))))

(defn tail-new-position
  [new-head tail old-head]
  (cond
    (> (chebyshev-distance new-head tail) 1) old-head
    :else tail))

(defn execute-motion
  [{:keys [head tail] :as grid} motion]
  (let [new-head-coord (move head (:direction motion))
        new-tail-coord (tail-new-position new-head-coord tail head)]
    (assoc grid :head new-head-coord :tail new-tail-coord)))

(defn part-1
  [input]
  (->> input
       (map parse-line)
       (reduce (fn [acc motion]
                 (concat acc (take (:n-steps motion) (repeat (assoc motion :n-steps 1)))))
               [])
       (reduce (fn [acc motion]
                 (let [new-grid (execute-motion (:grid acc) motion)]
                   (-> (assoc acc :grid new-grid)
                       (update :unique-positions conj (:tail new-grid)))))
               {:grid {:head {:x 0 :y 0}
                       :tail {:x 0 :y 0}}
                :unique-positions #{}})
       :unique-positions
       count))

(part-1 input);; => 6208 (too low)
