(ns slitrace.shape
  (:use slimath.core)
  (:use sligeom.core)
  (:use sligeom.transform)
  (:use sligeom.bounding)
  (:use [sligeom.intersect :only [ray-at 
                                  intersect-sphere-ray 
                                  intersect-triangle-ray]])
  (:import (sligeom.bounding.Bounded))
  (:use slitrace.core)
  (:import (slitrace.core.Traceable)))

(defrecord Sphere [^double radius]
  Traceable
  (trace [this _r]
    (let [^Ray r _r]
      (if-let [t (intersect-sphere-ray radius r)]
        (let [p (ray-at r t)
              n (v4sub p (point3 0 0 0))]
          [t p n]))))
  Bounded
  (bounding-box [this]
    (bbox (point3 (- radius) (- radius) (- radius))
          (point3 radius radius radius))))

(defn sphere [^double radius] (Sphere. radius))

(defrecord Triangle [p0 p1 p2]
  Traceable
  (trace [this r]
    (let [^Ray _r r]
      ((intersect-triangle-ray p0 p1 p2 _r)) 0))

  Bounded
  (bounding-box [this]
    (bbox-union (bbox p0 p1) p2)))

(defn triangle [p0 p1 p2] (Triangle. p0 p1 02))
