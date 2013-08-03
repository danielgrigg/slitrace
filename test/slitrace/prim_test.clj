(ns slitrace.prim-test
  (:use midje.sweet
        slimath.core
        [sligeom core transform]
        [sligeom.intersect :only [ray ray-at ray-interval]]
        [slitrace core shape prim]))

(fact "`trace` traces instanced primitives"
      (let [actual (trace (instance (compose (translate 1 2 3) 
                                          (rotate (vector3 1 2 3) 1.0)) 
                                 (sphere 5.0)) 
                       (ray (point3 0 0 0) (vector3 0 0 -1)))
            expect {:normal [-1.0 -2.0 -4.472135 0.0]
                    :position [0.0 0.0 -1.472135 1.0]
                    :t 1.472135}]
        (point-geometry-approx? actual expect)) => true

      (let [actual (trace (list-group (translate 0 2 0) 
                                      [(sphere 1.0)]) 
                          (ray (point3 -3 2 0) (vector3 1 0.2 0.2)))
        expect {:normal [-0.777777 0.444444 0.444444 0.0]
                :position [-0.777777 2.444444 0.444444 1.0]
                :t 2.222222}]        
        (point-geometry-approx? actual expect)) => true)

