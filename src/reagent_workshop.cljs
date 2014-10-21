(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


(def app-state
  (reagent/atom { :name "world" }))


(defn greeting-component [name]
  [:div {:className "greeting"} (str "Hello, " name "!")])

(defn main-view []
  [:div {:className "main-view"}
   [greeting-component (:name @app-state)]])


(defn ^:export run []
  (reagent/render-component [main-view] (.-body js/document)))
