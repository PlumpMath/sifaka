(ns user
  (:require (sifaka [io :as io]
                    [util :as u]
                    [packets :as p]
                    [object-common :as com]
                    [objects :as obj]
                    [xml-fu :as x])
            (sifaka.examples [sierpinski :as sp])
            (clojure [prxml :as px]))
  (:import [java.nio.channels DatagramChannel]
           [java.nio ByteBuffer]
           [java.net InetAddress InetSocketAddress DatagramSocket DatagramPacket]))


;; --- Basic XML generation.

(with-out-str (px/prxml [:p] [:q]))

(with-out-str (px/prxml [:p {:class "greet"} [:i "Ladies & gentlemen"]]))

(x/format-project-for-file [:fooble] [:gooble])

(x/format-project-for-upload [:fooble] [:gooble])

(with-out-str (p/prxml (x/project "Hello World")))

(with-out-str (p/prxml (x/fudge-container [100 100] [200 50] [80 80 80] [:foo])))

;; Nested container example. (container() takes varargs for the contents.)

(defn boz [size]
  (if (< size 30)
    nil
    (obj/container {:position [0 0]
                    :size [size size]
                    :colour [80 80 80]}
                   (boz (- size 25)))))

(boz 200)

(io/transmit-payload
 "10.0.0.125"
 8002
 (.getBytes (x/format-project-for-upload
             (x/project "TestProject")
             (x/interface [(obj/container
                            {:position [100 100]
                             :size [500 500]
                             :colour [80 120 120]}
                            (boz 600))]))))

(sp/sierpinski 3)

;; More examples.

(io/transmit-payload
 "10.0.0.125"
 8002
 (.getBytes (x/format-project-for-upload
             (x/project "TestProject")
             (x/interface [(obj/container
                            {:position [100 100]
                             :size [300 300]
                             :colour [80 120 120]}
                            (obj/pads {:id 987
                                       :name "pads"
                                       :position [0 0]
                                       :size [284 284]
                                       :off-colour [50 50 50]
                                       :on-colour [255 255 255]}))]))))

(io/transmit-payload
 "10.0.0.125"
 8002
 (.getBytes (x/format-project-for-upload
             (x/project "TestProject")
             (x/interface [(obj/container
                            {:position [100 100]
                             :size [300 300]
                             :colour [80 120 120]}
                            (obj/ringarea {:id 987
                                           :name "ra"
                                           :position [0 0]
                                           :size 284
                                           :colour [150 150 150]}))]))))

(io/transmit-payload
 "10.0.0.125"
 8002
 (.getBytes (x/format-project-for-upload
             (x/project "TestProject")
             (x/interface [(obj/container
                            {:position [10 10]
                             :size [700 700]
                             :colour [80 120 120]}
                            (map #(obj/ringarea
                                   {:id %
                                    :name (format "ra%d" %)
                                    :position [(int (* 278 (inc (Math/sin (* % Math/PI 0.125)))))
                                               (int (* 278 (inc (Math/cos (* % Math/PI 0.125)))))]
                                    :size 120
                                    :colour [(- 255 (* % 10)) 200 150]}) (range 16)))]))))

(io/transmit-payload
 "10.0.0.125"
 8002
 (.getBytes
  (x/format-project-for-upload
   (x/project "TestProject")
   (x/interface
    [(obj/container
      {:position [150 5]
       :size [(+ 16 (* 27 25)) (+ 16 (* 27 25))]
       :colour [100 100 100]}
      (for [[xx yy] (:points (sp/sierpinski 3))]
        (let [c (if (even? (+ xx yy)) [255 255 255] [100 100 140])]
          (obj/pads {:id (+ xx (* yy 27))
                     :name "MyButton"
                     :position [(* xx 25) (* yy 25)]
                     :size [25 25]
                     :off-colour c
                     :on-colour [255 0 0]}))))]))))

;; -----

(obj/pads {:id 345
           :name "Hello"
           :position [50 50]
           :size [200 200]
           :off-colour [0 0 0]
           :on-colour [1 1 1]})

(with-out-str (px/prxml (obj/pads {:id 345
                                   :name "Hello"
                                   :position [50 50]
                                   :size [200 200]
                                   :off-colour [0 0 0]
                                   :on-colour [1 1 1]})))


(com/env "my-obj" {})
