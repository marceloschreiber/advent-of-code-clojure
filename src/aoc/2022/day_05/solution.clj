(ns aoc.2022.day-05.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.string :as str]))

(defn transpose
  "Matrix transposition."
  [m]
  (apply mapv vector m))

(defn extract-crates
  "Receives a string such as \"[Z] [M] [P]\" and return
   '(:Z :M :P).
   When there are empty crates they are representend with spaces
   \"    [D]    \"
   return '(nil :D :nil)."
  [crates-str]
  (->> crates-str
       (partition 3 4)
       (map #(remove #{\space \[ \]} %))
       (map #(some-> % first str keyword))))

(defn pille-crates
  "Receives a collection such as [[nil :D] [:N :C] [:Z :M :P]]
   and returns [[:N :Z] [:D :C :M] [:P]]"
  [crates]
  (->> crates
       transpose
       (mapv #(into [] (remove nil? %)))))

(defn extract-rearrangement
  "Receives a string such as \"move 1 from 2 to 1\" and returns
   the map {:from 2 :to 1 :quantity 1}."
  [rearrangement-str]
  (let [[_match, quantity, from, to] (re-find #"move (\d+) from (\d+) to (\d+)" rearrangement-str)]
    {:from     (parse-long from)
     :to       (parse-long to)
     :quantity (parse-long quantity)}))

(def input (let [input-by-line (read-resources-file-by-line "2022/day_05.txt")

                 [crates-input
                  _blank-line
                  rearrangements-input] (partition-by str/blank? input-by-line)]
             {:crates         (->> crates-input
                                   butlast
                                   (map extract-crates)
                                   pille-crates)
              :rearrangements (map extract-rearrangement rearrangements-input)}))

(defn crate-mover-9000
  [crates quantity]
  (take quantity crates))

(defn crate-mover-9001
  [crates quantity]
  (->> (take quantity crates)
       reverse))

(defn move-crates-with-crate-mover
  "Returns the new crates configuration."
  [crates rearrangement crate-mover]
  (let [from-index (-> rearrangement :from dec)
        to-index   (-> rearrangement :to dec)
        quantity   (:quantity rearrangement)]
    (-> (update crates to-index #(-> (into (crate-mover (nth crates from-index) quantity) %)
                                     reverse
                                     vec))
        (update from-index #(into [] (drop quantity %))))))

(defn rearrange-crates-with-crate-mover
  "Returns the crates final position after executing all the rearrangements with the crate mover provided."
  [crates rearrangements crate-mover]
  (if (empty? rearrangements)
    crates
    (let [new-crates (move-crates-with-crate-mover crates (first rearrangements) crate-mover)]
      (rearrange-crates-with-crate-mover new-crates (rest rearrangements) crate-mover))))

(defn join-top-crates
  [crates]
  (->> crates
       (map first)
       (reduce (fn [acc crate]
                 (str acc (name crate))) "")))

(defn part-1
  [{:keys [crates rearrangements]}]
  (->> (rearrange-crates-with-crate-mover crates rearrangements crate-mover-9000)
       join-top-crates))

(defn part-2
  [{:keys [crates rearrangements]}]
  (->> (rearrange-crates-with-crate-mover crates rearrangements crate-mover-9001)
       join-top-crates))

(println "Part 1:" (part-1 input))
(println "Part 2:" (part-2 input))
