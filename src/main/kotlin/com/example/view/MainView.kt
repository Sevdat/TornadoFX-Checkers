package com.example.view

import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import tornadofx.*
import kotlin.math.abs
import kotlin.reflect.KClass


class MainView : View("Russian Checkers") {
// what is View
    override val root: Pane = pane {
        val rectangle = group()
        val allCircles = group()
        val location = group()
//        val dama = group()
        val list = listOf(Pair(100, 100), Pair(-100, 100), Pair(100, -100), Pair(-100, -100))
    // name better list
    fun choose(evt: MouseEvent) {
            rectangle.children.firstOrNull {
                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                it.contains(mousePt)
            }.apply {
                val loc = location.children
                // name loc better
                loc.firstOrNull {
                    val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
                    it.contains(mousePt)

                }.apply {
                    this as Circle?
                    if (this != null) {
                        val pick = loc[0] as Circle
                        val pickPiece =
                            allCircles.children.find { e -> e.contains(pick.centerX, pick.centerY) } as Circle
                        // val current = allCircles.children.find { e -> e.contains(this.x + 50, this.y + 50) } as Circle?
                        val newLocX = this.centerX
                        val newLocY = this.centerY
                        val substractX = pickPiece.centerX - this.centerX
                        val substractY = pickPiece.centerY - this.centerY
                        // when needs to be uncloned
                        when {
                            (abs(substractY) == 100.0 && abs(substractX) == 100.0) ->
                                allCircles += pickPiece.apply {
                                    centerX = newLocX
                                    centerY = newLocY
                                }
                            (abs(substractY) == 200.0 && abs(substractX) == 200.0) -> {
                                allCircles += pickPiece.apply {
                                    centerX = newLocX
                                    centerY = newLocY
                                }

                                val newPiece =
                                    allCircles.children.find { e ->
                                        e.contains(
                                            pickPiece.centerX + (substractX / 2),
                                            pickPiece.centerY + (substractY / 2)
                                        )
                                    } as Circle?
// make numbers val and name better
                                if (newPiece != null) {
                                    allCircles += newPiece.apply {
                                        centerX = 850.0
                                        centerY = 50.0
                                    }
                                }
                            }
                        }
                    }
                    // dont make clone
                    for (i in allCircles.children){
                        i as Circle
                        when{
                            (i.fill == Color.WHITE && i.centerY == 50.0) ->{
                                allCircles += i.apply {
                                    fill = Color.AQUA
                                    strokeWidth = 8.0
                                }
                            }
                            (i.fill == Color.BLACK && i.centerY == 750.0) ->{
                                allCircles += i.apply {
                                    fill = Color.DARKMAGENTA
                                    strokeWidth = 8.0
                                }
                            }
                        }
                    }
                }
            // name cou better and numbers val
                var cou = 0
                this as Rectangle?
                while (cou != loc.size - 1) {
                    location += loc[cou].apply {
                        this as Circle
                        centerX = 850.0
                        centerY = 50.0
                    }
                    cou += 1
                }

                if (this != null) {
                    val current = allCircles.children.find { e -> e.contains(this.x + 50, this.y + 50) } as Circle?
                    var count = 0
                    while (count != 4) {
                        var signX = list[count].first
                        var signY = list[count].second
                        if (count in 0..4) {
                           if (current?.fill != Color.AQUA || current?.fill != Color.DARKMAGENTA) {
                            if (current?.fill == Color.WHITE) signY = -signY
                            val diagonal = allCircles.children
                                .find { e -> e.contains(this.x + signX + 50, this.y + signY + 50) } as Circle?
                            var diagonal2: Circle? = null
                            if (diagonal != null && diagonal.fill != current?.fill) {
                                signX *= 2
                                signY *= 2
                                diagonal2 = allCircles.children
                                    .find { e -> e.contains(this.x + signX + 50, this.y + signY + 50) } as Circle?
                            }
                            if (diagonal?.fill != current?.fill && diagonal2 == null && current != null)
                                location += loc[count + 1].apply {
                                    this as Circle
                                    centerX = current.centerX + signX
                                    centerY = current.centerY + signY
                                }
                        }
                            if (current != null) {
                                var count2 = 0
                                if (current.fill == Color.AQUA || current.fill == Color.DARKMAGENTA)
                                    while(count2 != 4 ){
                                        location += loc[count2 + count*2 + 1].apply {
                                            this as Circle
                                            centerX = current.centerX + signX*(count2 + count*2)
                                            centerY = current.centerY + signY*(count2 + count*2)
                                        }
                                        count2 += 1
                                    }
                            }

                        }
                        if (current != null && count == 0)
                            location += loc[count].apply {
                                this as Circle
                                centerX = current.centerX
                                centerY = current.centerY
                            }
                        count += 1
                    }
                }
            }

        }
        addEventFilter(MouseEvent.MOUSE_PRESSED, ::choose)

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
                        }
                        location.children.add(this)
                    }
                    Color.DARKBLUE -> {
                        run {
                            radius = 15.0
                        }
                        location.children.add(this)
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
                            rectangle.add(this)
                        }.toBack()
                        when (yCor != 300 || yCor != 200) {
                            (yCor < 300) -> pieces(50.0 + xCor, 750.0 - yCor, Color.WHITE)
                            (yCor > 400) -> pieces(50.0 + xCor, 750.0 - yCor, Color.BLACK)
                        }

                    }

                    tile += 1
                    xCor += 100
                }
                if (yCor == 0) pieces(850.0, 50.0, Color.GREEN)
                pieces(850.0, 50.0, Color.DARKBLUE)
                pieces(850.0, 50.0, Color.DARKBLUE)
                xCor = 0
                tile += 1
                yCor += 100
            }
            rectangle(0, 0, 800.0, 800.0).toBack()
        }
        topBoard.invoke()
    }

}

