(ns smartbox.css
  (:require [garden.def :refer [defstyles]]
            [garden.selectors :refer [attr focus]]))

(defstyles screen
  [:body {:color "#333"
          :margin "0 auto"
          :padding-top "22px"
          :-webkit-font-smoothing "antialiased"
          :font-family "'Helvetica Neue', Verdana, Helvetica, Arial, sans-serif"
          :font-size "1.125em"
          :line-height "1.5em"
          :background "#F9F9F9"}]
           [:* (focus) {:outline 0}]


           [:div.smartbox {:width "100%"
                           :min-height "200px"
                           :background "#f8f5ec"
                           :border "1px solid #E0DED5"}

            [:input.freeform {:font-size "1.125em"
                              :float "left"
                              :border "none"
                              :padding "6px 0 6px 0"
                              :margin "2px"
                              :background "transparent"
                              :color "#2d2d2d"}]

            [:div.identifier {:float "left"
                              :padding "4px"
                              :margin "2px"
                              :border "1px solid #EAEAEA"
                              :border-radius "9px"
                              :transition "all 0.5s ease"}
             [:&.pending {:background "#EAEAEA"}]
             [:&.matched {:background "#9FCC68"
                          :border "#9FCC68"
                          :color "white"}]]]
)
