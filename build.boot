(set-env!
 :dependencies
 '[[org.clojure/clojure "1.10.0-alpha4"]
   [degree9/boot-semver "1.7.0" :scope "test"]

   ; transitive deps
   [adzerk/boot-cljs "2.1.2"]
   [org.clojure/clojurescript "1.10.238"]
   [doo "0.1.8"]

   [crisptrutski/boot-cljs-test "0.3.5-SNAPSHOT" :scope "test"]]

 :resource-paths #{"src"})

(require
 '[degree9.boot-semver :refer :all]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]])

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

(def test-cljs-options {:process-shim false})

(replace-task!
 [t test-cljs]
 (fn [& xs]
  (apply t
   :cljs-opts test-cljs-options
   xs)))