//            pathList.firstOrNull{
//                val mousePt = it.screenToLocal(evt.screenX, evt.screenY)
//                it.contains(mousePt)
//
//            }.apply {
//
//                pathList.forEach { it.toFront() }
//                if (this != null) {
//                    val newLocX = this.centerX - pieceRadius
//                    val newLocY = this.centerY - pieceRadius
//
//                    allCircles.find { e -> e.contains(pick.last().centerX,pick.last().centerY)  }?.apply {
//                        centerX = newLocX
//                        centerY = newLocY
//                        relocate(centerX - pieceRadius,centerY - pieceRadius )
//                        centerX = newLocX + pieceRadius
//                        centerY = newLocY + pieceRadius
//                    }
//                }
//            }
//            pathList.forEach { it.relocate(935.0, 135.0) }
//            pick.forEach { it.relocate(935.0, 135.0) }
//            if (this != null) {
//                pathList.forEach { it.relocate(935.0, 135.0) }
//                pick.forEach { it.relocate(935.0, 135.0) }
//                var count = 0
//
//                while (count != 4) {
//                    val list = listOf(Pair(100,100),Pair(-100,100),Pair(100,-100),Pair(-100,-100))
//                    var signX = list[count].first
//                    var signY = list[count].second
//
//                    if (count in 0..1) {
//                        if (this.fill == Color.WHITE) signY = -signY
//                        val diagonal = allCircles
//                            .find { e -> e.contains(this.centerX + signX, this.centerY + signY) }
//                        var diagonal2 : Circle? = null
//                            if (diagonal != null && diagonal.fill != this.fill) {
//                                signX *= 2
//                                signY *= 2
//                                diagonal2 = allCircles
//                                    .find { e -> e.contains(this.centerX + signX, this.centerY + signY) }
//                            }
//                        if (diagonal?.fill != this.fill && diagonal2 == null) {
//                            pathList += pieces(this.centerX + signX, this.centerY + signY, Color.DARKBLUE)
//                        }
//                        pick += pieces(this.centerX, this.centerY, Color.GREEN)
//                    }
//                    count +=1
//                }
//            }
//            pick.forEach { it.toFront() }
//        }
//        allCircles.filter { e -> e.fill == Color.WHITE }.find { k -> k.centerY == 50.0 }?.apply {
//            this.stroke = Color.DARKBLUE
//            this.strokeWidth = 15.0
//        }
//        allCircles.filter { e -> e.fill == Color.BLACK }.find { k -> k.centerY == 750.0 }?.apply {
//            this.stroke = Color.DARKGREEN
//            this.strokeWidth = 15.0
//            dama.add(this)
//        }
//    }

