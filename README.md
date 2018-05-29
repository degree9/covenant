# covenant #
[![Clojars Project](https://img.shields.io/clojars/v/degree9/covenant.svg)](https://clojars.org/degree9/covenant)
[![Dependencies Status](https://jarkeeper.com/degree9/covenant/status.svg)](https://jarkeeper.com/degree9/covenant)
[![Downloads](https://jarkeeper.com/degree9/covenant/downloads.svg)](https://jarkeeper.com/degree9/covenant)
[![CircleCI](https://circleci.com/gh/degree9/covenant/tree/master.svg?style=shield)](https://circleci.com/gh/degree9/covenant/tree/master)
<!-- [![Medium](https://img.shields.io/badge/medium-read-blue.svg)](https://medium.com/degree9/boot-covenant-e1453826b732) -->

Access Control and Data Validation for Clojure(Script) written in Clojure Spec.

> A covenant, is a solemn promise to engage in or refrain from a specified action. - [Wikipedia](https://en.wikipedia.org/wiki/Covenant_(law))

Covenant is divided into a few namespaces:
- `covenant.core` ICovenant protocol and public api.
- `covenant.schema` type/value based data comparison on top of clojure.spec.
- `covenant.acl` provides covenants around Access Control List's (ACL).
- `covenant.rbac` provides covenants around Role Based Access Control (RBAC).
- `covenant.abac` provides covenants around Attribute Based Access Control (ABAC).

## Usage ##

### `ICovenant` Protocol

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

### RBAC

`covenant.rbac` provides validation fns that are "loose" for collections.

If the values of the

### Examples

`(:require covenant.core :as covenant)`

For more examples check out the test suite.

A vanilla spec + scalar primitive will pass `covenant/explain`.

```clojure
(covenant/explain :covenant.core/number 1)
; =>
;Success!
```
