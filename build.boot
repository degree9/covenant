(set-env!
 :resource-paths #{"src"})

(task-options!
 pom    {:project 'degree9/covenant
         :description "Access Control for Clojure."
         :url "http://github.com/degree9/covenant"
         :scm {:url "http://github.com/degree9/covenant"}})

(deftask deploy
  "Build project for deployment to clojars."
  []
  (comp
    (version)
    (build-jar)
    (push-release)))

(deftask develop
  "Build project for local development."
  []
  (comp
    (version :develop true
             :pre-release 'snapshot)
    (watch)
    (build-jar)))
