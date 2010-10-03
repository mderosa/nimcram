(defproject compjtest "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.3.0-alpha1"]
                 [org/clojure/contrib/complete "1.3.0-alpha1"]
		 [compojure/compojure "0.5.1"]
		 [ring/ring-jetty-adapter "0.3.0"]
		 [ring/ring-devel "0.3.0"]
		 [hiccup/hiccup "0.2.3"]
		 [org.apache.httpcomponents/httpcore "4.0.1"]
		 [org.apache.httpcomponents/httpclient "4.0.1"]
		 [joda-time/joda-time "1.6"]]
  :dev-dependencies [[swank-clojure/swank-clojure "1.3.0-SNAPSHOT"]])