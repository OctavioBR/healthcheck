(ns net.cgrand.parsley.berk)




;; 2. collect new rules
(extend-protocol RuleFragment
  Object
    (unsugar [this]
      (if (= "" this) [] this))
    (collect [this unspaced top-rulename]
      nil)
    (develop [this rewrite space]
      [[this]])
    
  ;; a ref to another rule: add support for + ? or * suffixes
  clojure.lang.Keyword
    (unsugar [kw]
      (if-let [[_ base suffix] (re-matches #"(.*?)([+*?])" (name kw))] 
        (unsugar [(keyword base) (keyword suffix)])
        kw))
    (collect [this unspaced top-rulename]
      nil)
    (develop [this rewrite space]
      [[this]])
    
  ;; a set denotes an alternative
  clojure.lang.IPersistentSet
    (unsugar [this]
      (set (map unsugar this)))
    (collect [items unspaced top-rulename]
      (mapcat #(collect % unspaced top-rulename) items))
    (develop [items rewrite space]
      (mapcat #(rewrite % space) items))
    
  ;; a vector denotes a sequence, supports postfix operators :+ :? and :*
  clojure.lang.IPersistentVector
    (unsugar [this]
      (reduce #(condp = %2 
                 :* (conj (pop %1) #{[] (Repeat+. (peek %1))}) 
                 :+ (conj (pop %1) (Repeat+. (peek %1)))
                 :? (conj (pop %1) #{[](peek %1)})
                 (conj %1 (unsugar %2))) [] this))
    (collect [items unspaced top-rulename]
      (mapcat #(collect % unspaced top-rulename) items))
    (develop [items rewrite space]
      (reduce #(for [x (rewrite %2 space) sp space xs %1] 
                 (concat x (and (seq x) (seq xs) sp) xs))
        [()] (rseq items))))

;; with code-dispatch
(extend-protocol
  RuleFragment
  Object
  (unsugar [this] (if (= "" this) [] this))
  (collect [this unspaced top-rulename] nil)
  (develop [this rewrite space] [[this]])
  clojure.lang.Keyword
  (unsugar
    [kw]
    (if-let [[_ base suffix] (re-matches #"(.*?)([+*?])" (name kw))]
      (unsugar [(keyword base) (keyword suffix)])
      kw))
  (collect [this unspaced top-rulename] nil)
  (develop [this rewrite space] [[this]])
  clojure.lang.IPersistentSet
  (unsugar [this] (set (map unsugar this)))
  (collect
    [items unspaced top-rulename]
    (mapcat #(collect % unspaced top-rulename) items))
  (develop [items rewrite space] (mapcat #(rewrite % space) items))
  clojure.lang.IPersistentVector
  (unsugar
    [this]
    (reduce
      #(condp
        =
        %2
        :*
        (conj (pop %1) #{[] (Repeat+. (peek %1))})
        :+
        (conj (pop %1) (Repeat+. (peek %1)))
        :?
        (conj (pop %1) #{[] (peek %1)})
        (conj %1 (unsugar %2)))
      []
      this))
  (collect
    [items unspaced top-rulename]
    (mapcat #(collect % unspaced top-rulename) items))
  (develop
    [items rewrite space]
    (reduce
      #(for
        [x (rewrite %2 space) sp space xs %1]
        (concat x (and (seq x) (seq xs) sp) xs))
      [()]
      (rseq items))))

;; without code-dispatch
(extend-protocol
 RuleFragment
 Object
 (unsugar [this] (if (= "" this) [] this))
 (collect [this unspaced top-rulename] nil)
 (develop [this rewrite space] [[this]])
 clojure.lang.Keyword
 (unsugar
  [kw]
  (if-let
   [[_ base suffix] (re-matches #"(.*?)([+*?])" (name kw))]
   (unsugar [(keyword base) (keyword suffix)])
   kw))
 (collect [this unspaced top-rulename] nil)
 (develop [this rewrite space] [[this]])
 clojure.lang.IPersistentSet
 (unsugar [this] (set (map unsugar this)))
 (collect
  [items unspaced top-rulename]
  (mapcat
   (fn* [p1__5947#] (collect p1__5947# unspaced top-rulename))
   items))
 (develop
  [items rewrite space]
  (mapcat (fn* [p1__5948#] (rewrite p1__5948# space)) items))
 clojure.lang.IPersistentVector
 (unsugar
  [this]
  (reduce
   (fn*
    [p1__5950# p2__5949#]
    (condp
     =
     p2__5949#
     :*
     (conj (pop p1__5950#) #{[] (Repeat+. (peek p1__5950#))})
     :+
     (conj (pop p1__5950#) (Repeat+. (peek p1__5950#)))
     :?
     (conj (pop p1__5950#) #{[] (peek p1__5950#)})
     (conj p1__5950# (unsugar p2__5949#))))
   []
   this))
 (collect
  [items unspaced top-rulename]
  (mapcat
   (fn* [p1__5951#] (collect p1__5951# unspaced top-rulename))
   items))
 (develop
  [items rewrite space]
  (reduce
   (fn*
    [p1__5953# p2__5952#]
    (for
     [x (rewrite p2__5952# space) sp space xs p1__5953#]
     (concat x (and (seq x) (seq xs) sp) xs)))
   [()]
   (rseq items))))