(ns slitrace.prim
  (:use slitrace.core)
  (:use sligeom.core)
  (:use sligeom.bounding)
  (:use sligeom.transform)
  (:use sligeom.intersect)
  (:import [sligeom.intersect Ray])
  (:import [sligeom.bounding BBox])
  (:import [sligeom.transform Transform]))

(defrecord Instance [^Transform transform primitive ]
  Traceable
  (trace [this _r]
    (let [^Ray r _r]
      (if-let [[t p n] (trace primitive (transform r (inverse transform)))]
        [t (transform-point p transform) (transform-normal n transform)])))

  Bounded
  (bounding-box [this]
    (transform (bounding-box primitive) transform)))

(defn instance [^Transform T primitive]
  (Instance. T primitive))


(deftype ListGroup [^Transform transform ^BBox bounds primitives]
  Traceable
  (trace [this _r]
    (let [^Ray r _r
          ^Ray r-local (transform r (inverse transform))
          [tn pn nn] (reduce (fn [[t0 _ _ :as s1] prim]
                               (if-let [s2 (trace prim (ray-interval r-local t0))] s2 s1))
                             [(.maxt r-local) nil nil]
                             primitives)]
      (if pn
        [tn (transform-point pn transform) (transform-normal nn transform)]))))

;; TODO - bounding box
(defn list-group [^Transform transform primitives]
  (ListGroup. transform nil primitives))

;; TODO - choose a grouping strategy based on primitives
(defn group [^Transform transform primitives]
  (list-group transform primitives))

