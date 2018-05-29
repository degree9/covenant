(ns covenant.schema
 (:require
  [clojure.spec.alpha :as spec]
  [covenant.core :as core]))

(extend-protocol core/ICovenant

  nil
  (-spec [covenant]
    ;; nil cannot be anything other than nil
    ::core/nil)

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs number :clj java.lang.Long)
  (-spec [covenant]
    ;; number must be a number and equal
    (spec/and ::core/number
      (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs string :clj java.lang.String)
  (-spec [covenant]
    ;; string must be a string and equal
    (spec/and ::core/string
      (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs char :clj java.lang.Character)
  (-spec [covenant]
   ;; char must be a char and equal
   (spec/and ::core/char
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs Keyword :clj clojure.lang.Keyword)
  (-spec [covenant]
    ;; keyword could be a named spec
    (core/covenant-spec covenant
      ;; otherwise must be a keyword and equal
      (spec/and ::core/keyword
        (core/covenant-equal covenant))))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs Symbol :clj clojure.lang.Symbol)
  (-spec [covenant]
   ;; symbol could be a named spec
   (core/covenant-spec covenant
    ;; otherwise must be a symbol and equal
    (spec/and ::core/symbol
     (core/covenant-equal covenant))))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs object :clj java.lang.Object)
  (-spec [covenant]
   ;; object must be an object and equal
   (spec/and ::core/object
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs function :clj clojure.lang.IFn)
  (-spec [covenant]
   ;; function must be a function and equal
   (spec/and ::core/fn
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs EmptyList :clj clojure.lang.PersistentList$EmptyList)
  (-spec [covenant]
    ;; emptylist must be both a list and empty
    (spec/and ::core/list ::core/empty))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs List :clj clojure.lang.PersistentList)
  (-spec [covenant]
    ;; list must be a list and equal
    (spec/and ::core/list
      (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs PersistentArrayMap :clj clojure.lang.PersistentArrayMap)
  (-spec [covenant]
   ;; map must be a map and equal
   (spec/and ::core/map
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs PersistentHashMap :clj clojure.lang.PersistentHashMap)
  (-spec [covenant]
   ;; map must be a map and equal
   (spec/and ::core/map
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs PersistentHashSet :clj clojure.lang.PersistentHashSet)
  (-spec [covenant]
   ;; map must be a map and equal
   (spec/and ::core/set
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data))

  #?(:cljs PersistentVector :clj clojure.lang.PersistentVector)
  (-spec [covenant]
   ;; list must be a list and equal
   (spec/and ::core/vector
    (core/covenant-equal covenant)))

  ; boilerplate needed for clj...
  (-assert [covenant data]
   (spec/assert (core/spec covenant) data))
  (-conform [covenant data]
   (spec/conform (core/spec covenant) data))
  (-explain [covenant data]
   (spec/explain (core/spec covenant) data))
  (-problems [covenant data]
   (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
   (spec/valid? (core/spec covenant) data)))

#?(:clj
   (extend-protocol core/ICovenant
    java.lang.Double
    (-spec [covenant]
      ;; number must be a number and equal
      (spec/and ::core/number
        (core/covenant-equal covenant)))

    ; boilerplate needed for clj...
    (-assert [covenant data]
     (spec/assert (core/spec covenant) data))
    (-conform [covenant data]
     (spec/conform (core/spec covenant) data))
    (-explain [covenant data]
     (spec/explain (core/spec covenant) data))
    (-problems [covenant data]
     (spec/explain-data (core/spec covenant) data))
    (-validate [covenant data]
     (spec/valid? (core/spec covenant) data))))

#?(:cljs
   (extend-protocol core/ICovenant
    boolean
    (-spec [covenant]
     ;; bool must be a bool and equal
     (spec/and ::core/bool
      (core/covenant-equal covenant)))

    ; boilerplate needed for clj...
    (-assert [covenant data]
     (spec/assert (core/spec covenant) data))
    (-conform [covenant data]
     (spec/conform (core/spec covenant) data))
    (-explain [covenant data]
     (spec/explain (core/spec covenant) data))
    (-problems [covenant data]
     (spec/explain-data (core/spec covenant) data))
    (-validate [covenant data]
     (spec/valid? (core/spec covenant) data))))
