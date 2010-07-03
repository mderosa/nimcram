
(ns pelrapeire.repository.db.uuid
  (:import java.util.UUID))

(defn uuid []
  (. (. (UUID/randomUUID) toString) replace "-" ""))
