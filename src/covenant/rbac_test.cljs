(ns covenant.rbac-test
 (:require
   covenant.rbac
   [covenant.test :refer [is-valid is-invalid]]
   [cljs.test :refer-macros [deftest]]))

;; RBAC Tests ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest ??roles-rbac
  (let [rand-coll (fn [c] ((rand-nth [vec seq set]) c))
        roles (fn [& rs] {:roles (rand-coll rs)})
        admin  [(roles :admin)]
        editor [(roles :editor)]
        both   [(roles :editor :admin)]
        none   ['() [] #{} {} (roles)]]
    ;; data containd :admin role
    (is-valid admin admin)
    ;; data containd :admin and :editor roles
    (is-valid both editor)
    ;; no roles required, data has multiple roles
    (is-valid none both)
    ;; requires :admin role, data has :editor role
    (is-invalid admin editor)
    ;; requires :editor role, data has no roles
    (is-invalid editor none)
    ;; requires :admin and :editor roles, data has no roles
    (is-invalid both none)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
