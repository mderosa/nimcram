(defproject compjtest "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
		 [compojure/compojure "0.4.0-SNAPSHOT"]
		 [ring/ring-jetty-adapter "0.2.M"]
		 [hiccup/hiccup "0.2.3"]
		 [org.apache.httpcomponents/httpcore "4.0.1"]
		 [org.apache.httpcomponents/httpclient "4.0.1"]]
  :dev-dependencies [[swank-clojure/swank-clojure "1.2.0-SNAPSHOT"]])