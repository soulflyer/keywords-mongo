(ns keywords-mongo.core
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.operators :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:gen-class))

(defn lines [filename]
  (with-open [rdr (io/reader filename)]
    (doall (line-seq rdr))))

(defn count-tabs [s]
  (count (take-while #(= \tab %) s)))

(defn make-list [filename]
  "Creates a vector of entries consisting of a keyword and an array of sub-keywords"
  (loop [lines (lines filename) res []]
    (if (nil? lines)
      res
      (let [[cur & more] lines
            cur-level    (count-tabs cur)]
        (recur (next lines)
               (conj res
                     (->> more
                          (take-while #(> (count-tabs %) cur-level))
                          (filter #(= (count-tabs %) (inc cur-level)))
                          (map str/trim)
                          vec
                          list
                          (cons (str/trim cur)))))))))


(defn save-keywords
  "saves all the keywords in file to db"
  [dbase collection file]
  (make-list file))

(defn -main [& args]
  (save-keywords "monger-test" "keywords" (first args)))
