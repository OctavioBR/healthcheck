(ns schema.utils
  "Private utilities used in schema implementation."
  (:refer-clojure :exclude [record?])
        (:require [clojure.string :as string])
                  
                            
                                   
                                      
                                                           )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Miscellaneous helpers

(defn assoc-when
  "Like assoc but only assocs when value is truthy.  Copied from plumbing.core so that
   schema need not depend on plumbing."
  [m & kvs]
  (assert (even? (count kvs)))
  (into (or m {})
        (for [[k v] (partition 2 kvs)
              :when v]
          [k v])))

(defn type-of [x]
        (class x)
                             )

(defn fn-schema-bearer
  "What class can we associate the fn schema with? In Clojure use the class of the fn; in
   cljs just use the fn itself."
  [f]
        (class f)
          )

(defn format* [fmt & args]
  (apply       format                       fmt args))

(def max-value-length (atom 19))

(defn value-name
  "Provide a descriptive short name for a value."
  [value]
  (let [t (type-of value)]
    (if (<= (count (str value)) @max-value-length)
      value
      (symbol (str "a-"       (.getName ^Class t)         )))))

(defmacro char-map []
  clojure.lang.Compiler/CHAR_MAP)

(defn unmunge
  "TODO: eventually use built in demunge in latest cljs."
  [s]
  (->> (char-map)
       (sort-by #(- (count (second %))))
       (reduce (fn [^String s [to from]] (string/replace s from (str to))) s)))

(defn fn-name
  "A meaningful name for a function that looks like its symbol, if applicable."
  [f]
                 
                                                                           
                          
        (let [s (.getName (class f))
              slash (.lastIndexOf s "$")
              raw (unmunge
                   (if (>= slash 0)
                     (str (subs s 0 slash) "/" (subs s (inc slash)))
                     s))]
          (string/replace raw #"^clojure.core/" "")))

(defn record? [x]
        (instance? clojure.lang.IRecord x)
                               )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Error descriptions

;; A leaf schema validation error, describing the schema and value and why it failed to
;; match the schema.  In Clojure, prints like a form describing the failure that would
;; return true.

(declare validation-error-explain)

(deftype ValidationError [schema value expectation-delay fail-explanation]
                         
                                       
                                                                    )

(defn validation-error-explain [^ValidationError err]
  (list (or (.-fail-explanation err) 'not) @(.-expectation-delay err)))

      ;; Validation errors print like forms that would return false
(defmethod print-method ValidationError [err writer]
  (print-method (validation-error-explain err) writer))

(defn make-ValidationError
  "for cljs sake (easier than normalizing imports in macros.clj)"
  [schema value expectation-delay fail-explanation]
  (ValidationError. schema value expectation-delay fail-explanation))


;; Attach a name to an error from a named schema.
(declare named-error-explain)

(deftype NamedError [name error]
                         
                                       
                                                               )

(defn named-error-explain [^NamedError err]
  (list 'named (.-error err) (.-name err)))

      ;; Validation errors print like forms that would return false
(defmethod print-method NamedError [err writer]
  (print-method (named-error-explain err) writer))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Monoidish error containers, which wrap errors (to distinguish from success values).

(defrecord ErrorContainer [error])

(defn error
  "Distinguish a value (must be non-nil) as an error."
  [x] (assert x) (->ErrorContainer x))

(defn error? [x]
  (instance? ErrorContainer x))

(defn error-val [x]
  (when (error? x)
    (.-error ^ErrorContainer x)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Registry for attaching schemas to classes, used for defn and defrecord

     
(let [^java.util.Map +class-schemata+ (java.util.Collections/synchronizedMap (java.util.WeakHashMap.))]
  (defn declare-class-schema! [klass schema]
    "Globally set the schema for a class (above and beyond a simple instance? check).
   Use with care, i.e., only on classes that you control.  Also note that this
   schema only applies to instances of the concrete type passed, i.e.,
   (= (class x) klass), not (instance? klass x)."
    (assert (class? klass)
            (format* "Cannot declare class schema for non-class %s" (class klass)))
    (.put +class-schemata+ klass schema))

  (defn class-schema [klass]
    "The last schema for a class set by declare-class-schema!, or nil."
    (.get +class-schemata+ klass)))

      
   
                                            
                                              

                            
                                        


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Utilities for fast-as-possible reference to use to turn fn schema validation on/off

(def use-fn-validation
  "Turn on run-time function validation for functions compiled when
   s/compile-fn-validation was true -- has no effect for functions compiled
   when it is false."
  ;; specialize in Clojure for performance
        (java.util.concurrent.atomic.AtomicReference. false)
                     )

;;;;;;;;;;;; This file autogenerated from src/cljx/schema/utils.cljx
