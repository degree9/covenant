(ns covenant.core)

;; Basic Specs ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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

;; Spec Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn spec*
  "Returns default spec based on `schema`."
  [schema & [spec]]
  (or (spec/get-spec schema) spec))

(defn schema-kv
  "Returns a spec where `schema` is equal to `data` for a kv pair.

   Use :strict true to fail valiation for additional keys."
  [schema & [{:keys [strict]}]]
  (fn [data]
    (let [k (if (vector? data) (key data) data)
          v (if (vector? data) (val data) data)]
      (= v (get schema k (when-not strict v))))))

(defn schema-equal
  "Returns a spec where `data` equals `schema`."
  [schema]
  (fn [data]
    (= schema data)))

(defn schema-spec
  "Returns a spec from `schema` or based on `data` type."
  [schema]
  (fn [data]
    (let [k (if (vector? data) (key data) data)
          v (if (vector? data) (val data) data)]
      (spec* (get schema k v)))))

(defn schema-contains
  "Returns a spec where items within `schema` contain `data`."
  [schema]
  (fn [data]
    (some #{data} schema)))

;; Covenant Protocol ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol ICovenant
  "Provides an abstraction for validating data using clojure.spec based on a data schema."
  (assert   [schema data] "See clojure.spec/assert.")
  (conform  [schema data] "See clojure.spec/conform.")
  (explain  [schema data] "See clojure.spec/explain.")
  (validate [schema data] "See clojure.spec/valid?.")
  (spec     [schema]      "Returns related spec for `schema`."))

(extend-protocol ICovenant

  default
  (assert   [schema data]
    (spec/assert  (spec schema) data))
  (conform  [schema data]
    (spec/conform (spec schema) data))
  (explain  [schema data]
    (spec/explain (spec schema) data))
  (validate [schema data]
    (spec/valid?  (spec schema) data))

  nil
  (spec [schema]
    (spec* schema ::nil))

  number
  (spec [schema]
    (spec* schema ::number))

  char
  (spec [schema]
    (spec* schema ::char))

  string
  (spec [schema]
    (spec* schema ::string))

  boolean
  (spec [schema]
    (spec* schema ::bool))

  Keyword
  (spec [schema]
    (spec* schema ::keyword))

  Symbol
  (spec [schema]
    (spec* schema ::symbol))

  function
  (spec [schema]
    (spec/and ::fn
      (schema-equal schema)))

  List
  (spec [schema]
    (spec/and ::list
      (spec/coll-of
        (spec/and
          (schema-spec schema)
          (schema-contains schema)))))

  PersistentVector
  (spec [schema]
    (spec/and ::vector
      (spec/coll-of
        (spec/and
          (schema-spec schema)
          (schema-contains schema)))))

  PersistentHashSet
  (spec [schema]
    (spec/and ::set
      (spec/coll-of
        (spec/and
          (schema-spec schema)
          (schema-contains schema)))))

  PersistentArrayMap
  (spec [schema]
    (spec/merge ::map
      (spec/keys :req-un (keys schema))
      (spec/coll-of
        (spec/or
          :spec (schema-spec schema)
          :kv   (schema-kv schema))))))

;(explain {:test "sup"} {:test "sup"})
;(explain {:go "have" :some ['fun]} {:go "have" :some ['soup]})
