(set-env!
  :source-paths #{"src"}
  :dependencies '[
                  ; project deps
                  [congomongo "0.5.0"]
                  [bidi "2.1.2"]
                  [org.clojure/clojure "1.8.0"]
                  [ring/ring "1.6.3"]
                  [instar "1.0.10"]
                  [cheshire "5.8.0"]
                  [org.clojure/core.async "0.3.443"]
                  [aleph "0.4.4"]])

(task-options!
  pom {:project 'server.core
       :version "1.0.0-SNAPSHOT"
       :description "FIXME: write description"}
  aot {:namespace #{'server.core}}
  jar {:main 'server.core})

(require 'server.core)

(deftask run []
  (comp
    (target)
    (with-pass-thru _
      (server.core/dev-main))
      (wait)))

(deftask build []
  (comp
    (aot)
    (pom)
    (uber)
    (jar)
    (target)))

