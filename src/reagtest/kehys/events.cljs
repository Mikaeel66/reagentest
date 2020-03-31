(ns kehys.events
  (:require
   [re-frame.core :as rf]
   [kehys.db :as db]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax :refer [GET POST PUT]]))

(rf/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(defn- isChecked[c_id]
     (-> js/document
       (.getElementById c_id)
       (.-checked)))
           ;(set! false)

(defn- lue_muuvit [a]
  (rf/dispatch [:get-api-data :muuvi
      (str "https://juu.azurewebsites.net/lue_jutut.php?kanava_id=" a)]))

(defn- tallenna_juttu [a b]
  (rf/dispatch [:get-api-data :lukuvirhe222
      (str "https://juu.azurewebsites.net/talleta.php?otsikko=ei&id=" a "&teksti=" (clojure.string/replace b "\n" "<br>"))]))

(defn- uusi_juttu [a]
  (js/console.log "uusi juttu osa 2.-> " a)
  (rf/dispatch [:get-api-data :uusi_id
                (str "https://juu.azurewebsites.net/talleta.php?kanava_id=" a)]))

(rf/reg-event-db
 :value-change2
 (fn [db [_ new-value]]
   (assoc db :val2 new-value)))

(rf/reg-event-db
 :uusi-juttu
 (fn [db [_]]
   (js/console.log "uusi juttu-> osa 1.-> " (get db :name))
   (uusi_juttu (get db :cate)) 
   db ))



(rf/reg-event-db
  :value-change
   (fn [db [_ new-value]]
    (tallenna_juttu (get db :valittu_juttu_id) new-value)
    (assoc db :valittu_juttu new-value)))

(rf/reg-event-db
    :valinta
    (fn [db [_ huu]]
      (lue_muuvit huu)
      (assoc db :cate huu :valittu_juttu_id 0 :valittu_juttu "")))

(defn- lue_valittu_juttu [db id]
  (reduce 
   #(if (= id (:id %2)) 
      (:teksti %2) 
      % ) "" (:muuvi db)))
(rf/reg-event-db
 :valitse_juttu
 (fn [db [_ id]]
   ;(lue_muuvit (get db :cate))
   
(js/console.log "valittu juttu ---> " (lue_valittu_juttu  db id))
   (assoc db :uusi_id "" :muuvi "" :valittu_juttu_id id :valittu_juttu (clojure.string/replace (lue_valittu_juttu db id) "<br>" "\n" ))))

(defn- make-api-call [resource on-success on-failure]
     {:http-xhrio {:method :get
                   :uri resource
                   :response-format (ajax/json-response-format {:keywords? true})
                   :on-success on-success
                   :on-failure on-failure}})
(rf/reg-event-fx
    :get-api-data
      (fn [{db :db} [_ key resource]]
         (make-api-call resource [:get-api-data-success key] [:get-api-data-failure key])))

(rf/reg-event-db
      :get-api-data-success
        (fn [db [_ key data]]
          (js/console.log db "get api" key (str data))
          (assoc db  key data)))

(rf/reg-event-db
      :get-api-data-failure
           (fn [db [_ k]]
             (prn (str "virhe-> Failed to fetch " k))
             db))

(rf/reg-event-fx
 ::update-user-data-success
 (fn [{:keys [db]} [_ resp]]
   (js/console.log "onnistu-->22.57" (str resp))))

(rf/reg-event-fx
 ::update-user-data-failure
 (fn [{:keys [db]} [_ resp]]
   (js/console.log "Ei lukenu!!!!")))



(rf/reg-event-fx
 ::update-user-data
 (fn [{:keys [db]} [_ user-data]]
   (let [token (-> db :name)]
   {:http-xhrio
    {:method          :post
     ;:headers         {:Authorization (str "Token " token)}
     :uri             "https://juu.azurewebsites.net/test.php"
     :params          user-data
     :format          (ajax/transit-request-format)
     :response-format (ajax/transit-response-format)
     :on-success      [::update-user-data-success]
     :on-failure      [::update-user-data-failure]}}
   )
 ))