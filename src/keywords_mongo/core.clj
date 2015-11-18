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
                          (cons (str/trim cur))
                          ;;vec
                          ;;hash-map
                          )))))))

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

(defn keyword-entry
  "creates a map describing a keyword for entry in the database"
  [keywordline]
  (hash-map :_id (first keywordline) :sub (second keywordline)
            ))

(defn save-keywords
  "saves all the keywords in file to db"
  [database collection file]
  (let [connection (mg/connect)
        db (mg/get-db connection database)
        file file]
    (map keyword-entry (make-list file))
    ))

(defn -main [& args]
  (save-keywords "monger-test" "keywords" (first args)))
