(ns reagtest.core
    (:require
      [reagent.core :as r]
      [ajax.core :refer [GET]]))



(defonce my-atom (r/atom 11))

(defonce valuee (r/atom 212))

;; -------------------------
;; Views



(defn fetch-link! [data] 
  (GET "https://juu.azurewebsites.net/read_categories.php" 
    {:handler #(reset! data %) } )
    )

(defn fetch-link-bb! [data] 
  (GET "https://breaking-bad-quotes.herokuapp.com/v1/quotes" 
    {:handler #(reset! data %) } )
    )





(defn atom-input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

(defn shared-state []
  (let [val (r/atom "foo") data (r/atom nil) databb (r/atom nil)]
    (fetch-link! data)
    (fetch-link-bb! databb)
    (fn []
      (let [{:strs [category_id ]} (first @data) {:strs [author quote]} (first @databb)]
      [:div
       [:a {:href "https://gitlab.siilicloud.com/janne.kujanpaa/clojure-unit-testing" :target "_blank"} "clojure-unit-testing"]
       [:br]
       [:a {:href "https://www.rockyourcode.com/tutorial-clojurescript-app-with-reagent-for-beginners-part-1/" :target "_blank"} "tutorial-clojurescript-app-with-reagent-for-beginners-part-1"]
       [:br][:a {:href "https://www.reaktor.com/blog/clojurescript-pekonilla-vai-ilman/" :target "_blank"} "clojurescript pekonilla vai ilman"]
       [:br][:a {:href "https://www.yumpu.com/en/document/read/55096098/web-development-with-clojure-2nd-edition" :target "_blank"} "web-development-with-clojure-2nd-edition"]
       [:br][:a {:href "" :target "_blank"} ""]
       [:br][:a {:href "https://reagent-project.github.io/" :target "_blank"} "Reagent: Minimalistic React for ClojureScript"]
       [:br][:a {:href "" :target "_blank"} ""]
       [:br][:a {:href " " :target " _blank "} ""]
       
       (js/console.log (str "Tyyppi::" (type @databb)))
       [:p author]




       ;(js/console.log (map   #([:p(second(second %))]) @data))
       ;(js/console.log (map   #([:p(second(second %))]) @data))
       
       
      (for [item @data]
          (let [name (get item "name" )]
           [:div
           [:input {:type "checkbox" :checked "1":key name :style {:background-color "green"}}]
           [:b name]]))
       
       
       [:p "category_id"]
       [:p "The value is now: " @val]
       [:p "Change it here: " [atom-input val]]]))))

(defn home-page []
 

  [:div
        [:h3 "16:23 Tervetuloa"]
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
