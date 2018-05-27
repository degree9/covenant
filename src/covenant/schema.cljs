(ns covenant.schema
  (:require [cljs.spec.alpha :as spec]
            [covenant.core :as core]))

(extend-protocol core/ICovenant

  default
  (-assert   [covenant data]
    (spec/assert (core/spec covenant ) data))
  (-conform  [covenant data]
    (spec/conform (core/spec covenant ) data))
  (-explain  [covenant data]
    (spec/explain (core/spec covenant ) data))
  (-problems  [covenant data]
    (spec/explain-data (core/spec covenant ) data))
  (-validate [covenant data]
    (spec/valid? (core/spec covenant ) data))

  nil
  (-spec [covenant]
    ;; nil cannot be anything other than nil
    ::core/nil)

  number
  (-spec [covenant]
    ;; number must be a number and equal
    (spec/and ::core/number
      (core/covenant-equal covenant)))

  string
  (-spec [covenant]
    ;; string must be a string and equal
    (spec/and ::core/string
      (core/covenant-equal covenant)))

  char
  (-spec [covenant]
    ;; char must be a char and equal
    (spec/and ::core/char
      (core/covenant-equal covenant)))

  boolean
  (-spec [covenant]
    ;; bool must be a bool and equal
    (spec/and ::core/bool
      (core/covenant-equal covenant)))

  Keyword
  (-spec [covenant]
    ;; keyword could be a named spec
    (core/covenant-spec covenant
      ;; otherwise must be a keyword and equal
      (spec/and ::core/keyword
        (core/covenant-equal covenant))))

  Symbol
  (-spec [covenant]
    ;; symbol could be a named spec
    (core/covenant-spec covenant
      ;; otherwise must be a symbol and equal
      (spec/and ::core/symbol
        (core/covenant-equal covenant))))

  object
  (-spec [covenant]
    ;; object must be an object and equal
    (spec/and ::core/object
      (core/covenant-equal covenant)))

  function
  (-spec [covenant]
    ;; function must be a function and equal
    (spec/and ::core/fn
      (core/covenant-equal covenant)))

  EmptyList
  (-spec [covenant]
    ;; emptylist must be both a list and empty
    (spec/and ::core/list ::core/empty))

  List
  (-spec [covenant]
    ;; list must be a list and equal
    (spec/and ::core/list
      (core/covenant-equal covenant)))

  map
  (-spec [covenant]
    ;; map must be a map and equal
    (spec/and ::core/map
      (core/covenant-equal covenant))))
