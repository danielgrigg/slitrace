(defproject slitrace "0.2.4"
  :description "A suite of ray-tracing tools."
  :url "http://sliplanesoftware.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "LATEST"]
                 [sligeom "LATEST"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]]
                  :plugins [[lein-midje "3.1.0"]]}
                   })
