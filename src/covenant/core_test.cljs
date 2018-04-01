(ns covenant.core-test
 (:require
  covenant.core
  clojure.set
  [cljs.test :refer-macros [deftest is are]]))

(def bools #{true false})
(def numbers #{-1 0 1 2 2.5 0.0 js/Infinity})
(def falseys #{nil false})
(def strings #{"foo" ""})
(def chrs #{\a \b \c})
(def objects #{(js-obj) (clj->js {:foo :bar})})
(def maps #{{:foo :bar} {1 2} {:foo :foo} {}})
(def sets #{#{:foo} #{} #{1}})
(def vectors #{[] [:foo :bar] [1 2]})
(def lists #{(list) (list :foo :bar) (list 1 2)})
(def fns #{#() (fn [] :foo)})
(def everything
 (clojure.set/union
  bools
  numbers
  falseys
  strings
  chrs
  objects
  maps
  sets
  vectors
  lists
  fns))

(defn check-validate
 ([covenant valids]
  (check-validate covenant valids
   ; everything that is not valid is invalid
   (clojure.set/difference
    everything
    valids)))
 ([covenant valids invalids]
  (doseq [v valids]
   (is
    (covenant.core/validate covenant v)
    (str "Failed to validate " (pr-str v) " against covenant " covenant)))

  (doseq [v invalids]
   (is
    (not (covenant.core/validate covenant v))
    (str "Failed to invalidate " (pr-str v) " against covenant " covenant)))))

(deftest ??fn
 (check-validate :covenant.core/fn fns))

(deftest ??any
 (check-validate :covenant.core/any everything))

(deftest ??map
 (check-validate :covenant.core/map maps))

(deftest ??nil
 (check-validate :covenant.core/nil #{nil}))

(deftest ??set
 (check-validate :covenant.core/set sets))

(deftest ??char
 (check-validate :covenant.core/char chrs))
