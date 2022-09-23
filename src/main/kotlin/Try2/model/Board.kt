package Try2.model

import Try2.ratio
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

        var rectanglePaint: Paint = Paint.valueOf("#a94442")
        var white: Paint = Paint.valueOf("#ffffff")
        var rectangleLocation = mutableListOf(Pair(0,0))
        fun setup() {
            val distribute = listOf(0,1,2,3,4,5,6,7)
            var evenOdd = 0
            for (y in distribute){
                var x = 0
                while (x != 8){
                    if (evenOdd % 2 == 0 && x % 2 == 0) rectangleLocation.add(Pair(y*ratio,x*ratio))
                    if (evenOdd % 2 != 0 && x % 2 != 0) rectangleLocation.add(Pair(y*ratio,x*ratio))
                    x += 1
                }
                evenOdd +=1
            }
            rectangleLocation.add(Pair(800,0))
        }

//enum class Color(val c : Paint) {
//    Red(Paint.valueOf("#a94442")),
//    Black(Paint.valueOf("#00000000"))
//}

//while (x != 8){
//    location.add(x)
//    x += 1
//}
