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

#?(:cljs (spec/def ::object  object?))

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
  (-assert   [covenant data ] "See clojure.spec/assert.")
  (-conform  [covenant data ] "See clojure.spec/conform.")
  (-explain  [covenant data ] "See clojure.spec/explain.")
  (-problems [covenant data ] "See clojure.spec/explain-data.")
  (-validate [covenant data ] "See clojure.spec/valid?.")
  (-spec     [covenant ]      "Returns related spec for `covenant`."))

(defn assert [covenant data]
  (-assert covenant data))

(defn conform [covenant data]
  (-conform covenant data))

(defn explain [covenant data]
  (-explain covenant data))

(defn problems [covenant data]
  (-problems covenant data))

(defn validate [covenant data]
  (-validate covenant data))

(defn spec [covenant]
  (-spec covenant))

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
