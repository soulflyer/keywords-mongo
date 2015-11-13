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
                          )))))))

(defn keyword-entry
  "creates a map describing a keyword for entry in the database"
  [keywordline]
  (println (str "_id "(first keywordline) " sub " (second keywordline))))

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
