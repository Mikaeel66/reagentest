(ns kehys.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::valittu_juttu
 (fn [db]
   (:valittu_juttu db)))
(rf/reg-sub
 ::uusi_id
 (fn [db]
   (:id (:uusi_id db))))
(rf/reg-sub
    ::val
    (fn [db]
      (:val db)))
(rf/reg-sub
 ::valittu_juttu_id
 (fn [db]
   (js/console.log "valittu_juttu_id----------------->>>" (:valittu_juttu_id db))
   (:valittu_juttu_id db)))

(rf/reg-sub
 ::val2
 (fn [db]
   (:val2 db)))

(rf/reg-sub
          ::cate
          (fn [db]
            (:cate db)))

(rf/reg-sub
    ::categories
    (fn [db]
      (:categories db)))

(rf/reg-sub
    ::muuvi
    (fn [db]
      (:muuvi db)))

