(ns slitrace.prim-test
  (:use midje.sweet
        [slimath core vec matrix]
        [sligeom core transform]
        [sligeom core intersect]
        [slitrace core shape prim]))
       
(fact "`trace` traces instanced primitives"
      (let [actual (trace 
                    (instance 
                     (compose (translate 1 2 3) 
                              (rotate (vector3 1 2 3) 1.0)) 
                     (sphere 5.0))
                    (ray (point3 0 0 0) (vector3 0 0 -1)))
            expect [1.472135
                    [0.0 0.0 -1.472135 1.0]
                    [-1.0 -2.0 -4.472135 0.0]                    
                    ]]
        (struct-approx? actual expect))
        => true

      (let [actual (trace (group (translate 0 2 0) 
                                      [(sphere 1.0)]) 
                          (ray (point3 -3 2 0) (vector3 1 0.2 0.2)))
        expect [2.222222
                [-0.777777 2.444444 0.444444 1.0]
                [-0.777777 0.444444 0.444444 0.0]]]
        (struct-approx? actual expect))   
            => true)
