(ns reagtest.core
    (:require
      [reagent.core :as r]
      [ajax.core :refer [GET]]))

(defonce my-atom (r/atom 11))
(defonce valuee (r/atom ""))
(defonce databb (r/atom nil))
(defonce cate (r/atom ""))

(defn fetch-link! [data]
  (GET "https://juu.azurewebsites.net/read_categories.php"
    {:handler #(reset! data %) } )
    )

(defn fetch-link-bb! []
  (GET (str "https://juu.azurewebsites.net/filter_movies_by_category.php?title=" @valuee "&flt=" @cate)
    {:handler #(reset! databb %) } )
    )



    (defn isChecked[c_id]
      (-> js/document
        (.getElementById c_id)
        (.-checked)
            ;(set! false)
        )
      )

(defn atom-input [value]
  [:input {:type "text"
           :value @valuee
           :on-change #(do (reset! valuee (-> % .-target .-value)) (fetch-link-bb!) )}])

(defn shared-state []
  (let [val (r/atom "foo") data (r/atom nil) ]
    (fetch-link! data)
    (fetch-link-bb!)
    (fn []
      (let [{:strs [category_id ]} (first @data) {:strs [title]} (first @databb)]
      [:article [:div {:id "movieDiv"}
      [:div [:p "Change it here: " [atom-input val]]]


       (for [item @data]
          (let [name (get item "name" ) c_id (get item "category_id" )]

          [:aside
            [:div  {:id "categoriaDiv"}
              [:input {:type "checkbox"
                       :id c_id
                       :name "in"
                       :key c_id
                       :style {:background-color "green"}
                       :on-click
                         (fn[]
                            (reset! cate
                                (reduce
                                  #(let [c_id (get %2 "category_id")] (
                                    str %
                                    (if (isChecked c_id)
                                            (str (if (< 0 (count %)) ",") c_id )) ))
                                  ""
                                  @data )) (fetch-link-bb!))
              }]
              [:b name]]]))]
              [:table
              (for [item @databb]
                 (let [tit (get item "title" ) des (get item "description" )]
                  [:tr
                    [:td tit]
                    [:td des]]

                 ))
]
       [:p "category_id"]
       [:p "The value is now: " @val]
       ]))))

(defn home-page []


  [:div
        [:h3 "Tervetuloa"]
        [:p @valuee]
        [shared-state]
        [:button  {:on-click (fn[] (swap! valuee inc))} @valuee]
        [:p]])



;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
