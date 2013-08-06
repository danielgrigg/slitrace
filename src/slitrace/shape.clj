(ns slitrace.shape
  (:use [slimath core vec matrix]
        [sligeom core]
        [sligeom transform bounding]
        [sligeom.intersect ]
        [slitrace core])
  (:import [sligeom.bounding BBox]
           [sligeom.intersect Sphere Triangle Plane]))

(extend-type Sphere
  Traceable
  (trace [this _r]
    (let [^Ray r _r]
      (if-let [t (intersect-sphere-ray (:radius this) r)]
        (let [p (ray-at r t)
              n (v4sub p (point3 0 0 0))]
          [t p n])))))

(extend-type Triangle
  Traceable
  (trace [this r]
    (let [^Ray _r r]
      ((intersect-triangle-ray (:p0 this) (:p1 this) (:p2 this) _r)) 0)))

(extend-type BBox
  Traceable
  (trace [this _r]
  (let [^Ray r _r]
    (if-let [t (intersect-bbox-ray this r)]
      (let [p (ray-at r t)
            n (v4sub p (point3 0 0 0))]
        [t p n])))))
