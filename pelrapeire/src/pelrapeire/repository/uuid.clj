
(ns pelrapeire.repository.uuid
  (:import java.util.UUID))

(defn uuid []
  (. (. (UUID/randomUUID) toString) replace "-" ""))