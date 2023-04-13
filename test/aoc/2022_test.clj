(ns aoc.2022-test
  (:require [clojure.test :refer [deftest is testing]]
            [aoc.2022.day-01.solution :as day-01]
            [aoc.2022.day-02.solution :as day-02]
            [aoc.2022.day-03.solution :as day-03]
            [aoc.2022.day-04.solution :as day-04]
            [aoc.2022.day-05.solution :as day-05]
            [aoc.2022.day-06.solution :as day-06]
            [aoc.2022.day-07.solution :as day-07]
            [aoc.2022.day-08.solution :as day-08]
            [aoc.2022.day-09.solution :as day-09]
            [aoc.2022.day-10.solution :as day-10]))

(deftest day-01-test
  (testing "Part 1"
    (is (= 72240 (day-01/part-1 day-01/elfs-calories))))

  (testing "Part 2"
    (is (= 210957 (day-01/part-2 day-01/elfs-calories)))))

(deftest day-02-test
  (testing "Part 1"
    (is (= 12535 (day-02/part-1 day-02/input))))

  (testing "Part 2"
    (is (= 15457 (day-02/part-2 day-02/input)))))

(deftest day-03-test
  (testing "Part 1"
    (is (= 7826 (day-03/part-1 day-03/input))))

  (testing "Part 2"
    (is (= 2577 (day-03/part-2 day-03/input)))))

(deftest day-04-test
  (testing "Part 1"
    (is (= 453 (day-04/part-1 day-04/input))))

  (testing "Part 2"
    (is (= 919 (day-04/part-2 day-04/input)))))

(deftest day-05-test
  (testing "Part 1"
    (is (= "CVCWCRTVQ" (day-05/part-1 day-05/input))))

  (testing "Part 2"
    (is (= "CNSCZWLVT" (day-05/part-2 day-05/input)))))

(deftest day-06-test
  (testing "Part 1"
    (is (= 1155 (day-06/part-1 day-06/input))))

  (testing "Part 2"
    (is (= 2789 (day-06/part-2 day-06/input)))))

(deftest day-07-test
  (testing "Part 1"
    (is (= 1582412 (day-07/part-1 day-07/input))))

  (testing "Part 2"
    (is (= 3696336 (day-07/part-2 day-07/input)))))

(deftest day-08-test
  (testing "Part 1"
    (is (= 1825 (day-08/part-1 day-08/input))))

  (testing "Part 2"
    (is (= 235200 (day-08/part-2 day-08/input)))))

;; Too slow
#_(deftest day-09-test
  (testing "Part 1"
    (is (= 6269 (day-09/part-1 day-09/input))))

  (testing "Part 2"
    (is (= 2557 (day-09/part-2 day-09/input)))))
