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
  (-assert   [covenant data opts] "See clojure.spec/assert.")
  (-conform  [covenant data opts] "See clojure.spec/conform.")
  (-explain  [covenant data opts] "See clojure.spec/explain.")
  (-problems [covenant data opts] "See clojure.spec/explain-data.")
  (-validate [covenant data opts] "See clojure.spec/valid?.")
  (-spec     [covenant opts]      "Returns related spec for `covenant`."))

(defn assert [covenant data & opts]
  (-assert covenant data opts))

(defn conform [covenant data & opts]
  (-conform covenant data opts))

(defn explain [covenant data & opts]
  (-explain covenant data opts))

(defn problems [covenant data & opts]
  (-problems covenant data opts))

(defn validate [covenant data & opts]
  (-validate covenant data opts))

(defn spec [covenant & opts]
  (-spec covenant opts))

(extend-protocol ICovenant

  default
  (-assert   [covenant data opts]
    (spec/assert (spec covenant opts) data))
  (-conform  [covenant data opts]
    (spec/conform (spec covenant opts) data))
  (-explain  [covenant data opts]
    (spec/explain (spec covenant opts) data))
  (-problems  [covenant data opts]
    (spec/explain-data (spec covenant opts) data))
  (-validate [covenant data opts]
    (spec/valid? (spec covenant opts) data))

  object
  (-spec [covenant opts]
    ::object)

  nil
  (-spec [covenant opts]
    ::nil)

  number
  (-spec [covenant opts]
    ::number)

  char
  (-spec [covenant opts]
    ::char)

  string
  (-spec [covenant opts]
    ::string)

  boolean
  (-spec [covenant opts]
    ::bool)

  Keyword
  (-spec [covenant opts]
    ::keyword)

  Symbol
  (-spec [covenant opts]
    ::symbol)

  function
  (-spec [covenant opts]
    ::fn)

  EmptyList
  (-spec [covenant opts]
    ::empty)

  List
  (-spec [covenant opts]
    ::list)

  PersistentVector
  (-spec [covenant opts]
    ::vector)

  PersistentHashSet
  (-spec [covenant opts]
    ::set)

  PersistentArrayMap
  (-spec [covenant opts]
    ::map))
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
