(ns aoc.2023.day-03.solution
  (:require [aoc.utils :refer [digit? char->int read-resource-file]]))

(def input (read-resource-file "2023/day_03.txt"))

(defn- new-line?
  "Predicate for new line character."
  [character]
  (= character \newline))

(defn- move-right
  "Move one position to the right."
  [[y x]]
  [y (inc x)])

(defn- move-next-line
  "Move to the beginning of next line."
  [[y _x]]
  [(inc y) 0])

(defn- str->grid
  "Maps a multiline string to a map structure. The keys are a vector of
   coordinates and values are the characters."
  [input]
  ;; Example:
  ;; (grid "12\n34")
  ;; => {[0 0] \1
  ;;     [0 1] \2
  ;;     [1 0] \3
  ;;     [1 1] \4}
  (->> input
       (reduce (fn [[g coordinate] character]
                 (if (new-line? character)
                   [g (move-next-line coordinate)]
                   [(assoc g coordinate character) (move-right coordinate)]))
               [(sorted-map) [0 0]])
       first))

(defn- adjacent
  "Return the eight adjacent coordinates."
  [coordinate]
  (->> (for [x [-1 0 1]
             y [-1 0 1]]
         (mapv + [x y] coordinate))
       (remove #(= % coordinate))))

(defn extract-numbers
  [grid]
  (loop [keys                         (keys grid)
         acc                          []
         digits                       []
         adjacent-symbols-coordinates #{}]
    (let [current-coordinate           (first keys)
          value                 (get grid current-coordinate)
          remaining-coordinates (rest keys)]
      (cond
        (empty? current-coordinate)
        acc

        (or (zero? (second current-coordinate))
            (not (digit? value)))
        (let [digit (parse-long (apply str digits))]
          (recur remaining-coordinates
                 (if (some? digit)
                   (conj acc {:digit                       digit
                              :adjacent-symbols            (map #(get grid %) adjacent-symbols-coordinates)
                              :adjacent-symbols-coordinate adjacent-symbols-coordinates})
                   acc)
                 (if (digit? value)
                   [value]
                   [])
                 #{}))

        :else
        (let [xf (filter (fn [coordinate]
                           (when-let [character (get grid coordinate)]
                             (not (or (digit? character)
                                      (= character \.))))))]
          (recur remaining-coordinates
                 acc
                 (conj digits (char->int value))
                 (into adjacent-symbols-coordinates xf (adjacent current-coordinate))))))))

(defn part-1 [input]
  (->> input
       str->grid
       extract-numbers
       (filter #(seq (:adjacent-symbols %)))
       (map :digit)
       (reduce +)))

(defn part-2 [input]
  (->> input
       str->grid
       extract-numbers
       (filter #(some (fn [symbol] (= \* symbol)) (:adjacent-symbols %)))
       (group-by :adjacent-symbols-coordinate) ;; this works by pure luck on how the grid was build
       vals
       (filter #(= 2 (count %)))
       (map #(map (fn [m] (:digit m)) %))
       (map #(apply * %))
       (reduce +)))
