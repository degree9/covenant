(ns covenant.access-test
 (:require
  covenant.core
  [cljs.test :refer-macros [deftest is]]))

(deftest ??admin-role
 ; simplest case, a set of roles should validate against itself
 (is (covenant.core/validate {:roles #{:admin}} {:roles #{:admin}}))

 ; adding an extra role should not invalidate access
 (is (covenant.core/validate {:roles #{:admin}} {:roles #{:admin :editor}}))

 ; removing the admin role should not validate
 (doseq [t [{} {:roles #{}} {:roles #{:editor}}]]
  (is
   (not (covenant.core/validate {:roles #{:admin}} t))
   (str (pr-str t) " validated as admin"))))
