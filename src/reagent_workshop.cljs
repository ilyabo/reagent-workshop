(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


(defn main-view []
  [:div "Hello world!"])



(defn ^:export run []
  (reagent/render-component [main-view]
                              (.-body js/document)))