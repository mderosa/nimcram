(ns pelrapeire.controllers.users.new-ctrl)

(def req
     {
      :remote-addr "0:0:0:0:0:0:0:1", 
      :scheme :http, 
      :query-params {}, 
      :form-params {}, 
      :request-method :get, 
      :query-string nil, 
      :route-params {}, 
      :content-type nil, 
      :cookies {}, 
      :uri "/index", 
      :server-name "localhost", 
      :params {}, 
      :headers {
		"connection" "keep-alive, keep-alive 115", 
		"accept-charset" "ISO-8859-1,utf-8;q=0.7,*;q=0.7", 
		"accept-encoding" "gzip,deflate", 
		"accept-language" "en-us,en;q=0.5", 
		"accept" "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", 
		"user-agent" "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10", 
		"host" "localhost:8080"
		}, 
      :content-length nil, 
      :server-port 8080, 
      :character-encoding nil
      })

(deftest test-run-despatch-on-accept-header
  (testing "depending on the value of the accept header we should get back
either a json-view or a html-view"
    ))