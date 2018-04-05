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

;; Covenant Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn covenant-spec
  "Returns a spec from `covenant` or `default`"
  [covenant & [default]]
  (or (spec/get-spec covenant) default))

(defn covenant-equal
  "Returns a spec where `data` equals `covenant`."
  [covenant]
  (fn [data]
    (= covenant data)))

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

  nil
  (-spec [covenant opts]
    ;; nil cannot be anything other than nil
    ::nil)

  number
  (-spec [covenant opts]
    ;; number must be a number and equal
    (spec/and ::number
      (covenant-equal covenant)))

  string
  (-spec [covenant opts]
    ;; string must be a string and equal
    (spec/and ::string
      (covenant-equal covenant)))

  char
  (-spec [covenant opts]
    ;; char must be a char and equal
    (spec/and ::char
      (covenant-equal covenant)))

  boolean
  (-spec [covenant opts]
    ;; bool must be a bool and equal
    (spec/and ::bool
      (covenant-equal covenant)))

  Keyword
  (-spec [covenant opts]
    ;; keyword could be a named spec
    (covenant-spec covenant
      ;; otherwise must be a keyword and equal
      (spec/and ::keyword
        (covenant-equal covenant))))

  Symbol
  (-spec [covenant opts]
    ;; symbol could be a named spec
    (covenant-spec covenant
      ;; otherwise must be a symbol and equal
      (spec/and ::symbol
        (covenant-equal covenant))))

  object
  (-spec [covenant opts]
    ;; object must be an object and equal
    (spec/and ::object
      (covenant-equal covenant)))

  function
  (-spec [covenant opts]
    ;; function must be a function and equal
    (spec/and ::fn
      (covenant-equal covenant)))

  EmptyList
  (-spec [covenant opts]
    ;; emptylist must be both a list and empty
    (spec/and ::list ::empty))

  List
  (-spec [covenant opts]
    ;; list must be a list and equal
    (spec/and ::list
      (covenant-equal covenant)))

  map
  (-spec [covenant opts]
    ;; map must be a map and equal
    (spec/and ::map
      (covenant-equal covenant))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Covenant Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(defn covenant-contains
;  "Returns a spec where items within `covenant` contain `data`."
;  [covenant]
;  (fn [data]
;    (let [data (if (coll? data) data (list data))]
;      (some covenant data))))

;(defn covenant-empty
;  "Returns a spec where `covenant` and `data` are checked for being empty."
;  [covenant]
;  (fn [data]
;    (= (empty? covenant) (empty? data))))

;(defmulti covenant-multi identity :default ::default)

;(defmethod covenant-multi ::default
;  [covenant [k v]]
;  (covenant-spec (get covenant k)))

;(defn covenant-kv [covenant]
;  (spec/or
;    :kv-spec
;      (partial covenant-multi covenant))
;      ;(spec/multi-spec covenant-multi identity)
;    :kv-equal
;      (fn [[k v]]
;        (let [cov (get covenant k)]
;          (= cov v)))
;    :kv-contains
;      (fn [[k v]]
;        (let [cov (get covenant k)]
;          (some (set cov) v)))))

;(defn covenant-keys [covenant]
;  (fn [data]
;    (let [cov (keys covenant)
;          dat (keys data))
;      (when-not (empty? cov)
;        (some (set cov) (set dat))))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
