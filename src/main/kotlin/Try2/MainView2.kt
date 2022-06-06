package Try2

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import tornadofx.*
import java.awt.event.MouseEvent

class MVCView : View("MVC App") {

    private val kor: Board by inject()


    override val root = pane {
        this.add(kor.root)
        this.add(kor.path)
        this.add(kor.allCircles)
        this.add(kor.pick)
        var allCircles = kor.allCircles.children
        val pathList = kor.path.children
        val pick = kor.pick.children
        println(allCircles.size)
        allCircles.removeAt(1)

        fun choose(evt: javafx.scene.input.MouseEvent) {

            allCircles.firstOrNull {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
            }.apply {
                this as Circle?

                pathList.firstOrNull{
                    val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                    it.contains(mousePt)
                }.apply {
                    if (this != null) {
                        val pieceRadius = 30
                        this as Circle?
                        val picked = pick[0] as Circle
                        val newLocX = this.centerX - pieceRadius
                        val newLocY = this.centerY - pieceRadius

                        allCircles.find { e -> e.contains(picked.centerX,picked.centerY)  }?.apply {
                            centerX = newLocX
                            centerY = newLocY
                            relocate(centerX - pieceRadius,centerY - pieceRadius )
                            centerX = newLocX + pieceRadius
                            centerY = newLocY + pieceRadius
                        }
                    }
                }

                pick[0].relocate(935.0, 135.0)
                pathList.forEach { it.relocate(935.0,135.0) }

                if (this != null) {
                    var count = 0
                    val pickX = this.centerX
                    val pickY = this.centerY
                    while (count != 3) {
                        val list = listOf(Pair(100, 100), Pair(-100, 100), Pair(100, -100), Pair(-100, -100))
                        var signX = list[count].first
                        var signY = list[count].second

                        if (count in 0..1) {
                            if (this.fill == Color.WHITE) signY = -signY
                            val diagonal = allCircles
                                .find { e -> e.contains(this.centerX + signX, this.centerY + signY) } as Circle?
                            var diagonal2: Circle? = null
                            if (diagonal != null && diagonal.fill != this.fill) {
                                signX *= 2
                                signY *= 2
                                diagonal2 = allCircles
                                    .find { e -> e.contains(this.centerX + signX, this.centerY + signY) } as Circle?
                            }
                            if (diagonal?.fill != this.fill && diagonal2 == null && count in 0..3) {
                                    pathList[count].relocate(this.centerX + signX - 15, this.centerY + signY - 15)
                            }
                            pick.apply {
                                this[0].relocate(pickX - 15,pickY -15)
                            }
                            println(pick)
                        }
                        count += 1
                    }
                }
            }

        }
        addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, ::choose)
    }

}


class Board : View() {
    val allCircles = group()
    val path = group()
    val pick = group()
    override val root: Pane = pane {

        fun pieces(startX: Double, startY: Double, f: Color): Circle {
            return circle(startX, startY) {
                fill = f
                when (f) {
                    Color.BLACK -> {
                        run {
                            radius = 30.0
                            stroke = Color.WHITE
                            strokeWidth = 3.0
                        }
                        allCircles.children.add(this)
                    }
                    Color.WHITE -> {
                        run {
                            radius = 30.0
                            stroke = Color.BLACK
                            strokeWidth = 3.0
                        }
                        allCircles.children.add(this)
                    }
                    Color.GREEN -> {
                        run {
                            radius = 15.0
                            pick.children.add(this)
                        }
                    }
                    Color.DARKBLUE -> {
                        run {
                            radius = 15.0
                            path.children.add(this)
                        }
                    }
                }
            }
        }

        val topBoard = {
            var tile = 0
            var xCor = 0
            var yCor = 0
            while (yCor != 800) {

                while (xCor != 800) {

                    if (tile % 2 == 0) {
                        rectangle(xCor, 700 - yCor, 100.0, 100.0) {
                            fill = Paint.valueOf("#a94442")
                        }.toBack()
                        when (yCor != 300 || yCor != 200) {
                            (yCor < 300) -> pieces(50.0 + xCor, 750.0 - yCor, Color.WHITE)
                            (yCor > 400) -> pieces(50.0 + xCor, 750.0 - yCor, Color.BLACK)
                        }

                    }
                    tile += 1
                    xCor += 100
                }
                xCor = 0
                tile += 1
                yCor += 100
            }
            rectangle(0, 0, 800.0, 800.0).toBack()
            pieces(850.0, 50.0, Color.DARKBLUE)
            pieces(1050.0, 50.0,  Color.DARKBLUE)
            pieces(950.0, 150.0,  Color.GREEN)
            pieces(850.0, 250.0,  Color.DARKBLUE)
            pieces(1050.0, 250.0, Color.DARKBLUE)
        }
        topBoard.invoke()
    }

}


//class MVCView : View("MVC App") {
//
//    val form : MVCFormView by inject()
//    val status : MVCStatusView by inject()
//
//    override val root = vbox {
//        add(form)
//        separator()
//        add(status)
//
//        prefWidth = 480.0
//        prefHeight = 320.0
//
//    }
//}
//
//class MVCFormView : View() {
//
//    val data = SimpleStringProperty()
//
//    override val root = form {
//        fieldset {
//            field("Data") {
//                textfield(data)
//            }
//        }
//        button("Save") {
//            action {
//                find<MVCController>().save()  // breaks by inject cycle
//            }
//        }
//
//        padding = Insets(10.0)
//        vgrow = Priority.ALWAYS
//        alignment = Pos.CENTER_LEFT
//    }
//}
//
//class MVCStatusView : View() {
//
//    val lastMessage = SimpleStringProperty()
//
//    override val root = hbox {
//        label(lastMessage)
//        padding = Insets(4.0)
//        vgrow = Priority.NEVER
//    }
//}
//
//class MVCController : Controller() {
//
//    val form : MVCFormView by inject()
//    val status : MVCStatusView by inject()
//
//    fun save() {
//
//        val dataToSave = form.data.value
//
//        // execute save here
//        println("Saving $dataToSave")
//
////        status.lastMessage.value = "Data saved"
//    }
//}