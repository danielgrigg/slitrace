(ns slitrace.camera
  (:use [slimath core]
        [sligeom core transform intersect])
  (:import [sligeom.transform Transform]))

(defn camera-ray 
  "Create a ray in camera-space from a screne-space coordinate using the 
camera-to-screen transform."
  [^double x ^double y ^Transform camera-to-screen]
  (ray (point3 0 0 0)
       (v4sub (transform-point (inverse camera-to-screen) (point3 x y -1))
              (point3 0 0 0))))
