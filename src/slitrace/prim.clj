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


(deftype ListGroup [^Transform transformation ^BBox bounds primitives]
  Traceable
  (trace [this _r]
    (let [^Ray r _r
          ^Ray r-local (transform r (inverse transformation))
          [tn pn nn :as geometry] (trace-list r-local primitives)]
      (if geometry
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
(comment (do 
  (use '[slitrace core shape prim]) 
  (use '[sligeom core bounding transform intersect aggregate])
  (defn test-grid [] (grid (bbox (point3 0 0 0) (point3 9 9 9)) 12))
  (defn point-grid-seq [g r] (map #(voxel-to-point % g)(grid-seq g r)))

  (def G (grid-add (sphere 4.0) (test-grid)))
  (def G2 (grid-add (instance (translate 2 0 0) (sphere 2.0)) (test-grid)))
  (def r2 (ray (point3 9 1 1) (vector3 -1 0 0)))
  (->> (grid-seq G2 r2)
	   (map #(voxel-idx % G2))
	   (map (:voxels G2))
	   (map #(trace-list r2 %))
           (some identity))))

(defn trace-grid [g r]
  (->> (grid-seq g r)
	   (map #(voxel-idx % g))
	   (map (:voxels g))
	   (map (partial trace-list r))
           (some identity)))
