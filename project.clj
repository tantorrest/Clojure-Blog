(defproject hello-world "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
		 [com.taoensso/carmine "2.0.0-RC1"]
		 [hiccup "1.0.3"]
		 [ring/ring-jetty-adapter "1.1.0"]
]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler hello-world.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
