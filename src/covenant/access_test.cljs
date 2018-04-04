(ns covenant.access-test
 (:require
  covenant.core
  [cljs.test :refer-macros [deftest is]]))

(deftest ??admin-role
 ; simplest case, a set of roles should validate against itself
 (covenant.core/explain {:roles #{:admin}} {:roles #{:admin}})
 (is
   (covenant.core/validate {:roles #{:admin}} {:roles #{:admin}})
   (str (pr-str {:roles #{:admin}}) " did not validate as admin"))

 ; adding an extra role should not invalidate access
 (covenant.core/explain {:roles #{:admin}} {:roles #{:admin :editor}})
 (is
   (covenant.core/validate {:roles #{:admin}} {:roles #{:admin :editor}})
   (str (pr-str {:roles #{:admin :editor}}) " did not validate as admin"))

 ; removing the admin role should not validate
 (doseq [t [{} {:roles #{}} {:roles #{:editor}}]]
  (is
   (not (covenant.core/validate {:roles #{:admin}} t))
   (str (pr-str t) " validated as admin"))))
