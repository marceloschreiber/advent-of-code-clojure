(ns aoc.2022.day-02.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.set :as set]
            [clojure.string :as str]))

(def input (->> "2022/day_02.txt"
                read-resources-file-by-line
                (map #(str/split % #" "))))

(def opponent-shape-encryption {"A" :rock
                                "B" :paper
                                "C" :scissors})

(def my-shape-encryption {"X" :rock
                          "Y" :paper
                          "Z" :scissors})

(def round-outcome-encryption {"X" :lost
                               "Y" :draw
                               "Z" :win})

(def shape-points {:rock     1
                   :paper    2
                   :scissors 3})

(def outcome-points {:lost 0
                     :draw 3
                     :win  6})

(def winner-loser-shape {:rock     :scissors
                         :paper    :rock
                         :scissors :paper})

(def loser-winner-shape (set/map-invert winner-loser-shape))

(defn round-outcome
  [opponent-shape my-shape]
  (condp = my-shape
    opponent-shape :draw
    (opponent-shape winner-loser-shape) :lost
    :win))

(defn shape-to-play-for-outcome
  [opponent-shape expected-outcome]
  (case expected-outcome
    :draw opponent-shape
    :lost (opponent-shape winner-loser-shape)
    (opponent-shape loser-winner-shape)))

(defn part-1
  [input]
  (->> input
       (map (fn [[encrypted-opponent-shape encrypted-shape-outcome]]
              (let [opponent-shape (get opponent-shape-encryption encrypted-opponent-shape)
                    my-shape       (get my-shape-encryption encrypted-shape-outcome)
                    shape-points   (my-shape shape-points)
                    outcome-points (-> (round-outcome opponent-shape my-shape)
                                       outcome-points)]
                (+ shape-points outcome-points))))
       (apply +)))

(defn part-2
  [input]
  (->> input
       (map (fn [[encrypted-opponent-shape encrypted-outcome]]
              (let [round-outcome  (get round-outcome-encryption encrypted-outcome)
                    opponent-shape (get opponent-shape-encryption encrypted-opponent-shape)
                    my-shape       (shape-to-play-for-outcome opponent-shape round-outcome)
                    shape-points   (my-shape shape-points)
                    outcome-points (outcome-points round-outcome)]
                (+ shape-points outcome-points))))
       (apply +)))

(println "Part 1:" (part-1 input))
(println "Part 2:" (part-2 input))
