(ns slitrace.camera
  (:use [slimath core vec matrix]
        [sligeom core transform intersect])
  (:import [sligeom.transform Transform]))

(defrecord Camera [^long width ^long height 
                   ^Transform transformation ^Transform projection]
  Transformable
  (transform [this T] 
    (Camera. width height (compose T transformation) projection)))

(defn perspective-camera 
  [{:keys [width height transformation fov near far] 
    :or {transformation (ident) fov 38.0 near 1.0 far 1000.0}
    :as params}]
  (map->Camera (assoc params 
                 :projection  
                 (perspective :fov-rads (Math/toRadians fov)
                              :aspect (double (/ width height)) 
                              :near near
                              :far far))))

(defn camera-ray 
  "Create a ray in camera-space from a screne-space coordinate using the 
camera-to-screen transform."
  [^double x ^double y ^Transform camera-to-screen]
  (ray (point3 0 0 0)
       (v4sub (transform-point (inverse camera-to-screen) (point3 x y -1))
              (point3 0 0 0))))

(defn screen-projection-transform
  "Concatenated screen-projection transformation for a camera"
 [^Camera camera]
  (compose (screen-transform (:width camera) (:height camera))
                    (:projection camera)))
