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
      (if-let [[t u v] (intersect-triangle-ray (:p0 this) (:p1 this) (:p2 this) _r) ]
        [t (ray-at r t) (-> (cross (v3sub (:p1 this) (:p0 this)) 
                                   (v3sub (:p2 this) (:p0 this)))
                            v3normalize 
                            vector3)]))))

(extend-type BBox
  Traceable
  (trace [this _r]
  (let [^Ray r _r]
    (if-let [t (intersect-bbox-ray this r)]
      (let [p (ray-at r t)
            n (v4sub p (point3 0 0 0))]
        [t p n])))))
