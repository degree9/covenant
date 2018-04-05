(ns covenant.access-test
 (:require
  covenant.core
  [cljs.test :refer-macros [deftest is]]))

(deftest ??admin-role
 (let [admin {:roles #{:admin}}
       editor {:roles #{:editor}}
       editor+admin {:roles #{:admin :editor}}

       empty-map {}
       empty-set #{}
       empty-vec []
       empty-list '()

       no-role {:roles #{}}
       nil-role {:roles nil}

       empties [empty-map empty-set empty-vec empty-list]

       ; takes a list with set roles and extends it to have the same entries in
       ; vectors and lists
       with-seqs (fn [xs]
                  (into xs
                   (flatten
                    (let [fns [sequence vec]]
                     (map
                      (fn [f]
                       (map
                        (fn [x] (update x :roles f))
                        xs))
                      fns)))))

       no-roles (with-seqs [no-role nil-role])
       admins (with-seqs [admin editor+admin])
       editors (with-seqs [editor editor+admin])
       everything (concat empties no-roles admins editors)
       not-empties (remove (set empties) everything)]

  ; valids
  (doseq [[covs vs] [; any empty cov should validate empties
                     ; Matt: this will fail as empty lists still validate coll type
                     ;[empties empties]

                     ; empty roles cov should validate any role
                     ; Matt: this will fail as empty roles still validate coll type
                     ;[no-roles (concat no-roles admins editors)]

                     ; Matt: the solution for this would be to support a spec as
                     ;       the value of a kv pair which is applied to data

                     ; roles in a cov should validate that role
                     [admins admins]
                     [editors editors]]]
   (doseq [cov covs v vs]
    (is
     (covenant.core/validate cov v)
     (covenant.core/problems cov v)))

   ; invalids
   (doseq [[covs vs] [; not empty cov fails an empty val
                      [not-empties empties]

                      ; empties fail anything with any role (including no role)
                      [empties (concat no-roles admins editors)]

                      ; can't validate with missing role
                      [[admin] (concat empties no-roles [editor])]
                      [[editor] (concat empties no-roles [admin])]]]
    (doseq [cov covs v vs]
     (is
      (not (covenant.core/validate cov v))
      (str "value " (pr-str v) " validated against covenant " (pr-str cov))))))))
