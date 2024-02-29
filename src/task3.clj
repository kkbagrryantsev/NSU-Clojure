(ns task3
  (:require [clojure.test :refer :all]))

(defn async-process-chunk [chunk-size threads f chunks]
  (or
    (when (seq chunks)
      (concat
        (let [
              part-size (->> chunk-size (/ threads))
              parts (partition-all part-size (first chunks))
              ]
          (->> parts
               (map #(future (doall (f %))))
               (doall)
               (map deref)
               (flatten)
               )
          )
        (lazy-seq (async-process-chunk chunk-size threads f (rest chunks)))
        )
      )
    []
    )
  )

(defn async-parallel-filter [pred coll]
  (let [chunk-size 1000
        n-threads 4]
    (async-process-chunk chunk-size n-threads #(filter pred %) (partition-all chunk-size coll)))
  )


(deftest test-parallel-filter-basic
  (is (= (async-parallel-filter even? (range 10)) '(0 2 4 6 8))))

(deftest test-parallel-filter-empty-sequence
  (is (= (async-parallel-filter even? ()) '())))

(let [start-time (System/currentTimeMillis)
      result (take 1001 (async-parallel-filter even? (range)))]
  (println result)
  (println (str "Elapsed time for take operation: " (- (System/currentTimeMillis) start-time) " ms")))

(let [start-time (System/currentTimeMillis)
      result (nth (async-parallel-filter even? (range)) 10000)]
  (println result)
  (println (str "Elapsed time for nth operation: " (- (System/currentTimeMillis) start-time) " ms")))

(let [start-time (System/currentTimeMillis)
      result (nth (filter even? (range)) 10000)]
  (println result)
  (println (str "Elapsed time for sequential filter operation: " (- (System/currentTimeMillis) start-time) " ms")))