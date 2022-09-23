package Try2


import Try2.model.rectangleLocation
import Try2.model.setup
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.Controller
import tornadofx.removeFromParent

// moves and holds group such as lists
const val ratio = 100
var recList = listOf<Rectangle>()
var picList = listOf<Circle>()
val emptyBoard: IntRange = 13..20
const val zero = 0
val whiteSide: IntRange = 20..32
class MotionControl : Controller()  {

        init {
                if (rectangleLocation.size == 1) setup()
        }

        fun delete(circ: Circle){
                var newList = listOf<Circle>()
                for (i in picList){
                        if (i != circ) newList += i else picList.find { it == circ }?.removeFromParent()
                }
                picList = newList
        }
        fun move(circ: Circle){
                var travel = listOf<Int>()
                var length = 0..700
        }

        var boardSetUp = rectangleLocation
}
