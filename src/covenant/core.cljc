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
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn covenant*
  "Returns a spec from `covenant` or `default`"
  [covenant & [default]]
  (or (spec/get-spec covenant) default))

(defn covenant-kv
  "Returns a spec where `covenant` is equal to `data` for a kv pair.

   Use :strict true to fail valiation for additional keys."
  [covenant & [{:keys [strict]}]]
  (fn [data]
    (let [k (if (vector? data) (key data) data)
          v (if (vector? data) (val data) data)]
      (= v (get covenant k (when-not strict v))))))

(defn covenant-equal
  "Returns a spec where `data` equals `covenant`."
  [covenant]
  (fn [data]
    (= covenant data)))

(defn covenant-spec
  "Returns a spec from `covenant` or based on `data` type."
  [covenant]
  (fn [data]
    (let [data (if (vector? data) (key data) data)]
      (covenant* (get covenant data) covenant))))

(defn covenant-contains
  "Returns a spec where items within `covenant` contain `data`."
  [covenant]
  (fn [data]
    (some #{data} covenant)))
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

  List
  (spec [covenant]
    (spec/and ::list
      (spec/coll-of
        (spec/or
          :contains (covenant-contains covenant)))))

  PersistentVector
  (spec [covenant]
    (spec/and ::vector
      (spec/coll-of
        (spec/or
          :contains (covenant-contains covenant)))))

  PersistentHashSet
  (spec [covenant]
    (spec/and ::set
      (spec/coll-of
        (spec/and
          (covenant-spec covenant)
          (covenant-contains covenant)))))

  PersistentArrayMap
  (spec [covenant]
    (spec/merge ::map
      (spec/keys :req-un (keys covenant))
      (spec/coll-of
        (spec/or
          :spec (covenant-spec covenant)
          :kv   (covenant-kv covenant))))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
