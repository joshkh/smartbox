(ns smartbox.handlers
    (:require [re-frame.core :as re-frame]
              [smartbox.db :as db]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/register-handler
  :add-identifier
  (fn [db [_ identifier]]
    (println "id is" identifier)
    (let [current-identifiers (get-in db [:input :identifiers])]
      (println "current idens" current-identifiers)
      (update-in db [:input :identifiers] conj {:identifier identifier :status "pending"}))))

(re-frame/register-handler
  :talk
  (fn [db [_ [value]]]
    (map count (get-in value [:input]))
    db))