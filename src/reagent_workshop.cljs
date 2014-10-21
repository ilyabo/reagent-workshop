(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


; Constants

(def field-size 25)
(def num-cells (* field-size field-size))
(def cell-size 20)


; Helpers

(defn pos-x [ci]
  (* (rem ci field-size) cell-size))

(defn pos-y [ci]
  (* (quot ci field-size) cell-size))



; App state

(def app-state
  (reagent/atom {
      :bug (quot num-cells 2)
      :fruit nil }))


(defn spawn-new-fruit! []
  (swap! app-state
         assoc :fruit (rand-int num-cells))

  (println (clj->js @app-state)))





; Views

(defn fruit [ci]
  (let [m  (/ cell-size 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "fruit"
       :width     cell-size
       :height    cell-size
       :cx        cx
       :cy        cy
       :r         (dec m)}]))


(defn bug [ci]
  (let [m  (/ cell-size 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "bug"
       :width     cell-size
       :height    cell-size
       :cx        cx
       :cy        cy
       :r         (- m 3)
       ;:transform "scale(0.7,1)"
      }]))


(defn cell [ci & {:keys [has-bug has-fruit]}]
  [:g { :className "cell" }

   [:rect {
      :className "cell-rect"
      :width     cell-size
      :height    cell-size
      :x         (pos-x ci)
      :y         (pos-y ci)} ]

    (if has-bug (bug ci))

    (if has-fruit (fruit ci)) ])


(defn field [bug fruit]
  [:svg {:className "field"
         :width (* field-size cell-size)
         :height (* field-size cell-size)}

   (for [ci (range (* field-size field-size))]
      ^{:key ci}
       (cell ci
             :has-bug (= ci bug)
             :has-fruit (= ci fruit)))])


(defn main-view []
  [:div {:className "app"}
   [field
    (:bug @app-state)
    (:fruit @app-state)]])


(defn ^:export run []
  (spawn-new-fruit!)
  (reagent/render-component [main-view]
                              (.-body js/document)))