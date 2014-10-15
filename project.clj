(defproject rs.cors "0.1.0-SNAPSHOT"
  :description "bare bones CORS example with friend auth"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring "1.3.1"]
                 [compojure "1.1.9"]
                 [com.cemerick/friend "0.2.1"]
                 [ring-cors "0.1.4"]
                 [lib-noir "0.9.0"]]
  :profiles {:server {:ring {:handler rs.cors/server-handler}}
             :client {:ring {:handler rs.cors/client-handler}}})
