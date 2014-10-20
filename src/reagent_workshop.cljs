(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


(let [size 11]

(def app-state
  (reagent/atom {
      :size size
      :fruit [(rand-int size) (rand-int size)]
      :bug [(quot size 2) (quot size 2)]})))





;(defn spawn-new-fruit! []
;  (swap! app-state :fruit (rand-pos)))





(defn bug-view []
  [:div {:className "field__cell bug"}])



(defn fruit-view []
  [:div {:className "field__cell fruit"}])

(defn cell-view [i j]
  [:div {:className "field__cell"}])


(defn field-view [size bug-pos fruit-pos]
  [:div {:className "field"}
   (for [i (range size)]
     ^{:key (:name i)}
     [:div {:className "field__row"}
      (for [j (range size)]
        ^{:key (:name j)}
        (cond
          (= bug-pos [i j]) [bug-view]
          (= fruit-pos [i j]) [fruit-view]
         :else [cell-view i j])
        )])])

(defn main-view []
  [:div {:className "app"}
   [field-view
    (:size @app-state)
    (:bug @app-state)
    (:fruit @app-state)]])


(defn ^:export run []
  (reagent/render-component [main-view]
                              (.-body js/document)))