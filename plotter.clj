 {:position {:x 0 :y 0}
   :angle 0
   :color :black
   :carriage-state :up})


(defn- deg->rad [deg] (* deg (/ Math/PI 180.0)))

(defn- calc-new-position [distance angle {:keys [x y]}]
  (let [rad (deg->rad angle)
        nx (+ x (* distance (Math/cos rad)))
        ny (+ y (* distance (Math/sin rad)))]
    {:x (Math/round nx) :y (Math/round ny)}))

(defn move [state distance]
  (let [{:keys [position angle carriage-state color]} state
        new-pos (calc-new-position distance angle position)]
    (when (= carriage-state :down)
      (println (format "...Чертим линию из (%d, %d) в (%d, %d) используя %s цвет."
                       (:x position) (:y position) (:x new-pos) (:y new-pos) (name color))))
    (when (= carriage-state :up)
      (println (format "Передвигаем на %d от точки (%d, %d)" distance (:x position) (:y position))))
    (assoc state :position new-pos)))

(defn turn [state angle]
  (println (format "Поворачиваем на %d градусов" angle))
  (assoc state :angle (mod (+ (:angle state) angle) 360)))

(defn carriage-up [state]
  (println "Поднимаем каретку")
  (assoc state :carriage-state :up))

(defn carriage-down [state]
  (println "Опускаем каретку")
  (assoc state :carriage-state :down))

(defn set-color [state color]
  (println (format "Устанавливаем %s цвет линии." (name color)))
  (assoc state :color color))

(defn set-position [state position]
  (println (format "Устанавливаем позицию каретки в (%d, %d)." (:x position) (:y position)))
  (assoc state :position position))

;; Фигуры
(defn draw-triangle [size state]
  (->> (range 3)
       (reduce (fn [s _] (-> s (move size) (turn 120))) (carriage-down state))
       carriage-up))

(defn draw-square [size state]
  (->> (range 4)
       (reduce (fn [s _] (-> s (move size) (turn 90))) (carriage-down state))
       carriage-up))

;; Точка входа
(defn -main []
  (-> initial-state
      (draw-triangle 100)
      (set-position {:x 10 :y 10})
      (set-color :red)
      (draw-square 80)
      (assoc :done true)))
