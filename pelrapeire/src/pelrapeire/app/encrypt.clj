(ns pelrapeire.app.encrypt
  (:import java.security.MessageDigest))

(defn shaHash [#^String secret]
  (let [md (MessageDigest/getInstance "SHA1")
	bytes (. secret getBytes)]
    (do 
      (. md reset)
      (apply str 
	     (map #(Integer/toString (bit-and 0xff %) 16) (. md digest bytes))))))

    