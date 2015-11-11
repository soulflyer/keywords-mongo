(ns keywords-mongo.core
  (:require [monger.collection :as mc]
            [monger.core :as mg]
            [monger.operators :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:gen-class))

(defn save-keywords
  "saves all the keywords in file to db"
  [dbase collection file]
  (println file))

(defn -main [& args]
  (save-keywords "monger-test" "keywords" (first args)))
