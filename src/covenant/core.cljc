(ns covenant.core
  (:refer-clojure :exclude [assert])
  (:require
    #?(:clj [clojure.spec.alpha :as spec]
       :cljs [cljs.spec.alpha :as spec])))

;; Basic Covenants ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(spec/def ::fn      fn?)

(spec/def ::any     any?)

(spec/def ::map     map?)

(spec/def ::nil     nil?)

(spec/def ::set     set?)

(spec/def ::char    char?)

(spec/def ::list    list?)

(spec/def ::bool    boolean?)

(spec/def ::number  number?)

(spec/def ::string  string?)

(spec/def ::symbol  symbol?)

(spec/def ::vector  vector?)

(spec/def ::keyword keyword?)

(spec/def ::object  object?)

(spec/def ::empty   empty?)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol ICovenant
  "Provides an abstraction for validating data using clojure.spec based on a covenant."
  (assert   [covenant data & opts] "See clojure.spec/assert.")
  (conform  [covenant data & opts] "See clojure.spec/conform.")
  (explain  [covenant data & opts] "See clojure.spec/explain.")
  (problems [covenant data & opts] "See clojure.spec/explain-data.")
  (validate [covenant data & opts] "See clojure.spec/valid?.")
  (spec     [covenant & opts]      "Returns related spec for `covenant`."))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn covenant*
  "Returns a spec from `covenant` or `default`"
  [covenant & [default]]
  (or (spec/get-spec covenant) default))

(defn covenant-equal
  "Returns a spec where `data` equals `covenant`."
  [covenant]
  (fn [data]
    (= covenant data)))

(defn covenant-contains
  "Returns a spec where items within `covenant` contain `data`."
  [covenant]
  (fn [data]
    (let [data (if (coll? data) data (list data))]
      (some covenant data))))

(defn covenant-empty
  "Returns a spec where `covenant` and `data` are checked for being empty."
  [covenant]
  (fn [data]
    (= (empty? covenant) (empty? data))))

(defmulti covenant-multi identity :default ::default)

(defmethod covenant-multi ::default
  [covenant [k v]]
  (covenant* (get covenant k)))

(defn covenant-kv [covenant]
  (spec/or
    :kv-spec
      (partial covenant-multi covenant)
      ;(spec/multi-spec covenant-multi identity)
    :kv-equal
      (fn [[k v]]
        (let [cov (get covenant k)]
          (= cov v)))
    :kv-contains
      (fn [[k v]]
        (let [cov (get covenant k)]
          (some (set cov) v)))))

(defn covenant-keys [covenant]
  (fn [data]
    (let [cov (keys covenant)
          dat (keys data)]
      (when-not (empty? cov)
        (some (set cov) (set dat))))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(extend-protocol ICovenant

  default
  (assert   [covenant data & opts]
    (spec/assert (spec covenant opts) data))
  (conform  [covenant data & opts]
    (spec/conform (spec covenant opts) data))
  (explain  [covenant data & opts]
    (spec/explain (spec covenant opts) data))
  (problems  [covenant data & opts]
    (spec/explain-data (spec covenant opts) data))
  (validate [covenant data & opts]
    (spec/valid? (spec covenant opts) data))

  object
  (spec [covenant & opts]
    (covenant* covenant ::object))

  nil
  (spec [covenant & opts]
    (covenant* covenant ::nil))

  number
  (spec [covenant & opts]
    (covenant* covenant ::number))

  char
  (spec [covenant & opts]
    (covenant* covenant ::char))

  string
  (spec [covenant & opts]
    (covenant* covenant ::string))

  boolean
  (spec [covenant & opts]
    (covenant* covenant ::bool))

  Keyword
  (spec [covenant & opts]
    (covenant* covenant ::keyword))

  Symbol
  (spec [covenant & opts]
    (covenant* covenant ::symbol))

  function
  (spec [covenant & opts]
    ;; https://github.com/degree9/covenant/issues/25
    ::fn)

  EmptyList
  (spec [covenant & opts]
    ::list)
    ;(spec/and ::list
    ;  (covenant-equal covenant))

  List
  (spec [covenant & opts]
    ::list)
    ;(spec/and ::list
    ;  (spec/or
    ;    :equal (covenant-equal covenant)
    ;    :contents (spec/coll-of (covenant-contains covenant)))

  PersistentVector
  (spec [covenant & opts]
    ::vector)
    ;(spec/and ::vector
    ;  (spec/or
    ;    :equal (covenant-equal covenant)
    ;    :contents (spec/coll-of (covenant-contains covenant)))

  PersistentHashSet
  (spec [covenant & opts]
    ::set)
    ;(spec/and ::set
    ;  (spec/or
    ;    :equal (covenant-equal covenant)
    ;    :contents (spec/coll-of (covenant-contains covenant)))

  PersistentArrayMap
  (spec [covenant & opts]
    ::map))
    ;(spec/and ::map
    ;  (spec/merge)))
    ;    (covenant-keys covenant))))
      ;(covenant-empty covenant))))
      ;(spec/coll-of))))
        ;(fn [data] (prn data) true)))))
        ;(covenant-kv covenant)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
