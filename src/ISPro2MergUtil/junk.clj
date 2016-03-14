(ns ISPro2MergUtil.junk)

(comment
  (defn file-names-class []
  (for [file-pre
        (partition
          2 (flatten 
              (for [ name ["birchell" "gearhart" "hubbard"
                           "jhoeppner" "khoeppner" "kongara"
                           "lambert" "lepley" "metcalfe"
                           "morais" "naga" "perry" 
                           "spence" "tata" "travis"
                           "valley" "wagner" "waxman"]]
                (map #(vector % (str % "-" name))
                     ["down" "up" "right" "left" "push" "pull"]))))]
   (map #(vector (first file-pre) (str (second file-pre) "-" %)) ["1.txt" "2.txt" "3.txt"]))))