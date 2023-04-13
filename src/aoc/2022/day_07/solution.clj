(ns aoc.2022.day-07.solution
  (:require [aoc.utils :refer [read-resources-file-by-line]]
            [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.zip :as zip]
            [criterium.core :as criterium]))

(def input (->> "2022/day_07.txt"
                read-resources-file-by-line))

(defn dispatch-parse-line* [_loc line]
  (let [[a b & _args] line]
    (cond
      (= "$" a)  {:command b}
      (= "dir" a) :dir
      (and (parse-long a) (string? b)) :file
      :else line)))

(defmulti parse-line* #'dispatch-parse-line*)

(defmethod parse-line* :default [_loc split-line]
  (throw (Exception. (str "No implementation for: " split-line))))

(defn- move-into-dir [loc dirname]
  (let [node (zip/node loc)]
    (if (and (vector? node) (= dirname (-> node first :name)))
      loc
      (move-into-dir (zip/right loc) dirname))))

(defmethod parse-line* {:command "cd"} [loc [_ _ dirname]]
  (condp = dirname
    "/" loc
    ".." (zip/up loc)
    (-> (zip/down loc)
        (move-into-dir dirname))))

(defmethod parse-line* {:command "ls"} [loc _]
  loc)

(defmethod parse-line* :dir [loc [_ dirname]]
  (zip/append-child loc [{:name dirname, :type :dir}]))

(defmethod parse-line* :file [loc [size filename]]
  (zip/append-child loc {:name filename, :type :file, :size (parse-long size)}))

(defn parse-line [loc line]
  (parse-line* loc (str/split line #" ")))

(defn parse [input]
  (loop [loc (zip/vector-zip [{:name "/", :type :dir}])
         i 0]
    (if (< i (count input))
      (recur (parse-line loc (get input i)) (inc i))
      (zip/root loc))))

(defn assoc-dir-sizes [tree]
  (letfn  [(assoc-sub-sizes [form]
             (if-let [_dir? (and (vector? form)
                                 (= :dir (-> form first :type)))]
               (assoc-in form [0 :size]
                         (reduce (fn [size child]
                                   (+ size (or (:size child)
                                               (:size (first child))))) 0 (rest form)))
               form))]
    (walk/postwalk assoc-sub-sizes tree)))

(defn part-1 [input]
  (->> (parse input)
       assoc-dir-sizes
       flatten
       (keep #(when (and (= :dir (:type %))
                         (>= 100000 (:size %)))
                (:size %)))
       (apply +)))

(defn part-2 [input]
  (let [total  70000000
        target 30000000
        dirs   (-> (parse input)
                   assoc-dir-sizes
                   flatten)
        unused (- total (-> dirs first :size))]
    (->> dirs
         (keep #(when (and (= :dir (:type %))
                           (> (+ unused (:size %)) target))
                  (:size %)))
         (apply min))))

(comment
  (criterium/with-progress-reporting (criterium/quick-bench (part-1 input)))
  ;; => Execution time mean : 2.316463 ms
  (criterium/with-progress-reporting (criterium/quick-bench (part-2 input)))
  ;; => Execution time mean : 2.294431 ms
  )
