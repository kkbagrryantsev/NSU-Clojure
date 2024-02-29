(ns task5)

(def n-philosophers 5)
(def n-forks n-philosophers)
(def time-to-think 10)
(def time-to-eat 9)
(def n-periods 100)

(def transactions-starts (atom 0))
(def transactions-finishes (atom 0))

(def forks (take n-forks (repeatedly #(ref 0))))

(defn philosopher [left-fork, right-fork, i]
  (dorun n-periods
         (repeatedly
           (fn []
             (println (str "The philosopher " i " is thinking"))
             (Thread/sleep (long time-to-think))
             (dosync
               (swap! transactions-starts inc)
               (alter left-fork inc)
               (println (str "The philosopher " i " picked up the left fork"))
               (alter right-fork inc)
               (println (str "The philosopher " i " picked up the right fork"))
               (Thread/sleep (long time-to-eat))
               (println (str "The philosopher " i " finished the meal"))
               (swap! transactions-finishes inc))))))

(def threads
  (doall
    (map
      (fn [i]
        (new Thread
             (fn []
               (philosopher (nth forks i) (nth forks (mod (inc i) n-forks)) i))
             ))
      (range n-philosophers))
    ))

;(println threads)
(doall (map #(.start %) threads))
(doall (map #(.join %) threads))

(println "Transaction starts" transactions-starts)
(println "Transaction finishes" transactions-finishes)
(println "Transaction restarts" (- @transactions-starts @transactions-finishes))
(println forks)