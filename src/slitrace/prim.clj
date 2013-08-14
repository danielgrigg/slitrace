(ns slitrace.prim
  (:use [slitrace core]
        [sligeom core bounding transform intersect aggregate])
  (:import [sligeom.intersect Ray]
           [sligeom.bounding BBox]
           [sligeom.transform Transform]
           [sligeom.aggregate Grid]))

(defrecord Instance [^Transform transformation primitive ]
  Traceable
  (trace [this _r]
    (let [^Ray r _r]
      (if-let [[t p n] (trace primitive (transform r (inverse transformation)))]
        [t (transform-point transformation p) (transform-normal transformation n)])))

  Bounded
  (bounding-box [this]
    (transform (bounding-box primitive) transformation)))

(defn instance [^Transform T primitive]
  "Create an instanced primitive"
  (Instance. T primitive))

(deftype ListGroup [^Transform transformation ^BBox bounds primitives]
  Traceable
  (trace [this _r]
    (let [^Ray r _r
          ^Ray r-local (transform r (inverse transformation))
          [tn pn nn] (reduce (fn [[t0 _ _ :as s1] prim]
                               (if-let [s2 (trace prim (ray-interval r-local t0))] s2 s1))
                             [(.maxt r-local) nil nil]
                             primitives)]
      (if pn
        [tn (transform-point transformation pn) 
         (transform-normal transformation nn)]))))

;; TODO - bounding box
(defn- list-group [^Transform transformation primitives]
  "Create a primitive group as a list"
  (ListGroup. transformation nil primitives))

;; TODO - choose a grouping strategy based on primitives
(defn group [^Transform transformation primitives]
  "Create a primitive group":pwd
  (list-group transformation primitives))

(defrecord GridGroup [^Transform transformation ^Grid grid]
  Traceable
  (trace [this _r]
         nil
         ))

