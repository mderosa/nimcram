(ns migrate)

(def *menu* {})
(def *action* {})

(defstruct menuitem :query :yes :no])

(defn final-action [label query]
  (println (str query "  -bye")))

(defn mk-menu-item 
  ([label query]
     (def *menu* (assoc *menu* label final-action)))
  ([label query yes no]))

(mk-menu-item :what? "do you want to generate a do/undo script pair (y/n)?" :script-name? :forward-exec-migration?)
(mk-menu-item :script-name? "what is the name of your script?")
(mk-menu-item :forward-exec-migration? "do you want to forward execute a migration (y/n)?" :forward-to-version? :reverse-exec-migration?)
(mk-menu-item :forward-to-version? "what version would you like to roll forward to?")
(mk-menu-item :reverse-exec-migration "do you want to reverse execute a migration (y/n)?" :back-to-version? :goodbye)
(mk-menu-item :back-to-version "what version would you like to roll back to?")
(mk-menu-item :goodbye "goodbye") 

(:what? *menu*)
