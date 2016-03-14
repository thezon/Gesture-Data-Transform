(ns ISPro2MergUtil.core)

(def path "C:\\Users\\Shannon\\Desktop\\Research\\Projects\\IntellentSystemsClass\\Output\\Weka\\static signs\\")
(def out-file
  "C:\\Users\\Shannon\\Desktop\\Research\\Projects\\IntellentSystemsClass\\Output\\Weka\\static signs\\static-sign.txt")

(def output-name-str "static-sign-processed.arff")
(def sample true)

(def config-file "DataFormatTemplate.txt")

(defn file-names-class []
       (for [letter ["A" "B" "C" "D" "E" "F" "G" 
                     "H" "I" "K" "L" "M" "N" "O" 
                     "P" "Q" "R" "S" "T" "U" "V" 
                     "W" "X" "Y"]
             post-fix ["-p1.txt" "-p2.txt" "-p3.txt" "-p4.txt"]]
          [letter (str letter post-fix)]))
         
(defn transform-single-file[f-name class]
    (if sample
      (str (first (clojure.string/split (slurp (str path f-name)) #"\n")) (str "," class "\n"))
      (clojure.string/replace (slurp (str path f-name)) #"\n" (str "," class "\n"))))
           

(defn transform-files[]
  (apply str
         (for [file-tranform (file-names-class)]
           (transform-single-file (second file-tranform) (first file-tranform)))))

(defn transform [output-name-str]
  (spit (str path output-name-str) (str (slurp (str path config-file)) (transform-files))))

;(transform output-name-str)