(ns ISPro2MergUtil.gesturemerge)

(def path "C:\\Users\\Shannon\\Desktop\\Research\\Projects\\IntellentSystemsClass\\Output\\Weka\\gesture\\")

(def output-name-str "gesture-processed.arff")

(def config-file "DataFormatTemplate.txt")

(defn get-file-class [] 
  "returns [class file-name index]"
  (let [files (filter #(= (last (clojure.string/split % #"\.")) "txt")
                      (for [file (.listFiles (clojure.java.io/file path)) ]
                        (last 
                          (clojure.string/split
                            (str file) #"\\"))))]
    (map
      #(conj %1 %2)
      (for[in-file files]
        [(first (clojure.string/split in-file  #"-")) in-file])
      (range (count files)))))

(def file-class-seq-v (get-file-class))

(def sequence-length (count file-class-seq-v))

(defn get-file [path file-name]
  "get full file as string"
  (slurp (str path file-name)))

(defn split-into-line [file-str]
  "takes file and splits by lines"
  (clojure.string/split 
       file-str
        #"\n"))
  

(defn tokenize-line [line-str]
  "takes a line of text and splits by ,"
        (map 
          clojure.string/trim 
          (clojure.string/split line-str #",")))

(defn get-tokens-pos [tokens loc-v] 
  "takes list of tokens and positions and gets values of loc-v positions"
  (map 
    (fn [token-loc] (nth tokens token-loc))
    loc-v))

(defn tokenize-file-by-line [path file-name]
  "returns list of lines that are tokenized"
  (map tokenize-line 
         (split-into-line 
           (get-file path file-name))))

(defn get-file-attribute [file-name loc-v]
  "returns a list of values by location for each line of the file"
    (map 
      #(get-tokens-pos % loc-v)
      (tokenize-file-by-line path file-name)))
  
(defn list-str-to-float [str-list]
  (map #(java.lang.Float/parseFloat %) 
                      str-list))

(defn abs-of-float [float-list]
  (map #(java.lang.Math/abs %) float-list))

(defn get-attr-class [instance ]
  "gets most extreme and creates class index and sign  exp: 1 means +y direction -0 means -x direction"
  (let [numb-col (list-str-to-float instance)
        numb-abs-col (abs-of-float numb-col)
        max-num (apply max numb-abs-col)
        ext-ind (first (keep-indexed #(if (= max-num  %2)  %1) numb-abs-col))
        num-class  (first (clojure.string/split
                            (str (* (java.lang.Math/signum (nth numb-col ext-ind)) ext-ind)) #"\."))]
    ((keyword num-class) {:-1 "down" :1 "up" :0 "right" :-0 "left" :2 "pull" :-2 "push"})))


(defn create-sample [file-name loc-v class seq-num]
  (str 
    "seq-" 
    seq-num
    ","
    class
    ",'"
   (apply str (interpose
       "\\n"
       (if (< (count loc-v) 4)
         (map get-attr-class 
              (get-file-attribute file-name loc-v))
         (map get-attr-class
              (map (fn [x] 
                     (map (fn[x y] 
                            (str (+ (java.lang.Float/parseFloat x) (java.lang.Float/parseFloat y)))) 
                          (first x) (second x)))
                   (map #(partition 3 %) (get-file-attribute file-name loc-v)))))))"'\n"))

(def config-data 
  (str "@relation test\n@attribute seq-id {"
       (apply str (interpose "," (map #(str "seq-" %) (range sequence-length))))     
       "}\n@attribute class {up,down,push,pull,right,left}\n@attribute sequence relational\n"
       "@attribute output {up,down,push,pull,right,left}\n"
       "@end sequence\n@data\n"))

(def loc-v [6 7 8])

(spit (str path output-name-str) 
      (str config-data (apply str ;string seq
                  (seq ;force lazzy seq to evaluate
    
               (map #(create-sample (second %) loc-v (first %) (last %)) file-class-seq-v)))))