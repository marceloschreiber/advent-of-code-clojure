(ns aoc.2023.day-02.solution
  (:require [clojure.string :as str]
            [aoc.utils :refer [read-resources-file-by-line]]))

(def input (read-resources-file-by-line "2023/day_02.txt"))

(defn- extract-color-number
  "Given a string such as \"3 blue, 2 red\" and the color \"blue\", it returns 3.
   Return 0 when the color is not present in the string."
  [s color]
  (let [pattern      (re-pattern (str "(\\d+) " color))
        regex-result (re-find pattern s)]
    (if (nil? regex-result)
      0
      (-> regex-result second parse-long))))

(defn- parse-set
  "Given a string such as \"3 blue, 2 red\", it returns a map
   {:blue 3
    :red  2
    :green 0}."
  [s]
  (let [extract (partial extract-color-number s)]
    {:blue  (extract "blue")
     :green (extract "green")
     :red   (extract "red")}))

(defn- parse-game
  [s]
  (let [splitted    (str/split s #":|;")
        game-id     (->> splitted first (re-find #"\d+") parse-long)
        sets        (rest splitted)
        parsed-sets (map parse-set sets)]
    {:id   game-id
     :sets parsed-sets}))

(defn- impossible-game?
  [cubes set]
  (or (> (:blue set) (:blue cubes))
      (> (:green set) (:green cubes))
      (> (:red set) (:red cubes))))

(defn part-1
  [col]
  (->> col
       (map parse-game)
       (map #(assoc % :impossible-games (filter (fn [game-set]
                                                  (impossible-game? {:blue 14
                                                                     :green 13
                                                                     :red 12} game-set)) (:sets %))))
       (filter #(-> % :impossible-games empty?))
       (map :id)
       (reduce +)))

(defn- min-cubes-needed
  "Given several game sets (not Clojure set), return the minimal number of cubes
   that make the sets possible."
  [sets]
  (let [min-cubes (fn [color col]
                    (apply max (map color col)))]
    {:blue  (min-cubes :blue sets)
     :red   (min-cubes :red sets)
     :green (min-cubes :green sets)}))

(defn part-2
  [col]
  (->> col
       (map parse-game)
       (map #(min-cubes-needed (:sets %)))
       (map #(reduce * (vals %)))
       (reduce +)))
