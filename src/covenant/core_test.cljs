(ns covenant.core-test
 (:require
  covenant.core
  [cljs.test :refer-macros [deftest is are]]))

(deftest ??fn?
 ; valids
 (doseq [v [#()
            (fn [] :foo)]]
  (is (covenant.core/validate :covenant.core/fn v)))

 ; invalids
 (doseq [v [true false
            -1 0 1 2 2.5 0.0 ##NaN js/Infinity
            js/undefined nil
            "foo" ""
            (js-obj) (clj->js {:foo :bar})
            {:foo :bar} #{:foo} [:foo :bar] (list :foo :bar)]]
  (is (not (covenant.core/validate :covenant.core/fn v)))))
