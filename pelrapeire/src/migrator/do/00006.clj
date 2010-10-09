(ns migrator.do.00006
 ^{:doc "change projects-im-contributing-to to camel case"}
 (:use pelrapeire.repository.dbpelrapeire
       pelrapeire.repository.dbconfig
       pelrapeire.repository.db.dbops))

(defn query-all-users []
  (let [loc "_design/picominmin/_view/users"
	rtn (op-get-view loc db-config)]
    (map #(% "value") (rtn "rows"))))

(defn add-camel-cased-attributes [u]
  (assoc u "projectsImContributingTo" (u "projects-im-contributing-to") "projectsImFollowing" (u "projects-im-following")))

(defn del-noncamel-cased-attributes [u]
  (dissoc u "projects-im-contributing-to" "projects-im-following"))

(defn update-db []
  (doseq [u (map del-noncamel-cased-attributes (map add-camel-cased-attributes (query-all-users)))]
    (println (pel-update u :write))))