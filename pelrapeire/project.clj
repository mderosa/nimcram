(defproject hokulea "1.0"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.3.0-alpha1"]
                 [org/clojure/contrib/complete "1.3.0-alpha1" :classifier "bin"]
		 [compojure/compojure "0.5.1"]
		 [ring/ring-core "0.3.0"]
		 [ring/ring-servlet "0.3.0"]
		 [ring/ring-devel "0.3.0"]
		 [hiccup/hiccup "0.2.3"]
		 [org.mortbay.jetty/jetty "6.1.14"]
		 [org.mortbay.jetty/jetty-util "6.1.14"]
		 [org.apache.httpcomponents/httpcore "4.0.1"]
		 [org.apache.httpcomponents/httpclient "4.0.1"]
		 [joda-time/joda-time "1.6"]
		 [javax.mail/mail "1.4.1"]]
  :dev-dependencies [[swank-clojure/swank-clojure "1.3.0-SNAPSHOT"]]
  :target-dir "target/"
  :test-selectors {:default (fn [v] (not (:integration v)))
		   :integration :integration
		   :all (fn [_] true)})