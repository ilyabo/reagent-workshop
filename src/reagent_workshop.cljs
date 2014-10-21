(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


; Constants

(def FIELD-SIZE 25)
(def NUM-CELLS (* FIELD-SIZE FIELD-SIZE))
(def CELL-SIZE 20)
(def KEYS {37 :left 38 :up 39 :right 40 :down})


; Helpers

(defn pos-x [ci]
  (* (rem ci FIELD-SIZE) CELL-SIZE))

(defn pos-y [ci]
  (* (quot ci FIELD-SIZE) CELL-SIZE))



; App state

(def app-state
  (reagent/atom {
      :bug (quot NUM-CELLS 2)
      :fruit nil }))


(defn spawn-new-fruit! []
  (swap! app-state
         assoc :fruit (rand-int NUM-CELLS)))


(defn move-pos [direction pos]
  (+ pos (direction {:up   (- FIELD-SIZE)
                    :down  FIELD-SIZE
                    :left  -1
                    :right +1})))

(defn move-bug! [direction]
  (swap! app-state
         assoc :bug (move-pos direction (:bug @app-state))))


; Views

(defn fruit [ci]
  (let [m  (/ CELL-SIZE 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "fruit"
       :width     CELL-SIZE
       :height    CELL-SIZE
       :cx        cx
       :cy        cy
       :r         (dec m)}]))


(defn bug [ci]
  (let [m  (/ CELL-SIZE 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "bug"
       :width     CELL-SIZE
       :height    CELL-SIZE
       :cx        cx
       :cy        cy
       :r         (- m 3)
       ;:transform "scale(0.7,1)"
      }]))


(defn cell [ci & {:keys [has-bug has-fruit]}]
  [:g { :className "cell" }

   [:rect {
      :className "cell-rect"
      :width     CELL-SIZE
      :height    CELL-SIZE
      :x         (pos-x ci)
      :y         (pos-y ci)} ]

    (if has-bug (bug ci))

    (if has-fruit (fruit ci)) ])


(defn field [bug fruit]
  [:svg {:className "field"
         :width (* FIELD-SIZE CELL-SIZE)
         :height (* FIELD-SIZE CELL-SIZE)}

   (for [ci (range (* FIELD-SIZE FIELD-SIZE))]
      ^{:key ci}
       (cell ci
             :has-fruit (= ci fruit)
             :has-bug (= ci bug) ))])


(defn main-view []
  [:div {:className "app" }
   [field
    (:bug @app-state)
    (:fruit @app-state)]])


(defn on-key-down [evt]
  (if-let [key (KEYS (.-keyCode evt))] (move-bug! key)))


(defn ^:export run []
  (reagent/render-component [main-view] (.-body js/document))
  (spawn-new-fruit!)
  (js/addEventListener "keydown" on-key-down))