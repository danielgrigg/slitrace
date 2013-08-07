(ns slitrace.shape-test
  (:use midje.sweet
        [sligeom core]
        [slitrace core shape]
        [sligeom.intersect :only [ray]]))

(fact "`trace` traces a ray against primitives"
      (trace (sphere 2.0) (ray (point3 0 0 0) (vector3 1 0 0)))
      => [2.0 [2.0 0.0 0.0 1.0] [2.0 0.0 0.0 0.0]]

      (trace (triangle (point3 -1 -1 -1) (point3 1 -1 -1) (point3 0 1 -1))
             (ray (point3 0 0 0) (vector3 0 0 -1)))
      => [nil nil 1.0])

