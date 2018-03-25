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

`covenant.core :as spec`:

```clojure
(spec/explain :covenant.core/number 1)
; =>
;Success!

(spec/explain 1 2)
; =>
;Success!

(spec/explain [1 2 3] [4 5 6])
; =>
;In: [0] val: 4 fails predicate: (covenant-spec covenant)
;In: [1] val: 5 fails predicate: (covenant-spec covenant)
;In: [2] val: 6 fails predicate: (covenant-spec covenant)

(spec/explain {:go "have" :some ['fun]} {:go "have" :some ['soup]} )
; =>
;In: [1] val: [:some [soup]] fails at: [:spec] predicate: (covenant-spec covenant)
;In: [1] val: [:some [soup]] fails at: [:kv] predicate: (covenant-kv covenant)
```