//class MainView : View("Moving App") {
//
//    val rectangles = mutableListOf<Rectangle>()
//    var selectedRectangle: Rectangle? = null
//    var selectedOffset: Point2D? = null
//    val positionMessage = SimpleStringProperty("")
//
//    enum class XFormType { NONE, SCALE, ROTATE }
//
//    private fun startDrag(evt : MouseEvent) {
//
//        rectangles.firstOrNull {
//            val mousePt = it.sceneToLocal(evt.x, evt.y)
//            it.contains(mousePt)
//        }.apply {
//            if( this != null ) {
//
//                selectedRectangle = this
//
//                val mousInRectsPos = this.parent.sceneToLocal( evt.sceneX, evt.sceneY )
//                val vizBounds = this.boundsInParent
//
//                selectedOffset = Point2D(
//                    mousInRectsPos.x - vizBounds.minX - (vizBounds.width - this.boundsInLocal.width)/2,
//                    mousInRectsPos.y - vizBounds.minY - (vizBounds.height - this.boundsInLocal.height)/2
//                )
//
//            }
//        }
//    }
//    private fun drag(evt : MouseEvent) {
//
//        val mousePt : Point2D = (evt.source as Pane).sceneToLocal( evt.sceneX, evt.sceneY )
//        if( selectedRectangle != null && selectedOffset!= null  ) {
//
//            selectedRectangle!!.relocate(
//                mousePt.x - selectedOffset!!.x,
//                mousePt.y - selectedOffset!!.y)
//
//            positionMessage.value =
//                "Last Selection: Mouse (${mousePt.x}, ${mousePt.y}) " +
//                        "Moving To (${mousePt.x - selectedOffset!!.x}, ${mousePt.y - selectedOffset!!.y})"
//
//        }
//    }
//
//    private fun endDrag(evt : MouseEvent) {
//        selectedRectangle = null
//        selectedOffset = null
//    }
//
//    override val root = vbox {
//
//        anchorpane {
//            pane {
//
//                fun createRectangle(startX: Double, f: Color, xform: XFormType = XFormType.NONE): Rectangle {
//                    return rectangle(startX, 100.0, 50.0, 50.0) {
//                        fill = f
//                        stroke = Color.BLACK
//                        rectangles.add(this)  // for convenience
//                        layoutX = 25.0
//                        layoutY = 25.0
//                        when (xform) {
//                            XFormType.SCALE -> {
//                                scaleX = 2.0
//                                scaleY = 2.0
//                            }
//                            XFormType.ROTATE -> {
//                                rotate = 45.0
//                            }
//                        }
//                    }
//                }
//
//                createRectangle(100.0, Color.BLUE)
//                createRectangle(300.0, Color.YELLOW, XFormType.SCALE)
//                createRectangle(500.0, Color.GREEN, XFormType.ROTATE)
//
//                anchorpaneConstraints {
//                    topAnchor = 0.0
//                    bottomAnchor = 0.0
//                    rightAnchor = 0.0
//                    leftAnchor = 0.0
//                }
//
//                addEventFilter(MouseEvent.MOUSE_PRESSED, ::startDrag)
//                addEventFilter(MouseEvent.MOUSE_DRAGGED, ::drag)
//                addEventFilter(MouseEvent.MOUSE_RELEASED, ::endDrag)
//            }
//
//            vboxConstraints {
//                vgrow = Priority.ALWAYS
//            }
//        }
//
//        label(positionMessage) {
//            padding = Insets(2.0)
//        }
//
//        padding = Insets(2.0)
//    }
//}


