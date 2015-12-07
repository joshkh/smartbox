(ns smartbox.views
    (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
              [reagent.core  :as reagent :refer [atom]]))


;; home

(enable-console-print!)

(def flymine (js/imjs.Service. (clj->js {:root "www.flymine.org/query"})))


(defn resolve-id [id]
  (let [id-job-promise (.resolveIds flymine (clj->js {:identifiers [id]
                                                      :type "Gene"
                                                      :extra "D. melanogaster"}))]
    (-> id-job-promise
        (.then (fn [id-job]
                 (.poll id-job
                        (fn [success]
                          (let [[matches duplicate other]
                                (map #(js->clj (aget (aget success "matches") %) :keywordize-keys true)
                                     ["MATCH" "DUPLICATE" "OTHER"])
                                unresolved (.. success -unresolved)]
                            (if-not (empty? matches)
                              (dispatch [:update-identifier matches]))))))

               (fn [r] (println "FAILED FIRST"))))))

(defn app-state []
  (let [state (re-frame/subscribe [:app-state])]
    [:div (str @state)]))

(defn strip-characters
  "Removes one or more characters from a string"
  [haystack needles]
  (apply str (remove #((set needles) %) haystack)))

(defn smartbox-input
  "Component that controls the input for the list of items."
  []
  (let [val (atom nil)
        reset #(set! (-> % .-target .-value) nil)
        save #(let [v (strip-characters (-> @val str clojure.string/trim) ",; ")]
               (if-not (empty? v)
                 (do (dispatch [:add-identifier v])
                     (resolve-id v))))]
    (fn []
      [:input.freeform {:type "text"
               :value @val
               :size (count @val)
               :on-change #(reset! val (-> % .-target .-value))
              :placeholder "Identifiers..."
               :on-key-down #(case (.-which %)
                              13 (do (save) (reset %))
                              188 (do (save) (reset %))
                              9 (do (save) (reset %))
                              32 (do (save) (reset %))
                              nil)}])))

(defn identifier [{identifier :identifier status :status}]
  [:div.identifier {:class status} identifier])

(defn smartbox-container []
  "Component: Houses all the identifiers and inputs"
  (let [input-identifiers (re-frame/subscribe [:input-identifiers])
        focus-textbox #(-> % .-target (.querySelector "input[type=text]") .focus)]
    (fn [] [:div
            [:h1 (str (count @input-identifiers) " identifiers")]
            [:div.smartbox
             {:on-click focus-textbox}
             (map identifier @input-identifiers)
             [smartbox-input]]
            [app-state]])))


(defn home-panel []
  (let [input-identifiers (re-frame/subscribe [:input-identifiers])]
    (fn [] (smartbox-container))))


;; about

(defn about-panel []
  (fn []
    [:div "This is the About Page."
     [:div [:a {:href "#/"} "go to Home Page"]]]))


;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      (panels @active-panel))))
