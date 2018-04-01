# covenant #
[![Clojars Project](https://img.shields.io/clojars/v/degree9/covenant.svg)](https://clojars.org/degree9/covenant)
[![Dependencies Status](https://jarkeeper.com/degree9/covenant/status.svg)](https://jarkeeper.com/degree9/covenant)
[![Downloads](https://jarkeeper.com/degree9/covenant/downloads.svg)](https://jarkeeper.com/degree9/covenant)
<!-- [![Medium](https://img.shields.io/badge/medium-read-blue.svg)](https://medium.com/degree9/boot-covenant-e1453826b732) -->

Access Control and Data Validation for Clojure(Script) written in Clojure Spec.

> A covenant, is a solemn promise to engage in or refrain from a specified action. - [Wikipedia](https://en.wikipedia.org/wiki/Covenant_(law))

Covenant is divided into a few namespaces:
- `covenant.core` compares data structures and enables creation of spec's from data.
- `covenant.acl` provides covenants around Access Control List's (ACL)
- `covenant.roles` provides covenants around Role Based Access Control (RBAC)
- `covenant.attributes` provides covenants around Attribute Based Access Control (RBAC)

Within this project the term `covenant` may refer to one the following:

1. A Clojure Spec
2. A Clojure Data Structure
3. Any combination of 1. and 2.

### Notes on Comparison ###

- When comparing primitives, types are compared not values.
- When comparing lists, contents are compared.

## Usage ##

### Protocol fns

Covenant exposes a protocol that closely matches cljs native spec.

```clojure
(defprotocol ICovenant
  "Provides an abstraction for validating data using clojure.spec based on a covenant."
  (assert   [covenant data] "See clojure.spec/assert.")
  (conform  [covenant data] "See clojure.spec/conform.")
  (explain  [covenant data] "See clojure.spec/explain.")
  (problems [covenant data] "See clojure.spec/explain-data.")
  (validate [covenant data] "See clojure.spec/valid?.")
  (spec     [covenant]      "Returns related spec for `covenant`."))
```

The `spec` function will return a spec based on the passed data.

A simple example is that `(covenant.core/spec nil)` will return
`:covenant.core/nil` which is preregistered with spec as
`(clojure.spec.alpha/def :covenant.core/nil nil?)`.

Scalar primitives simply return a spec/predicate based on their data type while
collections also compare their contents.

### Examples

`(:require covenant.core :as covenant)`

For more examples check out the test suite.

A vanilla spec + scalar primitive will pass `covenant/explain`.

```clojure
(covenant/explain :covenant.core/number 1)
; =>
;Success!
```

Scalar values can be used as a covenant for other values of the same type.

```clojure
(covenant/validate true true) ; true
(covenant/validate true false) ; true
(covenant/validate true 1) ; false
(covenant/validate 1 1) ; true
(covenant/validate 1 0) ; true
(covenant/validate 1 false) ; false
```

A collection is compared against its type _and_ values.

```clojure
(covenant/explain [1 2 3] [4 5 6])
; =>
;In: [0] val: 4 fails predicate: (covenant-spec covenant)
;In: [1] val: 5 fails predicate: (covenant-spec covenant)
;In: [2] val: 6 fails predicate: (covenant-spec covenant)

(covenant/explain {:go "have" :some ['fun]} {:go "have" :some ['soup]} )
; =>
;In: [1] val: [:some [soup]] fails at: [:spec] predicate: (covenant-spec covenant)
;In: [1] val: [:some [soup]] fails at: [:kv] predicate: (covenant-kv covenant)
```
