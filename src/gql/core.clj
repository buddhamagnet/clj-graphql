(ns gql.core
  (:gen-class)
  (:require 
   [io.pedestal.http :as http]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
   [com.walmartlabs.lacinia.schema :as schema]
   [com.walmartlabs.lacinia.pedestal :as lacinia]
   [com.walmartlabs.lacinia :refer [execute]]
   [clojure.data.json :as json]))

(defn get-content [context arguments value]
  (let [{:keys [ref]} arguments]
    (println ref)
    (if (= ref "decimate")
      {:tegID 1000
       :tegType "Luke"}
      {:tegID 2000
       :tegType "Lando Calrissian"})))

(def content-schema
  (schema/compile
   (attach-resolvers {:get-content get-content}
     (edn/read-string
       (slurp (io/resource "schema.edn"))))))

(def service (lacinia/service-map content-schema {:graphiql true}))

(defonce runnable-service (http/create-server service))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (http/start runnable-service))