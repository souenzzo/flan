(ns com.example.todo-list)

(defonce todos (atom #{}))

(defn -get
  [req]
  [:html
   [:head]
   [:body
    [:form
     {:method "POST"}
     [:input {:name "text"}]
     [:input {:type "submit"}]]
    [:ul
     (for [todo @todos]
       [:li todo])]]])

(defn -post
  [{:keys [params]}]
  (swap! todos conj (:text params))
  {:headers {"Location" "/com.example.todo-list"}
   :status  301})
