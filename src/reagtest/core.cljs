(ns reagtest.core
    (:require
     [reagent.core :as r]
     [re-frame.core :as rf]
     [day8.re-frame.http-fx]
     [kehys.db :as db]
     [kehys.subs :as subs]
     [kehys.events :as events]
     [ajax.core :as ajax :refer [GET POST PUT]]))

(defn dangerous
  ([comp content]
   (dangerous comp nil content))
  ([comp props content]
   [comp (assoc props :dangerouslySetInnerHTML {:__html content})]))

(defn diivi [id sisus]
  (dangerous :div {:id id
                   :key id
                   :flex 1
                   :style {:padding 10
                           :margin 5
                           :border "dotted 1"
                           :border-color "lightblue"
                           :background-color "lightgrey"}
                   :on-click  #(rf/dispatch [:valitse_juttu id])}  (str sisus)))
(defn editoi [id sisus] [:div [:textarea {:id id
                                          :on-change #(rf/dispatch [:value-change  (-> % .-target .-value)])

                                          :on-focus-out #(js/console.log "focus out" %)
                                          :cols 57
                                          :rows 7
                                          :wrap "hard"
                                          :defaultValue sisus}]])


(defn kanava_valikko [id name]
  [:div
   [:button {:type  "button"
             :id id
             :on-click #(rf/dispatch [:valinta (-> % .-target .-id)])} name]])

(defn vara_home-page []
    [:h1 "toimii"]
    )
(defn vara_home-page []
  
   (let [kanavat  (rf/subscribe [::subs/categories])
         muuvi (rf/subscribe [::subs/muuvi]) 
         valittu_id (rf/subscribe [::subs/valittu_juttu_id])
         uusi_id (rf/subscribe [::subs/uusi_id])
         valittu_juttu (rf/subscribe [::subs/valittu_juttu])]
     [:div {:style {:display "flex"}}
        (def ddd (js/Date.))
        [:h1 (str "AIKA->" (.getHours ddd))]
      (when (< 0 @uusi_id)
        (rf/dispatch [:valitse_juttu @uusi_id]))
      [:div {:hidden false :style {:width 80}}
       (map #(kanava_valikko (:id %) (:otsikko %)) @kanavat)
       [:br]
       [:button  {:on-click #(rf/dispatch [:uusi-juttu])} "UUSI"]]


      [:div  {:style {:flex 1 :border "solid 0px"}}

       [:div
        (if (not= 0 @valittu_id)
          (editoi @valittu_id @valittu_juttu)
          (do  (js/console.log "maara-> " (count @muuvi))
               (map #(when (not= 0 (:id %))
                       (diivi (:id %) (:teksti %))) @muuvi)))]  
       (when (not= 0 @valittu_id)
         (diivi 0 (clojure.string/replace @valittu_juttu "\n" "<br>")))]])
  
  )




;; -------------------------
;; Initialize app


(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [::events/initialize-db])
  ;(rf/dispatch [:get-api-data :categories "https://juu.azurewebsites.net/lue_kanavat.php"])
  (rf/dispatch [:valinta 1])

  (mount-root))
