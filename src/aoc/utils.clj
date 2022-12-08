(ns aoc.utils
  (:require [clojure.java.io :as java-io]
            [clojure.string :as str]))

(defn read-resources-file-by-line
  "Receives a path relative to the resources folder and read its content
   splitting by line."
  [path]
  (-> path
      java-io/resource
      slurp
      str/split-lines))

(defn char->int
  "Transforms numeric character to its integer value."
  [^Character c]
  (Character/digit c 10))
