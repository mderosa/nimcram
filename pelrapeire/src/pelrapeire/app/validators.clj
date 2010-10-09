(ns pelrapeire.app.validators)

(defn revision? [^String rev]
  (if (and rev (re-find #"^\d+-[0-9a-f]{32}$" rev))
    true
    false))

(defn id? [^String id]
  (if (and id (re-find #"^[0-9a-f]{32}$" id))
    true
    false))

(defn email? [^String email]
  (if (and email (re-find #"^\S+@\S+\.[a-zA-Z]{2,3}$" email))
    true
    false))

