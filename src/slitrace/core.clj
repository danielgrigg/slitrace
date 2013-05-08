(ns slitrace.core
  (:use [sligeom intersect])
  (:import [sligeom.intersect Ray]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* true)

(defprotocol Traceable
  (trace [this r] ))

(defrecord TraceSample [ray position normal])

(defn trace-sample
  ([^Ray r]
     (TraceSample. r nil nil))
  ([^Ray r position normal]
     (TraceSample. r position  normal)))

