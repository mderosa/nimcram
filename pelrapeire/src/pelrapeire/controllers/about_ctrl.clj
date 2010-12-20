(ns pelrapeire.controllers.about-ctrl
  (:use pelrapeire.repository.mailconfig
	pelrapeire.repository.mail.mail))

(defn mail-admin-message [params mail-config]
  )

(defn run [{params :params}]
  {:view :about
   :layout :minimallayout
   :params params})