(ns sifaka.io
  (:import [java.io FileInputStream]))

(defn read-file
  ^{:doc "Read a (binary) file into a byte array."}
  [name]
  (let [f (FileInputStream. name)
        ba (byte-array (.available f))] ; Assumption: ".available" returns all.
    (.read f ba)
    (.close f)
    ba))
