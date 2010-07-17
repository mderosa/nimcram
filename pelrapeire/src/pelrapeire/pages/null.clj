(ns pelrapeire.pages.null)

(defn show [data]
  (let [content [:div (:data data)]]
    {:js nil :css nil :title "test" :content content}))