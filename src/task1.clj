(ns task1)

;(defn symbol-not-in-string? [symbol str]
;  (nil? (clojure.string/index-of str symbol))
;  )

(defn not-equal-subsequent? [symbol str]
  (not= (last symbol) (last str))
  )

(defn concat-prefixes [prefixes alphabet]
        (for [prefix prefixes
              symbol alphabet
        :let [result (str prefix symbol)]                   ; append symbol to prefix
        :when (not-equal-subsequent? symbol prefix)]        ; check the added symbol is not in prefix
          result)
        )

; Definitions
(def alphabet ["a" "b" "c"])
(def N 4)

; All alphabet symbols' combinations of length N starting with a given symbol with no equal subsequent characters
(defn get-combinations-for-symbol [symbol] (reduce concat-prefixes [symbol] (repeat (dec N) alphabet)))

; Gets all alphabet symbols' combinations of length N with no equal subsequent characters
(def get-combinations (into [] (for [symbol alphabet] (get-combinations-for-symbol symbol))))

; Flattens 2-dimensional list
(defn ravel [values] (reduce concat [] values))

(println (ravel get-combinations))
