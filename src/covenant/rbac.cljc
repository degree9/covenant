(ns covenant.rbac
 (:require
  #?(:clj [clojure.spec.alpha :as spec]
     :cljs [cljs.spec.alpha :as spec])
  [clojure.set :as s]
  [covenant.core :as core]))

(defn covenant-contains
  "Returns a spec where items within `covenant` contain `data`."
  [covenant]
  (fn [data]
    (let [data (if (coll? data) data (list data))]
      (some covenant data))))

(defn covenant-optional
  "Returns a spec where `data` is optional."
  [covenant]
  (fn [data]
    (constantly true)))

(defmulti covenant-multi identity :default ::default)

(defmethod covenant-multi ::default
  [covenant [k v]]
  (core/covenant-spec (get covenant k)))

(defn covenant-keys
  "Returns a spec where keys within `covenant` are also within `data`."
  [covenant]
  (fn [data]
    (s/subset?
      (set (keys covenant))
      (set (keys data)))))

(defn covenant-kv
  "Returns a spec where `data` is `key` `val`."
  [covenant]
  (spec/or
    :kv-missing
    (fn [[k v]]
      (when-not (get covenant k)
        (constantly true)))
    :kv-empty
    (fn [[k v]]
      (when (empty? (get covenant k))
        (constantly true)))
    :kv-spec
    (partial covenant-multi covenant)
    :kv-equal
    (fn [[k v]]
      (let [cov (get covenant k)]
        (= cov v)))
    :kv-contains
    (fn [[k v]]
      (let [cov (get covenant k)]
        (some (set cov) v)))))

(extend-protocol core/ICovenant

  #?(:cljs default :clj java.lang.Object)
  (-assert   [covenant data]
    (spec/assert (core/spec covenant) data))
  (-conform  [covenant data]
    (spec/conform (core/spec covenant) data))
  (-explain  [covenant data]
    (spec/explain (core/spec covenant) data))
  (-problems  [covenant data]
    (spec/explain-data (core/spec covenant) data))
  (-validate [covenant data]
    (spec/valid? (core/spec covenant) data))

  nil
  (-spec [covenant]
    ;; nil cannot be anything other than nil
    ::core/nil)

  ; ; number
  ; ; (-spec [covenant]
  ; ;   ;; number must be a number and equal
  ; ;   (spec/and ::core/number
  ; ;     (core/covenant-equal covenant)))
  ;
  ; string
  ; (-spec [covenant]
  ;   ;; string must be a string and equal
  ;   (spec/and ::core/string
  ;     (core/covenant-equal covenant)))
  ;
  ; char
  ; (-spec [covenant]
  ;   ;; char must be a char and equal
  ;   (spec/and ::core/char
  ;     (core/covenant-equal covenant)))
  ;
  ; boolean
  ; (-spec [covenant]
  ;   ;; bool must be a bool and equal
  ;   (spec/and ::core/bool
  ;     (core/covenant-equal covenant)))
  ;
  ; Keyword
  ; (-spec [covenant]
  ;   ;; keyword could be a named spec
  ;   (core/covenant-spec covenant
  ;     ;; otherwise must be a keyword and equal
  ;     (spec/and ::core/keyword
  ;       (core/covenant-equal covenant))))
  ;
  ; Symbol
  ; (-spec [covenant]
  ;   ;; symbol could be a named spec
  ;   (core/covenant-spec covenant
  ;     ;; otherwise must be a symbol and equal
  ;     (spec/and ::core/symbol
  ;       (core/covenant-equal covenant))))
  ;
  ; ;object
  ; ;(-spec [covenant]
  ;   ;; object must be an object and equal
  ; ;  (spec/and ::core/object
  ; ;    (core/covenant-equal covenant)))
  ;
  ; function
  ; (-spec [covenant]
  ;   ;; function must be a function and equal
  ;   (spec/and ::core/fn
  ;     (core/covenant-equal covenant)))
  ;
  ; EmptyList
  ; (-spec [covenant]
  ;   (spec/or
  ;     :optional (covenant-optional covenant)
  ;     :contains (covenant-contains covenant)))
  ;
  ; List
  ; (-spec [covenant]
  ;   (spec/or
  ;     :optional (covenant-optional covenant)
  ;     :contains (covenant-contains covenant)))
  ;
  ; PersistentVector
  ; (-spec [covenant]
  ;   (spec/or
  ;     :optional (covenant-optional covenant)
  ;     :contains (covenant-contains covenant)))
  ;
  ; PersistentHashSet
  ; (-spec [covenant]
  ;   (spec/or
  ;     :optional (covenant-optional covenant)
  ;     :contains (covenant-contains covenant)))
  ;
  #?(:cljs PersistentArrayMap :clj clojure.lang.PersistentArrayMap)
  (-spec [covenant]
    ;; map must be a map and entries are spec'd
    (spec/and ::core/map
      (covenant-keys covenant)
      (spec/or
        :coll-of (spec/coll-of (covenant-kv covenant))))))
