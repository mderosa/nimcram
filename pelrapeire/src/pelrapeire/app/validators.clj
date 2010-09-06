(ns pelrapeire.app.validators)

(defn revision? [^String rev]
  (if (and rev (re-find #"^\d-[0-9a-f]{32}$" rev))
    true
    false))

