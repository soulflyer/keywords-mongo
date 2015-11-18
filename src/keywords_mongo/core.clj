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


(defn make-maps [filename]
  "Creates a vector of entries consisting of a keyword and an array of sub-keywords"
  (loop [lines (lines filename) res []]
    (if (nil? lines)
      res
      (let [[cur & more] lines
            cur-level    (count-tabs cur)]
        (recur (next lines)
               (conj res
                     (hash-map
                      :_id (str/trim cur)
                      :sub (vec
                            (map str/trim
                                 (filter #(= (count-tabs %) (inc cur-level))
                                         (take-while #(> (count-tabs %) cur-level) more)
                                         ))))))))))


(defn save-keywords
  "saves all the keywords in file to db"
  [database collection file]
  (let [connection (mg/connect)
        db (mg/get-db connection database)
        file file]
    (for [line (make-maps file)]
      (mc/save db collection line))))

(defn -main [& args]
  (save-keywords "monger-test" "keywords" (first args)))
