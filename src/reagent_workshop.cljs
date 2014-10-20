(ns reagent-workshop
 (:require [clojure.string :as str]
           [reagent.core :as reagent]))

(enable-console-print!)


(def app-state
  (reagent/atom {
     :message "Hello, world!"
     }))


(defn main-view []
  [:div {:className "greeting"}
   (:message @app-state)])


(defn ^:export run []
  (reagent/render-component [main-view]
                              (.-body js/document)))