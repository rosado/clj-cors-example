(ns rs.cors
  (:require [ring.adapter.jetty :as jetty]
            (ring.middleware [params :refer [wrap-params]]
                             [keyword-params :refer [wrap-keyword-params]]
                             [nested-params :refer [wrap-nested-params]]
                             [session :refer [wrap-session]]
                             [resource :refer [wrap-resource]]
                             [cors :refer [wrap-cors]])
            [compojure.core :refer [defroutes GET POST OPTIONS ANY]]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            ;; [rs.ajax-login :as ajax-login]
            ))

;;; SERVER

(def users {"roland" {:username "roland"
                      :password (creds/hash-bcrypt "pwd")
                      :roles #{::user}}})

(defroutes server-routes
  (GET "/" [] {:status 200 :body "Yes? This is server"})
  (GET "/unprotected" [] {:status 200 :body "Unprotected resource"})
  (GET "/protected" request
       (friend/authenticated
        "You have reached the protected part.")))

(def server-handler
  (-> server-routes
      (friend/authenticate {:workflows [(workflows/http-basic
                                         :realm "CORS test"
                                         :credential-fn #(creds/bcrypt-credential-fn users %))]})
      wrap-params
      wrap-keyword-params
      wrap-nested-params
      wrap-session
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods [:get :post :options]
                 :access-control-allow-headers "Content-Type, Authorization"
                 :access-control-allow-credentials "true")))

;;; CLIENT

(defn base-client-handler
  [request]
  {:status 200
   :body "This is the client!"})

(def client-handler (wrap-resource base-client-handler "static"))
