(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


; Constants

(def SIZE 25)
(def WIDTH 20)
(def KEYS {37 :left 38 :up 39 :right 40 :down})


; Helpers

(defn pos-x [ci]
  (* (rem ci SIZE) WIDTH))

(defn pos-y [ci]
  (* (quot ci SIZE) WIDTH))



; App state

(def app-state
  (reagent/atom {
      :bug (quot (* SIZE SIZE) 2)
      :fruit nil }))


(defn spawn-new-fruit! []
  (swap! app-state
         assoc :fruit (rand-int (* SIZE SIZE))))

(defn can-move [pos direction]
  (case direction
    :up    (> pos SIZE)
    :down  (< (quot pos SIZE) (dec SIZE))
    :left  (> (rem pos SIZE) 0)
    :right (< (rem pos SIZE) (dec SIZE))))

(defn move [pos direction]
  (if (can-move pos direction)
    (+ pos (direction {:up    (- SIZE)
                      :down  SIZE
                      :left  -1
                      :right +1}))
    pos))

(defn move-bug! [direction]
  (swap! app-state
         assoc :bug (-> (:bug @app-state) (move direction)))

  (if (= (:bug @app-state) (:fruit @app-state))
    (spawn-new-fruit!)))


; Views

(defn fruit [ci]
  (let [m  (/ WIDTH 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "fruit"
       :width     WIDTH
       :height    WIDTH
       :cx        cx
       :cy        cy
       :r         (dec m)}]))


(defn bug [ci]
  (let [m  (/ WIDTH 2)
        cx (+ (pos-x ci) m)
        cy (+ (pos-y ci) m)]
    [:circle
      {:className "bug"
       :width     WIDTH
       :height    WIDTH
       :cx        cx
       :cy        cy
       :r         (- m 3)
       ;:transform "scale(0.7,1)"
      }]))


(defn cell [ci & {:keys [has-bug has-fruit]}]
  [:g { :className "cell" }

   [:rect {
      :className "cell-rect"
      :width     WIDTH
      :height    WIDTH
      :x         (pos-x ci)
      :y         (pos-y ci)} ]

   (if has-fruit (fruit ci))

   (if has-bug (bug ci))])


(defn field [bug fruit]
  [:svg {:className "field"
         :width (* SIZE WIDTH)
         :height (* SIZE WIDTH)}

   (for [ci (range (* SIZE SIZE))]
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