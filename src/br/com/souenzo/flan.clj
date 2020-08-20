(ns br.com.souenzo.flan
  (:require [io.pedestal.http :as http]
            [ring.util.mime-type :as mime]
            [hiccup2.core :as h]
            [io.pedestal.http.route :as route]
            [clojure.edn :as edn]
            [io.pedestal.interceptor :as interceptor]
            [io.pedestal.http.body-params :as body-params]
            [clojure.java.io :as io]))

(defn routes
  [{::keys [ns-name]}]
  (require (symbol ns-name)
           :reload)
  (let [{::keys [path]
         :or    {path (str ns-name)}} (requiring-resolve (symbol ns-name "-index"))]
    (-> #{[(str "/" ns-name) :any [(body-params/body-params)
                                   {:name  ::merge-params
                                    :enter (fn [ctx]
                                             (let [params (-> ctx
                                                              :request
                                                              ((juxt :json-params :form-params :transit-params)))]
                                               (assoc-in ctx [:request :params] (apply merge params))))}

                                   {:name  ::->html
                                    :leave (fn [{:keys [response]
                                                 :as   ctx}]
                                             (if (and (coll? response)
                                                      (= :html (first response)))
                                               (assoc ctx
                                                 :response {:body    (->> response
                                                                          (h/html
                                                                            {:mode :html}
                                                                            (h/raw "<!DOCTYPE html>\n"))
                                                                          str)
                                                            :headers {"Content-Type" (mime/default-mime-types "html")}
                                                            :status  200})
                                               ctx))}
                                   {:name  (keyword ns-name "-handler")
                                    :enter (fn [{{:keys [request-method]} :request
                                                 :keys                    [request]
                                                 :as                      ctx}]
                                             (when-let [-handler (requiring-resolve (symbol ns-name (str "-" (name request-method))))]
                                               (assoc ctx :response (-handler request))))}]]
          :route-name (keyword ns-name)}
        route/expand-routes)))


(defn not-found
  [{::keys [ns-name]}]
  (->> (fn [{:keys [response request]
             :as   ctx}]
         (if (http/response? response)
           ctx
           (let [base-file-name (subs (#'clojure.core/root-resource (symbol ns-name))
                                      1)
                 absolute-name (first (for [suffix ["clj" "cljc"]
                                            :let [r (io/resource (str base-file-name "." suffix))]
                                            :when r]
                                        (.getAbsolutePath (io/file r))))
                 body (->> [:html
                            [:head
                             [:title "Not Found"]]
                            [:body
                             [:header]
                             [:main
                              [:p "Can't find " [:code (-> request :uri pr-str)]]
                              (cond
                                (requiring-resolve (symbol ns-name "-get"))
                                [:p "You can try " [:a {:href "/com.example.todo-list"}
                                                    "/com.example.todo-list"]]
                                absolute-name
                                (list
                                  [:p "There is no function " [:code "-get"] " defined in your main namespace " [:code ns-name]]
                                  [:p "The code for " [:code ns-name] "in on" [:code absolute-name]]
                                  [:p "You can copy and paste this and the end of the file and try again"]
                                  [:pre "(defn -get\n  [req]\n  [:html\n   [:head]\n   [:body\n    \"Hello World!\"]])"])
                                :else [:p "You need to create a file in " [:code (str "src/" ns-name)]])]
                             [:footer]]]
                           (h/html
                             {:mode :html}
                             (h/raw "<!DOCTYPE html>\n"))
                           str)
                 response {:body    body
                           :headers {"Content-Type" (mime/default-mime-types "html")}
                           :status  404}]
             (assoc ctx :response response))))
       (hash-map :name ::not-found :leave)
       interceptor/interceptor))

(defn start
  [port->server opts]
  (prn port->server opts)
  (let [port 8080
        env {::ns-name "com.example.todo-list"}]
    (some-> port->server
            (get port)
            http/stop)
    (assoc port->server
      port (-> {::http/routes                (fn []
                                               (routes env))
                ::http/join?                 false
                ::http/mime-types            mime/default-mime-types
                ::http/port                  port
                ::http/not-found-interceptor (not-found env)
                ::http/type                  :jetty}
               http/default-interceptors
               http/dev-interceptors
               http/create-server
               http/start))))

(defonce port->server (atom {}))

(defn -main
  [& opts]
  (swap! port->server start opts))
