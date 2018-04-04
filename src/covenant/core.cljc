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
  (assert   [covenant data] "See clojure.spec/assert.")
  (conform  [covenant data] "See clojure.spec/conform.")
  (explain  [covenant data] "See clojure.spec/explain.")
  (problems [covenant data] "See clojure.spec/explain-data.")
  (validate [covenant data] "See clojure.spec/valid?.")
  (spec     [covenant]      "Returns related spec for `covenant`."))
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

(defn covenant-kv [covenant]
  (spec/or
    :kv-equal
      (fn [[k v]] (= (get covenant k) v))
    :kv-contains
      (fn [[k v]] (some (set (get covenant k)) v))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(extend-protocol ICovenant

  default
  (assert   [covenant data]
    (spec/assert  (spec covenant) data))
  (conform  [covenant data]
    (spec/conform (spec covenant) data))
  (explain  [covenant data]
    (spec/explain (spec covenant) data))
  (problems  [covenant data]
    (spec/explain-data (spec covenant) data))
  (validate [covenant data]
    (spec/valid?  (spec covenant) data))

  object
  (spec [covenant]
    (covenant* covenant ::object))

  nil
  (spec [covenant]
    (covenant* covenant ::nil))

  number
  (spec [covenant]
    (covenant* covenant ::number))

  char
  (spec [covenant]
    (covenant* covenant ::char))

  string
  (spec [covenant]
    (covenant* covenant ::string))

  boolean
  (spec [covenant]
    (covenant* covenant ::bool))

  Keyword
  (spec [covenant]
    (covenant* covenant ::keyword))

  Symbol
  (spec [covenant]
    (covenant* covenant ::symbol))

  function
  (spec [covenant]
    (spec/and ::fn
      (covenant-equal covenant)))

  EmptyList
  (spec [covenant]
    (spec/and ::list
      (covenant-equal covenant)))

  List
  (spec [covenant]
    (spec/and ::list
      (spec/or
        :equal (covenant-equal covenant)
        :contents (spec/coll-of (covenant-contains covenant)))))

  PersistentVector
  (spec [covenant]
    (spec/and ::vector
      (spec/or
        :equal (covenant-equal covenant)
        :contents (spec/coll-of (covenant-contains covenant)))))

  PersistentHashSet
  (spec [covenant]
    (spec/and ::set
      (spec/or
        :equal (covenant-equal covenant)
        :contents (spec/coll-of (covenant-contains covenant)))))

  PersistentArrayMap
  (spec [covenant]
    (spec/and ::map
      (covenant-empty covenant)
      (spec/coll-of
        (covenant-kv covenant)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
