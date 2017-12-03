(ns clojure.core.match.regex
  (:use [clojure.core.match
         :only [emit-pattern to-source groupable?]])
  (:import java.util.regex.Pattern))

;; # Regular Expression Extension
;;
;; This extension adds support for Clojure's regular expression syntax.

(defrecord RegexPattern [regex])

(defmethod emit-pattern java.util.regex.Pattern
  [pat]
  (RegexPattern. pat))

;; Regular expressions are matched with `re-matches`.
;;
;; For example, given a pattern `#"olive"` and occurance `q`, a match occurs
;; when this expression is true:
;;
;; `(re-matches #"olive" q)`

(defmethod to-source RegexPattern
  [pat ocr]
  `(re-matches ~(:regex pat) ~ocr))

;; `java.util.regex.Pattern` doesn't override `equals`, so we reinvent it here.
;;
;; Two `Pattern`s are equal if they have the same pattern and the same flags.

(defmethod groupable? [RegexPattern RegexPattern]
  [a b]
  (let [^Pattern ra (:regex a)
        ^Pattern rb (:regex b)]
    (and (= (.pattern ra) (.pattern rb))
         (= (.flags ra) (.flags rb)))))

