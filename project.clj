(defproject keywords-mongo "0.1.0-SNAPSHOT"
  :description "Convert a file of keywords exported from Aperture into a mongo collection"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.novemberain/monger "3.0.1"]
                 [org.clojure/tools.cli "0.3.3"]]
  :main keywords-mongo.core
  :bin {:name "save-keywords"
        :bin-path "~/bin"})
