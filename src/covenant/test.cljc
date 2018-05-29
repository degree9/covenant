(ns covenant.test
  (:require [covenant.core :as core]
            [cljs.test :refer-macros [is]]))
;; Test Helpers ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn is-valid [covenants valids]
  (doseq [c covenants v valids]
    (is (core/validate c v)
      (str "Covenant: " (pr-str c) " did not validate: " (pr-str v)))))

(defn is-invalid [covenants invalids]
  (doseq [c covenants i invalids]
    (is (not (core/validate c i))
      (str "Covenant: " (pr-str c) " validated: " (pr-str i)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
