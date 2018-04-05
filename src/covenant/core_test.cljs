(ns covenant.core-test
 (:require
  covenant.core
  clojure.set
  [cljs.test :refer-macros [deftest is]]))

;; Test Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn is-valid [covenants valids]
  (doseq [c covenants v valids]
    (is (covenant.core/validate c v)
      (str "Covenant: " (pr-str c) " did not validate: " (pr-str v)))))

(defn is-invalid [covenants invalids]
  (doseq [c covenants i invalids]
    (is (not (covenant.core/validate c i))
      (str "Covenant: " (pr-str c) " validated: " (pr-str i)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest ??anything
  (let [covs     #{:covenant.core/any}
        valids   #{nil 1 true "foo" :bar 'sym '() [] #{} {}}]
    (is-valid covs valids)))

(deftest ??boolean
  (let [covs     #{:covenant.core/bool true false}
        valids   #{true false}
        invalids #{nil 1 "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??number
  (let [covs     #{:covenant.core/number -1 0 1 1.5}
        valids   #{-1 0 1 2 2.5 0.0 js/Infinity}
        invalids #{nil true "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??nil
  (let [covs     #{:covenant.core/nil nil}
        valids   #{nil}
        invalids #{1 true "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??string
  (let [covs     #{:covenant.core/string "foo"}
        valids   #{"bar" ""}
        invalids #{1 true nil :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??char
  (let [covs     #{:covenant.core/char \a}
        valids   #{\b \c}
        invalids #{1 true nil :bar 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??keyword
  (let [covs     #{:covenant.core/keyword :foo}
        valids   #{:bar :foo/bar}
        invalids #{1 true nil 'sym "baz" '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??symbol
  (let [covs     #{:covenant.core/symbol 'foo}
        valids   #{'bar 'foo/bar 'foo.bar/baz}
        invalids #{1 true nil :baz "baz" '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??object
  (let [covs     #{:covenant.core/object #js{}}
        valids   #{#js{} (js-obj) (clj->js {:foo :bar})}
        invalids #{1 true nil :bar "baz" 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??function
  (let [covs     #{:covenant.core/fn #()}
        valids   #{#() (fn [] :foo)}
        invalids #{1 true nil :bar "baz" 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??list
  (let [covs     #{:covenant.core/list '()}
        valids   #{'() (list) (list :foo :bar) (list 1 2)}
        invalids #{1 true nil :bar "baz" 'sym [] #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??vector
  (let [covs     #{:covenant.core/vector []}
        valids   #{[] (vector) (vector :foo :bar) [1 2]}
        invalids #{1 true nil :bar "baz" 'sym (list) #{} {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??set
  (let [covs     #{:covenant.core/set #{}}
        valids   #{#{} (hash-set) (hash-set :foo :bar) #{1 2}}
        invalids #{1 true nil :bar "baz" 'sym (list) [] {}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))

(deftest ??map
  (let [covs     #{:covenant.core/map {}}
        valids   #{{} (hash-map) {:foo :bar} {"foo" "bar"}}
        invalids #{1 true nil :bar "baz" 'sym (list) [] #{}}]
    (is-valid   covs   valids)
    (is-valid   valids valids)
    (is-invalid covs   invalids)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
