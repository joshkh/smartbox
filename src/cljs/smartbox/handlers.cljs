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


(defn update-match [s searchterm value]
  (println "update-match: s" s)
  (println "update-match: searchterm" searchterm)
  (println "update-match: value" value)
  (vec (map (fn [item] (if (= searchterm (:identifier item))
                         (assoc item :status "matched")
                         item))
            s)) )

(re-frame/register-handler
  :update-identifier
  (fn [db [_ matches]]
    (let [test nil]
      (println (get-in (first matches) [:input]))
      (last (map (fn [searchterm]
                    (println "RUNNING")
                    (update-in db [:input :identifiers] update-match searchterm "matched"))
                  (get-in (first matches) [:input]))))))