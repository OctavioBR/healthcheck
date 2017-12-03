(ns server.core
  (:require [aleph.http :as http]
            [somnium.congomongo :as m]
            [cheshire.core :refer :all]
            [bidi.ring :refer (make-handler)]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [redirect response content-type]]
            [clojure.core.async :refer [go >! <! timeout chan close! <!! >!!]]
            [clojure.string :as s]
            [clojure.java.io :as io])
  (:gen-class))

(def conn
  (m/make-connection "hh"
                     :host (System/getenv "MONGO_SERVICE_HOST")
                     :port (read-string (System/getenv "MONGO_SERVICE_PORT"))))
(m/set-connection! conn)


(defonce web-server (atom nil))

(defn start-web-server! [handler]
  (do
    (reset! web-server (http/start-server handler {:port 4000 :join? false}))
    (println "up and running on 4000")))

(defn paciente [req]
  (let [qry-str (select-keys (:query-params req) ["cns"])]
    (content-type (response (generate-string
                              ((comp vec set)
                               (map #(dissoc % :_id) (m/fetch :lista_espera :where {:cns (get qry-str "cns") :servico "Exame"})))))
                  "application/json")))

(defn escopo [req]
  (let [qry-str (select-keys (:query-params req) ["cnes"
                                                  "procedimento"])]
    (content-type (response (generate-string
                                ((comp vec set)
                                 (map :cns (m/fetch :lista_espera :where {:codigo_procedimento (get qry-str "procedimento")
                                                                        :central_regulacao {:$regex (str ".*" (get qry-str "cnes") ".*")}
                                                                        :servico "Consulta"
                                                                        :tipo_lista "Regulada"
                                                                                    }))))) "application/json")))

(defn autocnes [req]
  (let [qry-str (select-keys (:query-params req) ["cnes"])]
    (content-type (response (generate-string
                              ((comp vec set)
                                (map
                                (comp #(select-keys % [:central_regulacao]) #(dissoc % :_id))
                                (m/fetch :lista_espera :where {:central_regulacao {:$regex (str ".*" (get qry-str "cnes") ".*")}}) ))))
                  "application/json")))

(defn autoproc [req]
  (let [qry-str (select-keys (:query-params req) ["procedimento"])]
    (content-type (response (generate-string
                              ((comp vec set)
                                (map
                                (comp #(select-keys % [:procedimento :codigo_procedimento]) #(dissoc % :_id))
                                (m/fetch :lista_espera :where {:procedimento {:$regex (str (get qry-str "procedimento") ".*")}})))))
                  "application/json")))

(def router (make-handler
              ["/" {"paciente" {:get paciente}
                    "escopo" {:get escopo}
                    "autocnes" {:get autocnes}
                    "autoproc" {:get autoproc}}]))



(defn dev-main []
  (when-not @web-server
    (start-web-server!  (wrap-params router))))

(defn -main [& args]
  (start-web-server! (wrap-params router)))
