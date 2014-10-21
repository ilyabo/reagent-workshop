(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


; Constants
(def WIDTH 500)
(def KEYS {37 :left 38 :up 39 :right 40 :down})


; Helpers

(defn pos-x [ci size w]
  (* (rem ci size) w))

(defn pos-y [ci size w]
  (* (quot ci size) w))



; App state

(let [size 9]

(def app-state
 (reagent/atom
   {
     :size  size
     :bug   nil
     :fruit nil
     :score 0 })))


(defn spawn-new-fruit! []
  (let [size (:size @app-state)]
    (swap! app-state
          assoc :fruit (rand-int (* size size)))))

(defn initial-positions! []
  (let [size (:size @app-state)]
    (swap! app-state
           assoc :bug (quot (* size size) 2))
    (spawn-new-fruit!)))


(defn can-move [pos direction size]
  (case direction
    :up    (>= pos size)
    :down  (< (quot pos size) (dec size))
    :left  (> (rem pos size) 0)
    :right (< (rem pos size) (dec size))))

(defn move [pos direction]
  (let [size (:size @app-state)]
    (if (can-move pos direction size)
     (+ pos (direction {:up    (- size)
                        :down  size
                        :left  -1
                        :right +1}))
     pos)))

(defn inc-score! []
  (swap! app-state
         update-in [:score] inc))

(defn update-bug-position! [direction]
  (swap! app-state
         assoc :bug (-> (:bug @app-state) (move direction)))

  (if (= (:bug @app-state) (:fruit @app-state))
    (do
      (spawn-new-fruit!)
      (inc-score!))))

(defn update-size! [size]
  (swap! app-state
         assoc :size size)
  (initial-positions!))


; Events

(defn on-key-down [evt]
  (if-let [key (KEYS (.-keyCode evt))] (update-bug-position! key)))

(defn on-resize [evt]
  (update-size! (-> evt .-target .-value js/parseInt)))



; Views

(defn fruit [ci size w]
  (let [m  (/ w 2)
        cx (+ (pos-x ci size w) m)
        cy (+ (pos-y ci size w) m)]
    [:circle
      {:className "fruit"
       :width     w
       :height    w
       :cx        cx
       :cy        cy
       :r         (dec m)}]))


(defn bug [ci size w]
  (let [m  (/ w 2)
        cx (+ (pos-x ci size w) m)
        cy (+ (pos-y ci size w) m)]
    [:circle
      {:className "bug"
       :width     w
       :height    w
       :cx        cx
       :cy        cy
       :r         (- m 3)
       ;:transform "scale(0.7,1)"
      }]))


(defn cell [ci size w & {:keys [has-bug has-fruit]}]
  [:g { :className "cell" }

   [:rect {
      :className "cell-rect"
      :width     w
      :height    w
      :x         (pos-x ci size w)
      :y         (pos-y ci size w)} ]

   (if has-fruit [fruit ci size w])

   (if has-bug [bug ci size w])])


(defn field [bug fruit size w]
  [:svg {:className "field"
         :width (* size w)
         :height (* size w)}

   (for [ci (range (* size size))]
      ^{:key ci}
       [cell ci size w
             :has-fruit (= ci fruit)
             :has-bug (= ci bug)] )])


(defn resize-view [size]
  [:div {:className "resize-view"}
   "Field size: "
   [:input {:type "range"
            :min 3
            :max 30
            :value size
            :on-change on-resize
   }]])

(defn score-view [score]
  [:div {:className "score-view"}
   (str "Score: " score)])

(defn main-view []
  [:div {:className "app" }
   [resize-view (:size @app-state)]
   [field
    (:bug @app-state)
    (:fruit @app-state)
    (:size @app-state)
    (/ WIDTH (:size @app-state))]
   [score-view (:score @app-state)]])


(defn ^:export run []
  (reagent/render-component [main-view] (.-body js/document))
  (initial-positions!)
  (js/addEventListener "keydown" on-key-down))