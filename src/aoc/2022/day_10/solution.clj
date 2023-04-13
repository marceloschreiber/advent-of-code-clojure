(ns aoc.2022.day-10.solution
  (:require [clojure.string :as str]
            [aoc.utils :refer [read-resources-file-by-line]]))

(defn parse-line [line]
  (let [[instruction argument] (str/split line #" ")]
    (cond-> {:name (keyword instruction)}
      argument (assoc :value (parse-long argument)))))

(defn expand-cycles [instruction]
  (reduce (fn [acc instruction]
            (condp = (:name instruction)
              :addx (into acc [(assoc instruction :cycle :first)
                               (assoc instruction :cycle :second)])
              (conj acc instruction))) [] instruction))

(defn calculate-x-after-instruction [instruction]
  (cond
    (nil? instruction) 1
    (and (= :addx   (-> instruction :instruction :name))
         (= :second (-> instruction :instruction :cycle)))
    (+ (:x-start-cycle instruction)
       (-> instruction :instruction :value))

    :else (:x-start-cycle instruction)))

(defn inc-with-default [cycle]
  (if cycle
    (inc cycle)
    1))

(defn execute-history [instructions]
  (reduce (fn [acc instruction]
            (let [previous-instruction (last acc)
                  new-instruction      {:instruction instruction
                                        :cycle (inc-with-default (:cycle previous-instruction))
                                        :x-start-cycle (or (:x-end-cycle previous-instruction) 1)}]
              (conj acc (assoc new-instruction :x-end-cycle (calculate-x-after-instruction new-instruction)))))
          []
          instructions))

(def input (->> (read-resources-file-by-line "2022/day_10.txt")
                (map parse-line)
                expand-cycles
                execute-history))

(defn symbol-to-draw [row cycle]
  (let [sprinte-center   (:x-start-cycle cycle)
        sprite-positions #{sprinte-center
                           (+ 1 sprinte-center)
                           (+ 2 sprinte-center)}
        column-position  (+ (* -40 row) (:cycle cycle))]
    (if (contains? sprite-positions column-position) "#" ".")))

(defn part-1 [input]
  (let [cycle-20 (get input (dec 20))
        cycle-60-to-240 (->> (drop (dec 60) input)
                             (take-nth 40))]
    (->> (cons cycle-20 cycle-60-to-240)
         (map #(* (:cycle %) (:x-start-cycle %)))
         (apply +))))

(defn part-2
  [input]
  (let [row-size 40
        rows     (partition row-size input)
        display  (map-indexed (fn [i row]
                                (reduce #(str %1 (symbol-to-draw i %2)) "" row)) rows)]
    display))

(defn print-letters
  "Use for printing part 2 response."
  [display]
  (let [row-size          40
        number-of-letters 8
        space-by-letter   (/ row-size number-of-letters)
        letters           (->> (range 1 (inc number-of-letters))
                               (map (fn [letter-n]
                                      (map (fn [row]
                                             (->> row
                                                  (drop (* space-by-letter (dec letter-n)))
                                                  (take space-by-letter)
                                                  str/join)) display)))
                               (map #(str/join "\n" %))
                               (map #(str/replace % #"\." " ")))]
    (doseq [letter-n (range 0 number-of-letters)]
      (println (format "Letter %d is " letter-n))
      (print (nth letters letter-n))
      (println "\n"))))
