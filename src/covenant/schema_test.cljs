(ns covenant.schema-test
 (:require
  covenant.schema
  clojure.set
  [covenant.test :refer [is-valid is-invalid]]
  [cljs.test :refer-macros [deftest]]))

;; Schema Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest ??anything
  (let [covs     #{:covenant.core/any}
        valids   #{nil 1 true "foo" :bar 'sym '() [] #{} {}}]
    (is-valid covs valids)))

(deftest ??boolean
  (let [covs     #{:covenant.core/bool}
        valids   #{true false}
        invalids #{nil 1 "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??number
  (let [covs     #{:covenant.core/number}
        valids   #{-1 0 1 2 2.5 0.0 js/Infinity}
        invalids #{nil true "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??nil
  (let [covs     #{:covenant.core/nil}
        valids   #{nil}
        invalids #{1 true "foo" :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??string
  (let [covs     #{:covenant.core/string}
        valids   #{"bar" ""}
        invalids #{1 true nil :bar '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??char
  (let [covs     #{:covenant.core/char}
        valids   #{\b \c}
        invalids #{1 true nil :bar 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??keyword
  (let [covs     #{:covenant.core/keyword}
        valids   #{:bar :foo/bar}
        invalids #{1 true nil 'sym "baz" '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??symbol
  (let [covs     #{:covenant.core/symbol}
        valids   #{'bar 'foo/bar 'foo.bar/baz}
        invalids #{1 true nil :baz "baz" '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??object
  (let [covs     #{:covenant.core/object}
        valids   #{#js{} (js-obj) (clj->js {:foo :bar})}
        invalids #{1 true nil :bar "baz" 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??function
  (let [covs     #{:covenant.core/fn}
        valids   #{#() identity}
        invalids #{1 true nil :bar "baz" 'sym '() [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??list
  (let [covs     #{:covenant.core/list}
        valids   #{'() (list) (list :foo :bar) (list 1 2)}
        invalids #{1 true nil :bar "baz" 'sym [] #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??vector
  (let [covs     #{:covenant.core/vector}
        valids   #{[] (vector) (vector :foo :bar) [1 2]}
        invalids #{1 true nil :bar "baz" 'sym (list) #{} {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??set
  (let [covs     #{:covenant.core/set}
        valids   #{#{} (hash-set) (hash-set :foo :bar) #{1 2}}
        invalids #{1 true nil :bar "baz" 'sym (list) [] {}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))

(deftest ??map
  (let [covs     #{:covenant.core/map}
        valids   #{{} (hash-map) {:foo :bar} {"foo" "bar"}}
        invalids #{1 true nil :bar "baz" 'sym (list) [] #{}}]
    (is-valid   covs   valids)
    (is-invalid valids invalids)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