//override val root = pane {
//        var xCor = 0
//        var yCor = 0
//        var boardColor = 0
//        var circles = mutableListOf<Circle>()
//
//        while (yCor != 800) {
//
//            while (xCor != 800) {
//                if (boardColor % 2 == 0){
//                    rectangle(xCor, yCor, 100, 100){
//                        fill = Paint.valueOf("#a94442")
//                    }
//                    if(yCor != 300 && yCor != 400) {
//                       var piece = circle(xCor + 50, yCor + 50, 30) {
//                            if (yCor >= 300 ) fill = Paint.valueOf("#d49942")
//                        }
//
//                    }
//                    boardColor += 2
//                } else rectangle(xCor, yCor, 100, 100)
//                boardColor += 1
//                xCor += 100
//            }
//            boardColor += 1
//        xCor = 0
//        yCor +=100
//        }
//
//    }

//override val root = pane {
//        var xCor = 0
//        var yCor = 0
//        var boardColor = 0
//
//       val bottomBoard = rectangle(0,0,800.0,800.0)
//
//        val topBoard = {
//            while (yCor != 800){
//                while (xCor !=800)
//            rectangle(xCor, yCor, 100.0, 100.0) {
//                fill = Paint.valueOf("#a94442")
//            }
//        }
//        }
//
//
//    }

//        var labelText = SimpleStringProperty()
//        label(labelText) {
//            bind(labelText)
//            addClass(Styles.heading)
//        }
//        button {
//            this.text = "click"
//            action {
//                labelText.set("yo")
//
//            }
//        }

//[19:34] Егорова Инга Сергеевна
//Добавить main, kotlinx Parser для обработки аргументов пользователяСобрать jar-архив средствами Идеи (и уметь его запускать)Нужно ли преобразование к Int при записи в файлДобавить тест на последовательную шифрацию 2 раза подряд, убедиться, что конечный файл равен исходномуДобавить тесты на некорректный ключ (assertThrows)
//
//[19:40] Егорова Инга Сергеевна
//https://www.youtube.com/watch?v=QsQQ5D4TARw
//
//[19:40] Егорова Инга Сергеевна
//onClickListener
//
//[19:42] Егорова Инга Сергеевна
//https://stackoverflow.com/questions/40757911/javafx-adding-actionlistener-to-button
//Javafx adding ActionListener to button
//button.setOnAction(new EventHandler() {
//    @Override public void handle(ActionEvent e) {
//        label.setText("Accepted");
//    }
//});
//In the code above we are defining what will

// chat
//Message List
//Meeting started
//18:49 Meeting started
//Last read
//Recording has started
//18:49 Recording has started
//Profile picture of Егорова Инга Сергеевна.Добавить main, kotlinx Parser для обработки... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:34
//Добавить main, kotlinx Parser для обработки аргументов пользователя
//
//Собрать jar-архив средствами Идеи (и уметь его запускать)
//
//Нужно ли преобразование к Int при записи в файл
//
//Добавить тест на последовательную шифрацию 2 раза подряд, убедиться, что конечный файл равен исходному
//
//Добавить тесты на некорректный ключ (assertThrows)
//
//Profile picture of Егорова Инга Сергеевна.Link https://www.youtube.com/watch?v=QsQQ5D... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:40
//https://www.youtube.com/watch?v=QsQQ5D4TARw
//
//onClickListener by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:40
//onClickListener
//
//Link https://stackoverflow.com/questions/40... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:42
//https://stackoverflow.com/questions/40757911/javafx-adding-actionlistener-to-button
//
//Url Preview for Javafx adding ActionListener to button
//Javafx adding ActionListener to button
//button.setOnAction(new EventHandler() { @Override public void handle(ActionEvent e) { label.setText("Accepted"); } }); In the code above we are defining what will
//
//stackoverflow.com
//Profile picture of Егорова Инга Сергеевна.Model: Board, Cell, Checker Map-> Button, C... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:49
//Model: Board, Cell, Checker
//
//Map-> Button, Checker -> щелкнули по Button, достали checker, можем выполнять операцию
//
//resizable = false by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:50
//resizable = false
//
//Model View Controller by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:52
//Model View Controller
//
//Отрисованы клавиши, по щелчку звук, кнопка ... by Егорова Инга Сергеевна
//Егорова Инга Сергеевна19:55
//Отрисованы клавиши, по щелчку звук, кнопка начать запись - сохраняем в список - остановить - FileChooser - сохранить, файл сохраняется, нет - не сохраняется