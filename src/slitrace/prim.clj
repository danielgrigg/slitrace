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


(defn trace-list "trace a primitive seq" [ray primitives]
  (let [trace-sorted (fn [[t _ _ :as closest-geometry] prim ]
                       (or (trace prim (ray-interval ray t)) closest-geometry))
        closest (reduce trace-sorted [(:maxt ray) nil nil] primitives)]    
    (if (second closest) closest)))

(defn trace-grid [r g]
  (->> (grid-seq g r)
	   (map #(voxel-idx % g))
	   (map (:voxels g))
	   (map (partial trace-list r))
           (some identity)))

(defrecord Group [^Transform transformation ^BBox bounds primitives trace-fn]
  Traceable
  (trace [this _r]
    (let [^Ray r _r
          ^Ray r-local (transform r (inverse transformation))
          [tn pn nn :as geometry] (trace-fn r-local primitives)]
      (if geometry
        [tn (transform-point transformation pn) 
         (transform-normal transformation nn)])))
  Bounded
  (bounding-box [this]
    bounds))

;; TODO - bounding box
(defn list-group [^Transform transformation primitives]
  "Create a primitive group as a list"
  (Group. transformation (bbox primitives) primitives trace-list))

(defn grid-group [^Transform transformation primitives]
  (let [g (reduce grid-conj 
                  (grid (bbox primitives) (count primitives))
                  primitives)]
    (Group. transformation (bounding-box g) g trace-grid)))

;; TODO - choose a grouping strategy based on primitives
(defn group [^Transform transformation primitives]
  "Create a primitive group":pwd
  (grid-group transformation primitives))
