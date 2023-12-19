(ns aoc.2023-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc.2023.day-01.solution :as day-01]
            [aoc.2023.day-02.solution :as day-02]))

(deftest day-01-test
  (testing "Part 1"
    (is (= 53651 (day-01/part-1 day-01/calibrations))))

  (testing "Part 2"
    (is (= 53894 (day-01/part-2 day-01/calibrations)))))

(deftest day-02-test
  (testing "Part 1"
    (is (= 1867 (day-02/part-1 day-02/input))))

  (testing "Part 2"
    (is (= 84538 (day-02/part-2 day-02/input)))))
