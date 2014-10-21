(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


(def app-state
  (reagent/atom {
    :name "world"
    :dots nil
  }))


(defn random-data [n]
  (for [i (range n)]
    {:id i
     :x (rand)
     :y (rand)
     :r (rand)}))


(defn update-data! []
  (swap! app-state
         assoc :dots  (random-data 20)))



(defn greeting-component [name]
  [:div {:className "greeting"} (str "Hello, " name "!")])



(defn chart-component [width height data]
  [:svg {:className "chart" :width width :height height}
   (for [d data]
     [:circle {
        :className "dot"
        :cx        (* width (:x d))
        :cy        (* height (:y d))
        :r         (* 30 (:r d))} ]) ])



(defn main-view []
  [:div {
    :className "main-view"
    :on-click update-data!
   }
   [greeting-component (:name @app-state)]
   [chart-component 500 500 (:dots @app-state)]])


(defn ^:export run []
  (update-data!)
  (reagent/render-component [main-view] (.-body js/document)))
