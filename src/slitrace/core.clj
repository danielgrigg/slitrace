(ns slitrace.core
  (:use sligeom.core)
  (:import [sligeom.core Ray]))

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

