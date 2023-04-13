(ns aoc.2022.day-09.solution
  (:require [clojure.string :as str]
            [criterium.core :as criterium]))

(defn parse-line
  [line]
  (let [[direction steps] (str/split line #" ")]
    {:direction (case direction
                  "U" :up
                  "D" :down
                  "L" :left
                  "R" :right)
     :steps (parse-long steps)}))

(defn expand-moves
  [moves]
  (reduce (fn [expanded-moves move]
            (concat expanded-moves (->> (assoc move :steps 1)
                                        repeat
                                        (take (:steps move)))))
          [] moves))

(defn chebyshev-distance
  [{x1 :x y1 :y}
   {x2 :x y2 :y}]
  (max (abs (- x2 x1))
       (abs (- y2 y1))))

(defn move-point-to-direction
  [point direction]
  (merge-with + point (case direction
                        :up    {:x 0  :y 1}
                        :down  {:x 0  :y -1}
                        :left  {:x -1 :y 0}
                        :right {:x 1  :y 0}
                        :none  {:x 0  :y 0})))

(defn horizontal-move
  [point position]
  (if (and (> (chebyshev-distance point position) 1)
           (not= (:x point) (:x position)))
    (if (> (:x position) (:x point))
      :right
      :left)
    :none))

(defn vertical-move
  [point position]
  (if (and (> (chebyshev-distance point position) 1)
           (not= (:y position) (:y point)))
    (if (> (:y position) (:y point))
      :up
      :down)
    :none))

(defn directions-from-point-to-position
  [point position]
  {:x (horizontal-move point position)
   :y (vertical-move point position)})

(defn follow-head
  [tail head]
  (let [{:keys [x y]} (directions-from-point-to-position tail head)]
    (-> tail
        (move-point-to-direction x)
        (move-point-to-direction y))))

(defn apply-moves
  [system]
  (reduce (fn [system move]
            (let [current-head-position    (-> system :knots :head :positions last)
                  new-head-position        (move-point-to-direction current-head-position (:direction move))
                  system-with-updated-head (update-in system [:knots :head :positions] conj new-head-position)
                  tails                    (->> system
                                                :knots
                                                keys
                                                sort
                                                (remove #(= % :head)))]
              (reduce (fn [new-system tail]
                        (let [follows               (-> new-system :knots tail :follows)
                              current-tail-position (-> new-system :knots tail :positions last)
                              new-tail-position     (follow-head current-tail-position (-> new-system :knots follows :positions last))]
                          (update-in new-system [:knots tail :positions] conj new-tail-position)))
                      system-with-updated-head
                      tails)))
          system (:directions system)))

(def input (->> "resources/2022/day_09.txt"
               slurp
               str/split-lines
               (map parse-line)
               expand-moves))

(defn part-1
  [moves]
  (-> (apply-moves {:directions  moves
                    :knots      {:head    {:positions [{:x 0 :y 0}]}
                                 :tail    {:positions [{:x 0 :y 0}]
                                           :follows   :head}}})
      :knots
      :tail
      :positions
      set
      count))

(defn part-2
  [moves]
  (-> (apply-moves {:directions moves
                    :knots {:head {:positions [{:x 0 :y 0}]}
                            :1    {:positions [{:x 0 :y 0}]
                                   :follows   :head}
                            :2    {:positions [{:x 0 :y 0}]
                                   :follows   :1}
                            :3    {:positions [{:x 0 :y 0}]
                                   :follows   :2}
                            :4    {:positions [{:x 0 :y 0}]
                                   :follows   :3}
                            :5    {:positions [{:x 0 :y 0}]
                                   :follows   :4}
                            :6    {:positions [{:x 0 :y 0}]
                                   :follows   :5}
                            :7    {:positions [{:x 0 :y 0}]
                                   :follows   :6}
                            :8    {:positions [{:x 0 :y 0}]
                                   :follows   :7}
                            :9    {:positions [{:x 0 :y 0}]
                                   :follows   :8}}})
      :knots
      :9
      :positions
      set
      count))

(comment
  ;; Slow solution:
  (criterium/with-progress-reporting (criterium/quick-bench (part-1 input)))
  ;; => Execution time mean: 2.98s
  (criterium/with-progress-reporting (criterium/quick-bench (part-2 input)))
  ;; => Execution time mean: 19.44s
  )
