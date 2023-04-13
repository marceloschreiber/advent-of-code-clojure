(ns aoc.2022.day-02.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.set :as set]
            [clojure.string :as str]))

(def input (->> "2022/day_02.txt"
                read-resources-file-by-line
                (map #(str/split % #" "))))

(def opponent-encryption->shape {"A" :rock
                                 "B" :paper
                                 "C" :scissors})

(def my-encryption->shape {"X" :rock
                           "Y" :paper
                           "Z" :scissors})

(def outcome-encryption->outcome {"X" :lost
                                  "Y" :draw
                                  "Z" :win})

(def shape->points {:rock     1
                    :paper    2
                    :scissors 3})

(def outcome->points {:lost 0
                      :draw 3
                      :win  6})

(def winner->loser {:rock     :scissors
                    :paper    :rock
                    :scissors :paper})

(def loser->winner (set/map-invert winner->loser))

(defn round-outcome
  [opponent-shape my-shape]
  (condp = my-shape
    opponent-shape :draw
    (opponent-shape winner->loser) :lost
    :win))

(defn shape-to-play-for-outcome
  [opponent-shape expected-outcome]
  (case expected-outcome
    :draw opponent-shape
    :lost (opponent-shape winner->loser)
    (opponent-shape loser->winner)))

(defn part-1
  [input]
  (->> input
       (map (fn [[encrypted-opponent-shape encrypted-shape-outcome]]
              (let [opponent-shape (get opponent-encryption->shape encrypted-opponent-shape)
                    my-shape       (get my-encryption->shape encrypted-shape-outcome)
                    shape-points   (my-shape shape->points)
                    outcome-points (-> (round-outcome opponent-shape my-shape)
                                       outcome->points)]
                (+ shape-points outcome-points))))
       (apply +)))

(defn part-2
  [input]
  (->> input
       (map (fn [[encrypted-opponent-shape encrypted-outcome]]
              (let [round-outcome  (get outcome-encryption->outcome encrypted-outcome)
                    opponent-shape (get opponent-encryption->shape encrypted-opponent-shape)
                    my-shape       (shape-to-play-for-outcome opponent-shape round-outcome)
                    shape-points   (my-shape shape->points)
                    outcome-points (round-outcome outcome->points)]
                (+ shape-points outcome-points))))
       (apply +)))
